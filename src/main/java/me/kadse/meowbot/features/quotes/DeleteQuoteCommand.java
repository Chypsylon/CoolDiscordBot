package me.kadse.meowbot.features.quotes;

import me.kadse.meowbot.features.permissions.PermissionsManager;
import me.kadse.meowbotframework.commands.Command;
import me.kadse.meowbotframework.commands.PermissionLevel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

import java.util.List;

public class DeleteQuoteCommand extends Command {
    private PermissionsManager permissionsManager;
    private QuoteManager quoteManager;

    public DeleteQuoteCommand(PermissionsManager permissionsManager, QuoteManager quoteManager) {
        super("delquote", new String[]{"rmquote"},"Löscht eine Quote", PermissionLevel.MOD);
        this.permissionsManager = permissionsManager;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(TextChannel channel, MessageAuthor sender, String[] args, List<User> mentionedUsers) {
        if(permissionsManager.getPermissionsLevel(sender.getId()) < this.getPermissionLevel().getLevel()) {
            reply(channel, "Du hast nicht die Rechte dafür! (Berechtigungs Level "
                    + permissionsManager.getPermissionsLevel(sender.getId()) + "/" + this.getPermissionLevel().getLevel() + ")");
            return;
        }

        if(args.length < 1) {
            reply(channel, "Syntax: !delquote <QuoteID>");
            return;
        }

        try {
            if(quoteManager.deleteQuote(Long.parseLong(args[0]))) {
                reply(channel, "Quote Gelöscht! RIP");
            } else {
                reply(channel, "Fehler beim Quote löschen! Gibt's das Zitat überhaupt?");
            }
        } catch (NumberFormatException e) {
            reply(channel, "Invalide Quote ID!");
        }
    }
}
