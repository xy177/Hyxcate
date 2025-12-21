package de.ellpeck.nyx.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IWorld;
import de.ellpeck.nyx.capability.NyxWorld;
import de.ellpeck.nyx.event.lunar.NyxEventBloodMoon;
import de.ellpeck.nyx.event.lunar.NyxEventBlueMoon;
import de.ellpeck.nyx.event.lunar.NyxEventFullMoon;
import de.ellpeck.nyx.event.lunar.NyxEventStarShower;
import de.ellpeck.nyx.event.solar.NyxEventGrimEclipse;
import de.ellpeck.nyx.event.solar.NyxEventRedGiant;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("unused")
@ZenRegister
@ZenExpansion("crafttweaker.world.IWorld")
public class CraftTweaker {
    @ZenMethod
    public static boolean isHyxcateBloodMoon(IWorld world) {
        World mcWorld = CraftTweakerMC.getWorld(world);
        NyxWorld data = NyxWorld.get(mcWorld);
        return data != null && data.currentLunarEvent instanceof NyxEventBloodMoon;
    }

    @ZenMethod
    public static boolean isHyxcateBlueMoon(IWorld world) {
        World mcWorld = CraftTweakerMC.getWorld(world);
        NyxWorld data = NyxWorld.get(mcWorld);
        return data != null && data.currentLunarEvent instanceof NyxEventBlueMoon;
    }

    @ZenMethod
    public static boolean isHyxcateFullMoon(IWorld world) {
        World mcWorld = CraftTweakerMC.getWorld(world);
        NyxWorld data = NyxWorld.get(mcWorld);
        return data != null && data.currentLunarEvent instanceof NyxEventFullMoon;
    }

    @ZenMethod
    public static boolean isHyxcateStarShower(IWorld world) {
        World mcWorld = CraftTweakerMC.getWorld(world);
        NyxWorld data = NyxWorld.get(mcWorld);
        return data != null && data.currentLunarEvent instanceof NyxEventStarShower;
    }

    @ZenMethod
    public static boolean isHyxcateGrimEclipse(IWorld world) {
        World mcWorld = CraftTweakerMC.getWorld(world);
        NyxWorld data = NyxWorld.get(mcWorld);
        return data != null && data.currentSolarEvent instanceof NyxEventGrimEclipse;
    }

    @ZenMethod
    public static boolean isHyxcateRedGiant(IWorld world) {
        World mcWorld = CraftTweakerMC.getWorld(world);
        NyxWorld data = NyxWorld.get(mcWorld);
        return data != null && data.currentSolarEvent instanceof NyxEventRedGiant;
    }
}
