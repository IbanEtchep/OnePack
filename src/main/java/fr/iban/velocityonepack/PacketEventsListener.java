package fr.iban.velocityonepack;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;

public class PacketEventsListener extends PacketListenerAbstract {

    private final VelocityOnePackPlugin plugin;

    public PacketEventsListener(VelocityOnePackPlugin plugin) {
        super(PacketListenerPriority.NORMAL);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        PacketTypeCommon packetType = event.getPacketType();

        if(packetType == PacketType.Play.Server.RESOURCE_PACK_REMOVE){
            if(plugin.getBlockedPlayers().contains(user.getUUID())) {
                event.setCancelled(true);
            }
        }

        if(packetType == PacketType.Play.Server.RESOURCE_PACK_SEND){
            if(plugin.getBlockedPlayers().contains(user.getUUID())) {
                event.setCancelled(true);
            }else {
                plugin.addBlockPlayerAndSync(user.getUUID());
            }
        }
    }
}
