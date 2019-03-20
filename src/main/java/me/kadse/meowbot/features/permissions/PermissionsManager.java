package me.kadse.meowbot.features.permissions;

import com.google.gson.*;
import me.kadse.meowbotframework.utils.FileUtils;
import org.javacord.api.DiscordApi;

import java.io.File;
import java.util.HashMap;

public class PermissionsManager {
    private File permissionsFile;
    private DiscordApi discordApi;
    private Gson gson;

    /**
     * HashMap<UserID, PermissionsLevel>
     */
    private HashMap<Long, Integer> permissionLevels;

    public PermissionsManager(DiscordApi discordApi, File permissionsFile) {
        this.discordApi = discordApi;
        this.permissionsFile = permissionsFile;
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        this.permissionLevels = new HashMap<>();

        loadPermissions();
    }

    public void loadPermissions() {
        if (!permissionsFile.exists())
            return;

        JsonObject permissions = new JsonParser().parse(FileUtils.readFile(permissionsFile)).getAsJsonObject();

        for (String id : permissions.keySet()) {
           permissionLevels.put(Long.parseLong(id), permissions.get(id).getAsInt());
        }
    }

    public int getPermissionsLevel(Long user) {
        if(!permissionLevels.containsKey(user))
            return 0;

        return permissionLevels.get(user);
    }

    public void setPermissionsLevel(Long user, int level) {
        permissionLevels.put(user, level);
        FileUtils.saveString(permissionsFile, gson.toJson(permissionLevels));
    }
}
