package srki2k.tweakedexcavation.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class CustomMineralBlocks {
    public static ItemStack getBlocks(String blockId) {
        ItemStack customStack = ItemStack.EMPTY;

        String[] strings = blockId.split(":");

        if (strings.length == 1) {
            return customStack;
        }

        if (strings.length == 2) {
            customStack = searchOreDictionary(strings);
            if (customStack != ItemStack.EMPTY) {
                return customStack;
            }
        }

        if (strings.length == 2 || strings.length == 3) {
            customStack = searchBlock(strings);
            return customStack;
        }

        return customStack;
    }

    private static ItemStack searchOreDictionary(String[] strings) {
        ItemStack customStack;
        for (ItemStack stack : OreDictionary.getOres(strings[1])) {
            if (!stack.isEmpty()) {
                ResourceLocation rl = Item.REGISTRY.getNameForObject(stack.getItem());

                if (rl.getNamespace().equals(strings[0])) {
                    customStack = stack;

                    if (customStack.getMetadata() == 32767) {
                        customStack.setItemDamage(0);
                    }

                    return customStack.copy();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack searchBlock(String[] strings) {
        ItemStack customStack;
        Block block = Block.getBlockFromName(strings[0] + ":" + strings[1]);

        if (block != null) {
            customStack = new ItemStack(block);

            if (strings.length == 3) {
                customStack.setItemDamage(Integer.parseInt(strings[2]));
            }

            return customStack.copy();
        }
        return ItemStack.EMPTY;
    }

}
