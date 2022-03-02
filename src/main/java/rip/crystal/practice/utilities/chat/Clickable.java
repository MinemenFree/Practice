package rip.crystal.practice.utilities.chat;

import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Clickable {

    private final List<TextComponent> components = new ArrayList<>();

    public Clickable(String msg) {
        TextComponent message = new TextComponent(CC.translate(msg));

        this.components.add(message);
    }

    public Clickable(String msg, String hoverMsg, String clickString) {
        this.add(msg, hoverMsg, clickString);
    }

    public TextComponent add(String msg, String hoverMsg, String clickString) {
        TextComponent message = new TextComponent(CC.translate(msg));

        if (hoverMsg != null) {
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMsg).create()));
        }

        if (clickString != null) {
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickString));
        }

        this.components.add(message);

        return message;
    }

    public TextComponent addURL(String msg, String hoverMsg, String clickString) {
        TextComponent message = new TextComponent(CC.translate(msg));

        if (hoverMsg != null) {
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMsg).create()));
        }

        if (clickString != null) {
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, clickString));
        }

        this.components.add(message);

        return message;
    }

    public void add(String message) {
        this.components.add(new TextComponent(message));
    }

    public void sendToPlayer(Player player) {
        player.spigot().sendMessage(this.asComponents());
    }

    public void sendToPlayerArray(Player player) {
        for (TextComponent textComponent : this.asComponents()) {
            player.spigot().sendMessage(textComponent);
        }
    }

    public TextComponent[] asComponents() {
        return this.components.toArray(new TextComponent[0]);
    }
}