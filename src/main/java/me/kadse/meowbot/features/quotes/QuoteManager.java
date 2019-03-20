package me.kadse.meowbot.features.quotes;

import com.google.gson.*;
import lombok.Getter;
import me.kadse.meowbotframework.utils.FileUtils;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuoteManager {
    private DiscordApi discordApi;
    private File quotesFile;
    private Gson gson;

    /**
     * HashMap<QuoteID, QuoteObject>
     */
    @Getter
    private HashMap<Long, Quote> quotesMap;

    /**
     * HashMap<UserID, List<QuoteID>>
     */
    @Getter
    private HashMap<Long, List<Long>> quotesUsersMap;

    /**
     * HashMap<Username, UserID>
     */
    @Getter
    private HashMap<String, Long> userIdMap;

    public QuoteManager(DiscordApi discordApi, File quotesFile) {
        this.discordApi = discordApi;
        this.quotesFile = quotesFile;
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        this.quotesMap = new HashMap<>();
        this.quotesUsersMap = new HashMap<>();
        this.userIdMap = new HashMap<>();

        loadQuotes();
    }

    public void loadQuotes() {
        if(!quotesFile.exists())
            return;

        JsonArray quotes = new JsonParser().parse(FileUtils.readFile(quotesFile)).getAsJsonArray();

        for (int i = 0; i < quotes.size(); i++) {
            JsonObject quote = quotes.get(i).getAsJsonObject();

            quotesMap.put(quote.get("id").getAsLong(),
                new Quote(
                        quote.get("id").getAsLong(),
                        quote.get("author").getAsLong(),
                        quote.get("authorName").getAsString(),
                        quote.get("quoter").getAsLong(),
                        quote.get("quoterName").getAsString(),
                        quote.get("channel").getAsLong(),
                        quote.get("timestamp").getAsLong(),
                        quote.get("content").getAsString()
                ));

            try {
                if(!quotesUsersMap.containsKey(quote.get("author").getAsLong()))
                    quotesUsersMap.put(quote.get("author").getAsLong(), new ArrayList<>());

                quotesUsersMap.get(quote.get("author").getAsLong()).add(quote.get("id").getAsLong());
                userIdMap.put(quote.get("authorName").getAsString().toLowerCase(), quote.get("author").getAsLong());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void saveQuote(Message message, User quoter) {
        if(quotesMap.containsKey(message.getId()))
            return;

        String content = message.getContent();

        if(content.length() < 1 && message.getEmbeds().size() > 0) {
            content = message.getEmbeds().get(0).getDescription().get();
            if(message.getEmbeds().get(0).getImage().isPresent())
                content += (" " + message.getEmbeds().get(0).getImage().get().getUrl());
            else if(message.getEmbeds().get(0).getThumbnail().isPresent())
                content += (" " + message.getEmbeds().get(0).getThumbnail().get().getUrl());
        }

        Quote quote = new Quote(
                message.getId(),
                message.getAuthor().getId(),
                message.getAuthor().getDisplayName(),
                quoter.getId(),
                quoter.getDisplayName(message.getServer().get()),
                message.getChannel().getId(),
                System.currentTimeMillis()/1000L,
                content
        );

        quotesMap.put(message.getId(), quote);

        if(!quotesUsersMap.containsKey(message.getAuthor().getId())) {
            quotesUsersMap.put(message.getAuthor().getId(), new ArrayList<>());
        }

        quotesUsersMap.get(message.getAuthor().getId()).add(quote.getId());

        FileUtils.saveString(quotesFile, gson.toJson(quotesMap.values()));
    }

    public boolean deleteQuote(long quoteId) {
        if(quotesMap.containsKey(quoteId)) {
            quotesMap.remove(quoteId);
        } else {
            return false;
        }

        FileUtils.saveString(quotesFile, gson.toJson(quotesMap.values()));
        return true;
    }
}
