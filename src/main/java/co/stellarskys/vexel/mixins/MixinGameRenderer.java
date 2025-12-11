package co.stellarskys.vexel.mixins;

import dev.deftu.omnicore.api.client.render.OmniResolution;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import co.stellarskys.vexel.Vexel;
import co.stellarskys.vexel.events.GuiEvent;

import static co.stellarskys.vexel.Vexel.getEventBus;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    //#if MC >= 1.21.6
    //$$ @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;incrementFrame()V", shift = At.Shift.AFTER), cancellable = true)
    //#else
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", shift = At.Shift.AFTER), cancellable = true)
    //#endif

    public void hookRender(
            RenderTickCounter tickCounter,
            boolean tick,
            CallbackInfo ci
    ) {
        Vexel.getRenderer().beginFrame(OmniResolution.getWindowWidth(), OmniResolution.getWindowHeight());
        if (
                getEventBus().post(
                        new GuiEvent.Render(),
                        false
                )
        ) ci.cancel();
        Vexel.getRenderer().endFrame();
    }
}