package de.ellpeck.nyx.block;

import java.util.List;

import javax.annotation.Nullable;

import com.invadermonky.futurefireproof.api.IFireproofBlock;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

// If Future Fireproof is installed, make it fireproof like Netherite!
@Optional.Interface(modid = "futurefireproof", iface = "com.invadermonky.futurefireproof.api.IFireproofBlock", striprefs = true)
public class NyxBlockStairs extends BlockStairs implements IFireproofBlock {
    public NyxBlockStairs(IBlockState modelState) {
        super(modelState);
        this.useNeighborBrightness = true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.nyx.blastproof"));
    }
}
