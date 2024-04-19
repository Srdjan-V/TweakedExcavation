package io.github.srdjanv.tweakedexcavation.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class CustomMineralBlocks {

    private static CustomMineralBlocks instance;

    public static CustomMineralBlocks getInstance() {
        if (instance == null) instance = new CustomMineralBlocks();
        return instance;
    }

    public static void cleanCache() {
        instance = null;
    }

    private CustomMineralBlocks() {
    }

    private final Map<String, ItemStack> startupMineralBlocksCache = new HashMap<>();

    public ItemStack getBlocksFromCache(String blockId) {
        return startupMineralBlocksCache.get(blockId);
    }

    public boolean searchBlock(String blockId) {
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

    private ItemStack searchOreDictionary(String[] strings) {
        ItemStack customStack;
        for (ItemStack stack : OreDictionary.getOres(strings[1])) {
            if (!stack.isEmpty()) {
                ResourceLocation rl = Item.REGISTRY.getNameForObject(stack.getItem());

                if (rl.getNamespace().equals(strings[0])) {
                    customStack = stack;

                    if (customStack.getMetadata() == Short.MAX_VALUE) {
                        customStack.setItemDamage(0);
                    }

                    return customStack.copy();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack searchBlock(String[] strings) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(strings[0] + ":" + strings[1]));
        if (block == null) {
            return ItemStack.EMPTY;
        }

        if (strings.length == 3) {
            try {
                return new ItemStack(block, 1, Integer.parseInt(strings[2]));
            } catch (NumberFormatException ignored) {
            }
        }
        return new ItemStack(block, 1);
    }

}
