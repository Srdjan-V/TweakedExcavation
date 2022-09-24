package srki2k.tweakedexcavation.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class CustomMineralBlocksCache {
    private static final HashMap<String, ItemStack> blocksCache = new HashMap<>();

    public static ItemStack getBlocks(String blockId) {
        ItemStack customStack;

        if (!blocksCache.containsKey(blockId)) {
            String[] strings = blockId.split(":");

            if (strings.length == 1) {
                return ItemStack.EMPTY;
            }

            if (strings.length == 2) {
                customStack = searchOreDictionary(strings);
                if (customStack != ItemStack.EMPTY) {
                    blocksCache.put(blockId, customStack);
                    return customStack;
                }
            }

            customStack = searchBlock(strings);
            if (customStack != ItemStack.EMPTY) {
                blocksCache.put(blockId, customStack);
                return customStack;
            }

            return customStack;
        }

        customStack = blocksCache.get(blockId);
        return customStack == null ? ItemStack.EMPTY : customStack.copy();

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

            if (strings.length > 2) {
                customStack.setItemDamage(Integer.parseInt(strings[2]));
            }

            return customStack.copy();
        }
        return ItemStack.EMPTY;
    }
}
