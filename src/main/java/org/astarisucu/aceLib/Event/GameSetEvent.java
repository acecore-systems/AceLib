package org.astarisucu.aceLib.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameSetEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String winner; // 勝者の名前など

    public GameSetEvent(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
