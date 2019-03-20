package me.kadse.meowbot.features.commands;

import me.kadse.meowbotframework.commands.Command;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

import java.util.List;

public class AwooCommand extends Command {
    public AwooCommand() {
        super("awoo", new String[]{}, "Awooooooo");
    }

    public void execute(TextChannel channel, MessageAuthor sender, String[] args, List<User> mentionedUsers) {
        reply(channel, "Awoooooooooooo");
    }
}
