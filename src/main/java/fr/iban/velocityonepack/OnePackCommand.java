package fr.iban.velocityonepack;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;

@Command("onepack")
public class OnePackCommand {

    private final VelocityOnePackPlugin plugin;

    public OnePackCommand(VelocityOnePackPlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("blockreceive")
    public void blockReceive(Player player) {
        plugin.addBlockPlayerAndSync(player.getUniqueId());
        player.sendMessage("§aVous ne recevrez plus de nouveau pack.");
    }

    @Subcommand("unblockreceive")
    public void unblockReceive(Player player) {
        plugin.removeBlockPlayerAndSync(player.getUniqueId());
        player.sendMessage("§aVous recevrez de nouveau les packs.");
    }
}
