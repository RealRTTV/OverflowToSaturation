package ca.rttv.overflowtosaturation.mixin;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Shadow
    private int foodLevel;
    @Shadow
    private float foodSaturationLevel;

    @Inject(at = @At("HEAD"), method = "add(IF)V", cancellable = true)
    private void init(int food, float saturationModifier, CallbackInfo ci) {
        float saturation = (float) food * saturationModifier * 2.0F;
        int projectedFoodLevel = this.foodLevel + food;
        if (projectedFoodLevel > 20) {
            ci.cancel();
            this.foodLevel = 20;
            int additionalSaturation = projectedFoodLevel - 20;
            this.foodSaturationLevel = Math.min(20, this.foodSaturationLevel + saturation + additionalSaturation);
        }
    }
}
