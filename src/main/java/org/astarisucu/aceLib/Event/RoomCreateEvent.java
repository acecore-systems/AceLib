package org.astarisucu.aceLib.Event;

import org.bukkit.event.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RoomCreateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player creator; // ルームを作成したプレイヤー

    public RoomCreateEvent(Player creator) {
        this.creator = creator;
    }

    public Player getCreator() {
        return creator;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
