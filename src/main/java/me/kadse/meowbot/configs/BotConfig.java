package me.kadse.meowbot.configs;

import me.kadse.meowbotframework.utils.JsonConfig;

import java.io.File;

public class BotConfig extends JsonConfig {
    public BotConfig() {
        super(new File("config/bot.json"));
    }

    public String botToken = "DISCORD TOKEN";
    public char commandPrefix = '!';
    public String permissionsFile = "permissions.json";
}
