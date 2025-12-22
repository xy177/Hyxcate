package de.ellpeck.nyx.block;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.invadermonky.futurefireproof.api.IFireproofBlock;

import de.ellpeck.nyx.init.NyxRegistry;

// If Future Fireproof is installed, make it fireproof like Netherite!
@Optional.Interface(modid = "futurefireproof", iface = "com.invadermonky.futurefireproof.api.IFireproofBlock", striprefs = true)
public class NyxBlockSpaceGlass extends BlockGlass implements IFireproofBlock {
    public NyxBlockSpaceGlass() {
        super(Material.GLASS, false);
        this.setHarvestLevel("pickaxe", 0);
        this.setSoundType(NyxRegistry.LIGHT_CRYSTAL);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.nyx.blastproof"));
    }
}
