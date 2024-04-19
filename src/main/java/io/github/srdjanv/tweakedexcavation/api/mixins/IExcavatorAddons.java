package io.github.srdjanv.tweakedexcavation.api.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface IExcavatorAddons {

    void initEnergyStorage();

    void onUpdate(CallbackInfo ci);

}
