package de.ellpeck.nyx.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NyxSoundFallenEntity extends MovingSound {
    private final EntityItem entityStar;

    public NyxSoundFallenEntity(EntityItem entityStar, SoundEvent soundEvent, float volume) {
        super(soundEvent, SoundCategory.AMBIENT);
        this.entityStar = entityStar;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = volume;
    }

    @Override
    public void update() {
        if (this.entityStar.isDead) {
            this.volume = this.volume - 0.05F;
        } else {
            this.xPosF = (float) this.entityStar.posX;
            this.yPosF = (float) this.entityStar.posY;
            this.zPosF = (float) this.entityStar.posZ;
        }
    }

    @Override
    public boolean isDonePlaying() {
        return this.volume <= 0.0F;
    }
}
