package srki2k.tweakedexcavation.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.List;
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
        List<ItemStack> ores = OreDictionary.getOres(strings[1]);

        if (ores.size() == 1) {
            ItemStack itemStack = ores.get(0);
            if (!itemStack.isEmpty()) {
                return itemStack.copy();
            }
            return ItemStack.EMPTY;
        }

        for (ItemStack stack : ores) {
            if (!stack.isEmpty()) {

                ResourceLocation rl = ForgeRegistries.ITEMS.getKey(stack.getItem());
                ItemStack customStack;
                if (strings[0].equals(rl.getNamespace())) {
                    customStack = stack.copy();

                    if (customStack.getMetadata() == 32767) {
                        customStack.setItemDamage(0);
                    }

                    return customStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack searchBlock(String[] strings) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(strings[0] + ":" + strings[1]));

        if (block != null) {
            if (strings.length == 3) {
                try {
                    return new ItemStack(block, 1, Integer.parseInt(strings[2]));
                } catch (NumberFormatException ignored) {
                }
            }

            return new ItemStack(block, 1);
        }
        return ItemStack.EMPTY;
    }

}
