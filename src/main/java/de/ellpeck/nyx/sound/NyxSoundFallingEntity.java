package de.ellpeck.nyx.sound;

import de.ellpeck.nyx.entities.NyxEntityFallingStar;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NyxSoundFallingEntity extends MovingSound {
    private final NyxEntityFallingStar entityStar;

    public NyxSoundFallingEntity(NyxEntityFallingStar entityStar, SoundEvent soundEvent, float volume) {
        super(soundEvent, SoundCategory.AMBIENT);
        this.entityStar = entityStar;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = volume;
    }

    @Override
    public void update() {
        if (this.entityStar.isDead || this.entityStar.collided) {
            this.donePlaying = true;
        } else {
            this.xPosF = (float) this.entityStar.posX;
            this.yPosF = (float) this.entityStar.posY;
            this.zPosF = (float) this.entityStar.posZ;
        }
    }
}
