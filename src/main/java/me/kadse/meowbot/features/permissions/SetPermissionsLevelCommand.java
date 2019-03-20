package me.kadse.meowbot.features.permissions;

import me.kadse.meowbotframework.commands.Command;
import me.kadse.meowbotframework.commands.PermissionLevel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

import java.util.List;

public class SetPermissionsLevelCommand extends Command {
    PermissionsManager permissionsManager;

    public SetPermissionsLevelCommand(PermissionsManager permissionsManager) {
        super("setpermissions", new String[]{"setpermissionslevel", "setpermissions"}, "Setzt das permissions level von einem User", PermissionLevel.ADMIN);
        this.permissionsManager = permissionsManager;
    }

    @Override
    public void execute(TextChannel channel, MessageAuthor sender, String[] args, List<User> mentionedUsers) {
        if(permissionsManager.getPermissionsLevel(sender.getId()) < this.getPermissionLevel().getLevel()) {
            reply(channel, "Du hast nicht die Rechte dafür! (Berechtigungs Level "
                    + permissionsManager.getPermissionsLevel(sender.getId()) + "/" + this.getPermissionLevel().getLevel() + ")");
            return;
        }

        if(mentionedUsers.size() < 1) {
            reply(channel, "Kein User gementioned! Syntax: !setpermissions <level> @user");
            return;
        }

        int level;
        try {
            level = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            reply(channel, "Oida, Level muss eine Zahl sein. Syntax: !setpermissions <level> @user");
            return;
        }

        permissionsManager.setPermissionsLevel(mentionedUsers.get(0).getId(), level);
        reply(channel, "Permissions Level für " + mentionedUsers.get(0).getDiscriminatedName() + " auf " + level + " gesetzt!");
    }
}
