package knickbig.portalcalc;

import com.mojang.brigadier.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@EventBusSubscriber(modid = PortalCalcMod.MOD_ID, value = Dist.CLIENT)
public final class PortalCalcClient {
    private PortalCalcClient() {
    }

    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("CalcPortal")
                .executes(context -> calculateCoordinates()));
    }

    private static int calculateCoordinates() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return 0;
        }

        var dimension = player.level().dimension();
        if (dimension == Level.END) {
            player.displayClientMessage(
                    Component.literal("Portal calculation is not available in the End."),
                    false
            );
            return Command.SINGLE_SUCCESS;
        }

        double multiplier;
        String destination;
        if (dimension == Level.NETHER) {
            multiplier = 8.0;
            destination = "Overworld";
        } else if (dimension == Level.OVERWORLD) {
            multiplier = 1.0 / 8.0;
            destination = "Nether";
        } else {
            player.displayClientMessage(
                    Component.literal("Portal calculation is not available in this dimension."),
                    false
            );
            return Command.SINGLE_SUCCESS;
        }

        player.displayClientMessage(
                Component.literal(destination + " Coordinates  "
                        + "X: " + Math.round(player.getX() * multiplier)
                        + " Y: " + Math.round(player.getY())
                        + " Z: " + Math.round(player.getZ() * multiplier)),
                false
        );
        return Command.SINGLE_SUCCESS;
    }
}