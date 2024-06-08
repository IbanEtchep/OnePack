package fr.iban.velocityonepack;

import com.github.retrooper.packetevents.PacketEvents;
import fr.iban.bukkitcore.CoreBukkitPlugin;
import fr.iban.bukkitcore.event.CoreMessageEvent;
import fr.iban.bukkitcore.manager.MessagingManager;
import fr.iban.common.messaging.Message;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static fr.iban.common.messaging.CoreChannel.PLAYER_QUIT_CHANNEL;

public final class VelocityOnePackPlugin extends JavaPlugin implements Listener {

    private Set<UUID> blockedPlayers;
    private final String RESOURCEPACK_BLOCK_CHANNEL = "resourcepack_block_channel";
    private final String RESOURCEPACK_ALLOW_CHANNEL = "resourcepack_allow_channel";

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(true)
                .bStats(true);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        blockedPlayers = new HashSet<>();
        PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener(this));
        PacketEvents.getAPI().init();
        registerCommands();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    private void registerCommands() {
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        commandHandler.register(new OnePackCommand(this));
        commandHandler.registerBrigadier();
    }

    public Set<UUID> getBlockedPlayers() {
        return blockedPlayers;
    }

    @EventHandler
    public void onCoreMessage(CoreMessageEvent e) {
        Message message = e.getMessage();

        switch (e.getMessage().getChannel()) {
            case RESOURCEPACK_BLOCK_CHANNEL -> blockedPlayers.add(UUID.fromString(message.getMessage()));
            case RESOURCEPACK_ALLOW_CHANNEL, PLAYER_QUIT_CHANNEL -> blockedPlayers.remove(UUID.fromString(message.getMessage()));
        }
    }

    public void addBlockPlayerAndSync(UUID uuid) {
        blockedPlayers.add(uuid);
        MessagingManager messagingManager = CoreBukkitPlugin.getInstance().getMessagingManager();
        messagingManager.sendMessage(RESOURCEPACK_BLOCK_CHANNEL, uuid.toString());
    }

    public void removeBlockPlayerAndSync(UUID uuid) {
        blockedPlayers.remove(uuid);
        MessagingManager messagingManager = CoreBukkitPlugin.getInstance().getMessagingManager();
        messagingManager.sendMessage(RESOURCEPACK_ALLOW_CHANNEL, uuid.toString());
    }
}
