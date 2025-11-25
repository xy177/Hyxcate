package de.ellpeck.nyx.compat.jei;

import de.ellpeck.nyx.init.NyxBlocks;
import de.ellpeck.nyx.init.NyxItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEI implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        addJEIInfo(registry, new ItemStack(NyxBlocks.cyberCrystal));
        addJEIInfo(registry, new ItemStack(NyxItems.fallenStar));
        addJEIInfo(registry, new ItemStack(NyxItems.frezariteBoots));
        addJEIInfo(registry, new ItemStack(NyxItems.tektiteGemCluster));
    }

    private static void addJEIInfo(IModRegistry registry, ItemStack stack) {
        registry.addIngredientInfo(stack, VanillaTypes.ITEM, stack.getTranslationKey() + ".jei_desc");
    }
}
