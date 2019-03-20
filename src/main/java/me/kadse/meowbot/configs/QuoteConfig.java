package me.kadse.meowbot.configs;

import me.kadse.meowbotframework.utils.JsonConfig;

import java.io.File;

public class QuoteConfig extends JsonConfig {
    public QuoteConfig() {
        super(new File("config/quotes.json"));
    }

    public String quoteEmoji = "💾";
    public String deleteQuoteEmoji = "🚽";
    public String confirmationEmoji = "✅";
    public int deleteQuoteCount = 5;
    public String quotesFile = "quotes.json";

}
