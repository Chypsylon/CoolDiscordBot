/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kadse.meowbot.features.pressf;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.kadse.meowbotframework.commands.Command;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

/**
 *
 * @author alex
 */
public class PressfCommand extends Command {

    private static final String RESPECT_EMOJI = "🇫";
    private static final String PRESSF_CONFIRMATION_MSG = "Drücke " + RESPECT_EMOJI + " um Respekt für '%s' zu zahlen";
    private static final String PAID_RESPECT_MSG = "**%s hat Respekt für '%s' gezahlt**";
    private static final String FINAL_MSG = "**%d Respekt wurden für '%s' gezahlt**";

    private static final int PRESSF_DURATION_S = 2*60;

    private PressfManager pressfManager;

    public PressfCommand(PressfManager pressfManager) {
        super("pressf", new String[]{}, "Drücke " + RESPECT_EMOJI + " um Respekt zu zahlen");
        this.pressfManager = pressfManager;
    }

    @Override
    public void execute(TextChannel channel, MessageAuthor sender, String[] args, List<User> mentionedUsers) {
        String reason = String.join(" ", args).trim();
        
        if (reason.length() == 0) {
            reply(channel, "Syntax: !pressf <Grund>");
            return;
        }

        CompletableFuture<Message> firstMessageFuture = channel.sendMessage(String.format(PRESSF_CONFIRMATION_MSG, reason));
        try {
            Message firstMessage = firstMessageFuture.get();

            long firstMessageId = firstMessage.getId();

            pressfManager.getActiveRespects().put(firstMessageId, new HashMap<>());

            firstMessage.addReaction(RESPECT_EMOJI);

            firstMessage.addReactionAddListener(reactionAddEvent -> {
                if (reactionAddEvent.getEmoji().equalsEmoji(RESPECT_EMOJI) && !reactionAddEvent.getUser().isYourself()) {
                    try {
                        Message userReactedMessage = reactionAddEvent.getChannel().sendMessage(String.format(PAID_RESPECT_MSG, reactionAddEvent.getUser().getDisplayName(reactionAddEvent.getServer().get()), reason)).get();
                        pressfManager.getActiveRespects().get(firstMessageId).put(reactionAddEvent.getUser().getId(), userReactedMessage);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).removeAfter(PRESSF_DURATION_S, TimeUnit.SECONDS);

            firstMessage.addReactionRemoveListener(reactionRemoveEvent -> {
                if (reactionRemoveEvent.getEmoji().equalsEmoji(RESPECT_EMOJI) && !reactionRemoveEvent.getUser().isYourself()) {
                    Message userReactedMessage = pressfManager.getActiveRespects().get(firstMessageId).remove(reactionRemoveEvent.getUser().getId());
                    userReactedMessage.delete("User removed pressF reaction");
                }
            }).removeAfter(PRESSF_DURATION_S, TimeUnit.SECONDS);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int amountRespectPaid = pressfManager.getActiveRespects().get(firstMessageId).size();
                    channel.sendMessage(String.format(FINAL_MSG, amountRespectPaid, reason));
                }
            }, PRESSF_DURATION_S * 1000);

        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(PressfCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
