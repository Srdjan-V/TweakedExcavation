package srki2k.tweakedexcavation.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class CustomMineralBlocks {

    private static final Map<String, ItemStack> startupMineralBlocksCache = new HashMap<>();

    public static void cleanCache() {
        startupMineralBlocksCache.clear();
    }

    public static ItemStack getBlocksFromCache(String blockId) {
        return startupMineralBlocksCache.get(blockId);
    }

    public static boolean searchBlock(String blockId) {
        if (startupMineralBlocksCache.containsKey(blockId)) {
            return true;
        }

        String[] strings = blockId.split(":");

        if (strings.length == 1) {
            return false;
        }

        ItemStack customStack;
        if (strings.length == 2) {
            customStack = searchOreDictionary(strings);
            if (customStack != ItemStack.EMPTY) {
                startupMineralBlocksCache.put(blockId, customStack);
                return true;
            }
        }

        if (strings.length == 2 || strings.length == 3) {
            customStack = searchBlock(strings);
            if (customStack != ItemStack.EMPTY) {
                startupMineralBlocksCache.put(blockId, customStack);
                return true;
            }
        }

        return false;
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
