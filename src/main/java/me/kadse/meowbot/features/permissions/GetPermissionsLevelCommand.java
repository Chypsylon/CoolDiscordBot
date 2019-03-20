package me.kadse.meowbot.features.permissions;

import me.kadse.meowbotframework.commands.Command;
import me.kadse.meowbotframework.commands.PermissionLevel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

import java.util.List;

public class GetPermissionsLevelCommand extends Command {
    PermissionsManager permissionsManager;

    public GetPermissionsLevelCommand(PermissionsManager permissionsManager) {
        super("getpermissions", new String[]{"getpermissionslevel", "getpermissions"}, "Setzt das permissions level von einem User", PermissionLevel.ADMIN);
        this.permissionsManager = permissionsManager;
    }

    @Override
    public void execute(TextChannel channel, MessageAuthor sender, String[] args, List<User> mentionedUsers) {
        if(mentionedUsers.size() < 1) {
            reply(channel, "Kein User gementioned! Syntax: !getpermissions @user");
            return;
        }

        reply(channel, mentionedUsers.get(0).getDiscriminatedName() + " - Level " + permissionsManager.getPermissionsLevel(mentionedUsers.get(0).getId()));
    }
}
