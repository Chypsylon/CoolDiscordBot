package me.kadse.meowbot.features.quotes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.kadse.meowbot.utils.TimeUtils;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter @AllArgsConstructor
public class Quote {
    //private static final Pattern IMAGE_PATTERN = Pattern.compile("https://.*\\.discordapp\\..*/.*");
    private static final Pattern IMAGE_PATTERN = Pattern.compile("https?:\\/\\/.*\\/.*\\.(png|jpg|jpeg|gif)($|\\s|\\?.*)");

    private long id;
    private long author;
    private String authorName;
    private long quoter;
    private String quoterName;
    private long channel;
    private long timestamp;
    private String content;

    public EmbedBuilder toEmbed(DiscordApi discordApi) throws ExecutionException, InterruptedException {
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(discordApi.getUserById(this.getAuthor()).get())
                .setDescription(this.getContent())
                .setFooter("Zitat " + this.getId() + " hinzugefügt von " + this.getQuoterName() + " am " + TimeUtils.timestampToString(this.getTimestamp()));

        Matcher imageMatcher = IMAGE_PATTERN.matcher(this.getContent());
        if(imageMatcher.find()) {
            embed.setImage(imageMatcher.group());
        }

        return embed;
    }
}
