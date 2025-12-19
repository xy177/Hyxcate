package de.ellpeck.nyx.sound;

import de.ellpeck.nyx.Nyx;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum NyxSoundEvents {
    // Blocks
    denseCrystalBreak("block.dense_crystal.break"),
    denseCrystalPlace("block.dense_crystal.place"),
    lightCrystalBreak("block.light_crystal.break"),
    lightCrystalHit("block.light_crystal.hit"),
    lightCrystalPlace("block.light_crystal.place"),
    lightCrystalStep("block.light_crystal.step"),
    meteoricRockBreak("block.meteoric_rock.break"),
    meteoricRockPlace("block.meteoric_rock.place"),
    meteoricRockStep("block.meteoric_rock.step"),

    // Effects
    frezariteHit("effect.deep_freeze.start"),
    kreknoriteHit("effect.inferno.start"),
    tektiteHit("effect.paralysis.start"),
    paralyzeZap("effect.paralysis.zap"),

    // Entities
    alienKittyIdle("entity.alien_kitty.idle"),
    fallingMeteor("entity.meteor.falling"),
    fallingMeteorImpactFar("entity.meteor.impact.far"),
    fallingMeteorImpact("entity.meteor.impact"),
    fallingStar("entity.star.falling"),
    fallingStarIdle("entity.star.idle"),
    fallingStarImpact("entity.star.impact"),

    // Equip
    equipCrystalline("equip.crystalline"),
    equipMetallic("equip.metallic"),

    // Events (Lunar) - No unique sounds yet, these will be done in the future!
    bloodMoonStart("event.blood_moon.start"),
    harvestMoonStart("event.blue_moon.start"),
    starShowerStart("event.star_shower.start"),

    // Events (Solar) - No unique sounds yet, these will be done in the future!
    solarEclipseStart("event.grim_eclipse.start"),
    redSunStartSpecial("event.red_giant.start.special"),
    redSunStart("event.red_giant.start"),

    // Items
    beamSwordHit("item.beam_sword.hit"),
    beamSwordIdle("item.beam_sword.idle"),
    beamSwordSwing("item.beam_sword.swing"),
    celestialEmblem("item.celestial_emblem.create"),
    hammerHit("item.celestial_warhammer.hit"),
    hammerSpecialLaunch("item.celestial_warhammer.launch"),
    hammerSpecialHit("item.celestial_warhammer.smash"),
    laserShot("item.laser_gun.shoot"),
    meteorDetectorCancel("item.meteor_detector.cancel"),
    meteorDetectorConfirm("item.meteor_detector.confirm"),
    meteorDetectorPrompt("item.meteor_detector.prompt"),

    // Random
    starAura("random.star_aura"),
    starExplode("random.star_explode");

    private final SoundEvent soundEvent;

    NyxSoundEvents(String path) {
        ResourceLocation resourceLocation = new ResourceLocation(Nyx.ID, path);
        this.soundEvent = new SoundEvent(resourceLocation);
        this.soundEvent.setRegistryName(resourceLocation);
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
}
