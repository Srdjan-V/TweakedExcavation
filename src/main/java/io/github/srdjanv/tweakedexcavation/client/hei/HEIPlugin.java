package io.github.srdjanv.tweakedexcavation.client.hei;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalMultiblock;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import io.github.srdjanv.tweakedexcavation.TweakedExcavation;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;

import static blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList;

@mezz.jei.api.JEIPlugin
public class HEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        BaseHEIUtil.initExcavatorGui(registry.getJeiHelpers().getGuiHelper(),
                new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.EXCAVATOR.getMeta()),
                TweakedExcavation.MODID);
        registry.addRecipeCategories(new ExcavatorCategory());
    }


    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(ExcavatorHandler.MineralMix.class, ExcavatorWrapper::new, ExcavatorCategory.UID);
        registry.addRecipes(mineralList.keySet(), ExcavatorCategory.UID);
        registry.addRecipeCatalyst(BaseHEIUtil.getExcavatorCatalyst(), ExcavatorCategory.UID);
    }

}
