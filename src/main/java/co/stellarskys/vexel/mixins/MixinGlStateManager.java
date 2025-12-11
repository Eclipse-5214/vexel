package co.stellarskys.vexel.mixins;

import co.stellarskys.vexel.api.nvg.StateTracker;
import com.mojang.blaze3d.opengl.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class MixinGlStateManager {
    @Inject(method = "_bindTexture", at = @At("HEAD"), remap = false)
    private static void onBindTexture(int texture, CallbackInfo ci) {
        StateTracker.setPreviousBoundTexture(texture);
    }
}