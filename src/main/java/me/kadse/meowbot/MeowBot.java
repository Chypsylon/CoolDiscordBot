package me.kadse.meowbot;

import me.kadse.meowbot.configs.BotConfig;
import me.kadse.meowbot.configs.QuoteConfig;
import me.kadse.meowbot.features.commands.AwooCommand;
import me.kadse.meowbot.features.permissions.GetPermissionsLevelCommand;
import me.kadse.meowbot.features.permissions.PermissionsManager;
import me.kadse.meowbot.features.permissions.SetPermissionsLevelCommand;
import me.kadse.meowbot.features.pressf.PressfManager;
import me.kadse.meowbot.features.quotes.*;
import me.kadse.meowbotframework.DiscordBot;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.UserStatus;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import me.kadse.meowbot.features.pressf.PressfCommand;

public class MeowBot {
    private BotConfig botConfig;
    private QuoteConfig quoteConfig;

    private QuoteManager quoteManager;
    private PermissionsManager permissionsManager;
    private PressfManager pressfManager;

    public MeowBot() {
        botConfig = new BotConfig();
        botConfig.load();

        quoteConfig = new QuoteConfig();
        quoteConfig.load();

        DiscordBot discordBot = new DiscordBot(botConfig.botToken, botConfig.commandPrefix);
        System.out.println("Invite Link: " + discordBot.getDiscordApi().createBotInvite());

        //Instantiate Features
        quoteManager = new QuoteManager(discordBot.getDiscordApi(), new File(quoteConfig.quotesFile));
        permissionsManager = new PermissionsManager(discordBot.getDiscordApi(), new File(botConfig.permissionsFile));
        pressfManager = new PressfManager();

        //Instantiate Commands
        new AwooCommand();
        new PressfCommand(pressfManager);
        new QuoteCommand(quoteManager, discordBot.getDiscordApi());
        new QuotesCommand(quoteManager, discordBot.getDiscordApi());
        new DeleteQuoteCommand(permissionsManager, quoteManager);
        new SetPermissionsLevelCommand(permissionsManager);
        new GetPermissionsLevelCommand(permissionsManager);

        discordBot.getDiscordApi().addReactionAddListener(new QuoteReactionListener(quoteConfig, quoteManager));

        discordBot.getDiscordApi().updateActivity(ActivityType.WATCHING, "Katzen");

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            String cmd = line.split(" ")[0];
            String[] args = line.replace(cmd + " ", "").split(" ");

            switch(cmd) {
                case "exit": {
                    System.exit(0);
                    break;
                }
                case "msg": {
                    StringBuilder sb = new StringBuilder();
                    for(int i = 2; i < args.length; i++) {
                        sb.append(" ").append(args[i]);
                    }
                    discordBot.getDiscordApi().getServerById(args[0]).get().getTextChannelsByName(args[1]).get(0).sendMessage(sb.toString());
                    break;
                }
                case "servers": {
                    for (Server server : discordBot.getDiscordApi().getServers()) {
                        System.out.println(server.getIdAsString() + " - " + server.getName());
                    }
                    break;
                }
            }
        }
    }
}
