package forked.godlycow.org.splinecartunlocked.mixin.client;

import forked.godlycow.org.splinecartunlocked.entity.TrackFollowerEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenEffectRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(method = "getViewBlockingState",
            at = @At("HEAD"), cancellable = true)
    private static void splinecartunlocked$modifySuffocatingBlock(Player player, CallbackInfoReturnable<BlockState> info) {
        var vehicle = player.getVehicle();
        while (vehicle != null) {
            if (vehicle instanceof TrackFollowerEntity) {
                var world = player.level();
                var eye = player.getEyePosition();
                var pos = BlockPos.containing(eye);
                var state = world.getBlockState(pos);
                var eyeBox = AABB.ofSize(eye, 0.2, 0.2, 0.2);

                if (state.getRenderShape() != RenderShape.INVISIBLE && state.isViewBlocking(world, pos, eyeBox)) {
                    info.setReturnValue(state);
                } else {
                    info.setReturnValue(null);
                }
                return;
            }

            vehicle = vehicle.getVehicle();
        }
    }
}
