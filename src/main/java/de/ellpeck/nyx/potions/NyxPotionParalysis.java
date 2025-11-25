package de.ellpeck.nyx.potions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class NyxPotionParalysis extends NyxPotionBase {
    public NyxPotionParalysis(String name, boolean isBadEffect, int liquidColor) {
        super(name, isBadEffect, liquidColor);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {

        // Disable mob ai when paralyzed
        if (entity instanceof EntityPlayer) {
            return;
        } else if (entity instanceof EntityLiving) {
            ((EntityLiving) entity).setNoAI(true);
        } else {
            entity.updateBlocked = true;
        }
    }
}
