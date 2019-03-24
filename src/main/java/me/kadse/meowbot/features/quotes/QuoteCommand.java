package me.kadse.meowbot.features.quotes;

import me.kadse.meowbot.utils.TimeUtils;
import me.kadse.meowbotframework.commands.Command;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class QuoteCommand extends Command {
    private QuoteManager quoteManager;
    private DiscordApi discordApi;

    private Random random = new Random();

    public QuoteCommand(QuoteManager quoteManager, DiscordApi discordApi) {
        super("quote", new String[]{}, "Zitat anzeigen");
        this.quoteManager = quoteManager;
        this.discordApi = discordApi;
    }

    @Override
    public void execute(TextChannel channel, MessageAuthor sender, String[] args, List<User> mentionedUsers) {
        if (quoteManager.getQuotesMap().isEmpty()) {
            reply(channel, "Keine Quotes gespeichert");
            return;
        }
        
        try {
            long id;

            if(mentionedUsers.size() > 0) {
                if(quoteManager.getQuotesUsersMap().containsKey(mentionedUsers.get(0).getId())) {
                    List<Long> quotes = quoteManager.getQuotesUsersMap().get(mentionedUsers.get(0).getId());
                    id = quotes.get(random.nextInt(quotes.size()));
                } else {
                    reply(channel, "Dieser User hat noch keine Quotes!");
                    return;
                }
            } else {
                if(args.length == 0) {
                    id = (Long) quoteManager.getQuotesMap().keySet().toArray()[random.nextInt(quoteManager.getQuotesMap().size())];
                } else {
                    try {
                        id = Long.parseLong(args[0]);
                    } catch (NumberFormatException e) {
                        StringJoiner stringJoiner = new StringJoiner(" ");
                        for (String arg : args) {
                            stringJoiner.add(arg);
                        }

                        if(quoteManager.getUserIdMap().containsKey(stringJoiner.toString().toLowerCase())) {
                            List<Long> userQuotes = quoteManager.getQuotesUsersMap().get(quoteManager.getUserIdMap().get(stringJoiner.toString().toLowerCase()));
                            id = userQuotes.get(random.nextInt(userQuotes.size()));
                        } else {
                            reply(channel, "Kein Quote gefunden! Syntax: !quote <QuoteID\\||Username\\||@User>");
                            return;
                        }
                    }
                }
            }

            if(!quoteManager.getQuotesMap().containsKey(id)) {
                reply(channel, "Quote nicht gefunden");
                return;
            }

            Quote quote = quoteManager.getQuotesMap().get(id);
            channel.sendMessage(quote.toEmbed(discordApi));
        } catch (Exception e) {
            reply(channel, "Fehler beim Anzeigen der Quotes! (" + e.toString() + ")");
            e.printStackTrace();
        }
    }
}
