package me.kadse.meowbot.features.quotes;

import me.kadse.meowbotframework.commands.Command;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.List;
import java.util.StringJoiner;

public class QuotesCommand extends Command {
    private QuoteManager quoteManager;
    private DiscordApi discordApi;

    public QuotesCommand(QuoteManager quoteManager, DiscordApi discordApi) {
        super("quotes", new String[]{}, "Listet alle Quotes eines Users auf");
        this.quoteManager = quoteManager;
        this.discordApi = discordApi;
    }

    @Override
    public void execute(TextChannel channel, MessageAuthor sender, String[] args, List<User> mentionedUsers) {
        try {
            if (mentionedUsers.size() < 1) {
                StringJoiner stringJoiner = new StringJoiner(" ");
                for (String arg : args) {
                    stringJoiner.add(arg);
                }

                if(quoteManager.getUserIdMap().containsKey(stringJoiner.toString().toLowerCase())) {
                    channel.sendMessage(showQuotes(quoteManager.getUserIdMap().get(stringJoiner.toString().toLowerCase())));
                    return;
                }
                reply(channel, "Syntax: !quotes <@User||Username>");
            } else {
                if (quoteManager.getQuotesUsersMap().containsKey(mentionedUsers.get(0).getId())) {
                    channel.sendMessage(showQuotes(mentionedUsers.get(0).getId()));
                } else {
                    reply(channel, "Dieser User hat noch keine Quotes!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            reply(channel, "Fehler beim Anzeigen der Quotes! (" + e.toString() + ")");
        }
    }

    private EmbedBuilder showQuotes(long userId) {
        try {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(discordApi.getUserById(userId).get());

            int totalCharCount = 0;
            int i = 0;
            for (Long id : quoteManager.getQuotesUsersMap().get(userId)) {
                if (i >= 24 || totalCharCount > 5000) {
                    break;
                }

                Quote quote = quoteManager.getQuotesMap().get(id);
                String content = quote.getContent();

                if(content.trim().equals(""))
                    continue;

                if (content.length() > 500) {
                    content = content.substring(0, 500) + " [...]";
                    totalCharCount += (500 + " [...]".length());
                } else {
                    totalCharCount += content.length();
                }

                embed.addField(String.valueOf(quote.getId()), content);
                i++;
            }

            embed.setFooter("Zeige " + i + " von " + quoteManager.getQuotesUsersMap().get(userId).size() + " Zitate von " + discordApi.getUserById(userId).get().getDiscriminatedName());


            return embed;
        } catch (Exception e) {
            e.printStackTrace();
            return new EmbedBuilder().setDescription(e.toString());
        }
    }
}
