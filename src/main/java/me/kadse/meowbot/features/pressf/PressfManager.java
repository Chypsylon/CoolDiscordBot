package me.kadse.meowbot.features.pressf;

import lombok.Getter;
import org.javacord.api.entity.message.Message;

import java.util.HashMap;
import java.util.List;

public class PressfManager {
    @Getter
    private HashMap<Long, HashMap<Long, Message>> activeRespects;

    public PressfManager() {
        this.activeRespects = new HashMap<>();
    }
}
