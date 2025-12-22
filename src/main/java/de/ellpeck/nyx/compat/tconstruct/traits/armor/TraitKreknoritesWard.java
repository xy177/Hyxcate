package de.ellpeck.nyx.compat.tconstruct.traits.armor;

import c4.conarm.lib.traits.AbstractArmorTrait;
import de.ellpeck.nyx.Nyx;
import de.ellpeck.nyx.init.NyxPotions;
import de.ellpeck.nyx.util.NyxUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class TraitKreknoritesWard extends AbstractArmorTrait {
    public TraitKreknoritesWard() {
        super(Nyx.ID + "." + "kreknorites_ward", 0x8F0E0E);
    }

    @Override
    public float onDamaged(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingDamageEvent event) {
        if (NyxUtils.setChance(0.1F)) {
            Entity trueSource = source.getTrueSource();

            if (!player.world.isRemote) {
                if (trueSource instanceof EntityLivingBase) {
                    int level = -1;
                    PotionEffect potionEffect = ((EntityLivingBase) trueSource).getActivePotionEffect(NyxPotions.INFERNO);

                    if (potionEffect != null) {
                        level = potionEffect.getAmplifier();
                    }

                    level = Math.min(4, level + 1);

                    ((EntityLivingBase) trueSource).addPotionEffect(new PotionEffect(NyxPotions.INFERNO, 5 * 20, level));
                }
            }
        }

        return newDamage;
    }
}
