package srki2k.tweakedexcavation.api.ihelpers;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface IExcavatorAddons {

    void initEnergyStorage();

    void onUpdate(CallbackInfo ci);

}
