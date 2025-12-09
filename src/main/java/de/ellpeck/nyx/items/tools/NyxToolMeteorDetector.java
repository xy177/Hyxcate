package de.ellpeck.nyx.items.tools;

import de.ellpeck.nyx.capabilities.NyxWorld;
import de.ellpeck.nyx.sound.NyxSoundEvents;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class NyxToolMeteorDetector extends Item {
    private static final ConcurrentHashMap<UUID, PendingDeletion> PENDING_DELETIONS = new ConcurrentHashMap<>();

    public NyxToolMeteorDetector() {
        // Copied from compass and edited
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rota;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;
            // edit: add this variable for tracking time
            @SideOnly(Side.CLIENT)
            BlockPos meteorPos;

            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null && !stack.isOnItemFrame()) {
                    return 0.0F;
                } else {
                    boolean flag = entityIn != null;
                    Entity entity = flag ? entityIn : stack.getItemFrame();

                    if (worldIn == null) {
                        worldIn = entity.world;
                    }

                    // edit: remove if check for surface world, since this should work in any dimension
                    double d0;
                    double d1 = flag ? (double) entity.rotationYaw : this.getFrameRotation((EntityItemFrame) entity);
                    d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                    double d2 = this.getSpawnToAngle(worldIn, entity) / (Math.PI * 2D);
                    // edit: spin randomly if no spawn angle was found
                    if (Double.isNaN(d2)) {
                        d0 = Math.random();
                    } else {
                        d0 = 0.5D - (d1 - 0.25D - d2);

                    }

                    if (flag) {
                        d0 = this.wobble(worldIn, d0);
                    }

                    return MathHelper.positiveModulo((float) d0, 1.0F);
                }
            }

            @SideOnly(Side.CLIENT)
            private double wobble(World worldIn, double d) {
                if (worldIn.getTotalWorldTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = worldIn.getTotalWorldTime();
                    double d0 = d - this.rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
                    this.rota += d0 * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }

                return this.rotation;
            }

            @SideOnly(Side.CLIENT)
            private double getFrameRotation(EntityItemFrame frame) {
                return MathHelper.wrapDegrees(180 + frame.facingDirection.getHorizontalIndex() * 90);
            }

            @SideOnly(Side.CLIENT)
            private double getSpawnToAngle(World world, Entity e) {
                // edit: find the nearest meteor instead of spawn
                if (this.meteorPos == null || world.getTotalWorldTime() % 100 == 0) {
                    NyxWorld data = NyxWorld.get(world);
                    if (data == null || data.meteorLandingSites.isEmpty()) return Double.NaN;
                    this.meteorPos = Stream.concat(data.meteorLandingSites.stream(), data.cachedMeteorPositions.stream()).min((p1, p2) -> MathHelper.floor(e.getDistanceSq(p1) - e.getDistanceSq(p2))).get();
                }
                if (this.meteorPos == null) return Double.NaN;
                return Math.atan2((double) this.meteorPos.getZ() - e.posZ, (double) this.meteorPos.getX() - e.posX);
            }
        });
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.nyx.meteor_detector"));
        tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.nyx.meteor_detector.hint"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        return new ActionResult<>(handleRightClick(player, world, hand), player.getHeldItem(hand));
    }

    private EnumActionResult handleRightClick(EntityPlayer player, World world, EnumHand hand) {
        if (world.isRemote) return EnumActionResult.SUCCESS;
        player.swingArm(hand);
        // Sneaking + right click: Confirm deletion
        if (player.isSneaking()) {
            return confirmDeletion(player) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
        }
        // Right click: Prompt deletion
        return promptDeletion(player, world);
    }

    private EnumActionResult promptDeletion(EntityPlayer player, World world) {
        NyxWorld data = NyxWorld.get(world);
        if (data == null) return EnumActionResult.PASS;

        BlockPos nearest = Stream.concat(data.meteorLandingSites.stream(), data.cachedMeteorPositions.stream()).min(Comparator.comparingDouble(player::getDistanceSq)).orElse(null);

        if (nearest == null) {
            player.sendMessage(new TextComponentTranslation("info.nyx.meteor_detector.none_found").setStyle(new Style().setColor(TextFormatting.RED)));
            player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorCancel.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F);
            return EnumActionResult.FAIL;
        }

        double dist = player.getDistanceSq(nearest);
        if (dist > 16 * 16) {
            player.sendMessage(new TextComponentTranslation("info.nyx.meteor_detector.too_far", Math.round(Math.sqrt(dist))).setStyle(new Style().setColor(TextFormatting.YELLOW)));
            player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorPrompt.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            return EnumActionResult.FAIL;
        }

        PENDING_DELETIONS.remove(player.getUniqueID());

        PendingDeletion pending = new PendingDeletion(nearest, world.provider.getDimension(), player.getPosition(), world.getTotalWorldTime());
        PENDING_DELETIONS.put(player.getUniqueID(), pending);

        ITextComponent msg = new TextComponentTranslation("info.nyx.meteor_detector.confirm", nearest.getX(), nearest.getY(), nearest.getZ());
        player.sendMessage(msg);

        ITextComponent sneakHint = new TextComponentTranslation("info.nyx.meteor_detector.sneak_confirm").setStyle(new Style().setColor(TextFormatting.GREEN).setItalic(true));
        player.sendMessage(sneakHint);

        player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorPrompt.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F);

        return EnumActionResult.SUCCESS;
    }

    private boolean confirmDeletion(EntityPlayer player) {
        PendingDeletion pending = PENDING_DELETIONS.remove(player.getUniqueID());
        if (pending == null) {
            player.sendMessage(new TextComponentTranslation("info.nyx.meteor_detector.no_pending").setStyle(new Style().setColor(TextFormatting.RED)));
            player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorCancel.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F);
            return false;
        }

        // Timeout check (30 seconds)
        if (player.world.getTotalWorldTime() - pending.timestamp > 600) {
            player.sendMessage(new TextComponentTranslation("info.nyx.meteor_detector.expired").setStyle(new Style().setColor(TextFormatting.RED)));
            player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorCancel.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F);
            return false;
        }

        // Dimension check (same ID)
        if (player.world.provider.getDimension() != pending.dimensionId) {
            player.sendMessage(new TextComponentTranslation("info.nyx.meteor_detector.wrong_dimension").setStyle(new Style().setColor(TextFormatting.RED)));
            player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorCancel.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F);
            return false;
        }

        // Distance check (within 32 blocks of original position)
        if (player.getDistanceSq(pending.playerPos) > 32 * 32) {
            player.sendMessage(new TextComponentTranslation("info.nyx.meteor_detector.moved_too_far").setStyle(new Style().setColor(TextFormatting.RED)));
            player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorCancel.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F);
            return false;
        }

        NyxWorld nyx = NyxWorld.get(player.world);
        if (nyx == null) return false;

        boolean removed = nyx.meteorLandingSites.remove(pending.pos) | nyx.cachedMeteorPositions.remove(pending.pos);

        if (removed) {
            player.sendMessage(new TextComponentTranslation("info.nyx.meteor_detector.deleted", pending.pos.getX(), pending.pos.getY(), pending.pos.getZ()).setStyle(new Style().setColor(TextFormatting.GREEN)));
            player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorConfirm.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            return true;
        } else {
            player.sendMessage(new TextComponentTranslation("info.nyx.meteor_detector.already_gone").setStyle(new Style().setColor(TextFormatting.RED)));
            player.world.playSound(null, player.getPosition(), NyxSoundEvents.meteorDetectorCancel.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F);
            return false;
        }
    }

    private static class PendingDeletion {
        final BlockPos pos;
        final int dimensionId;
        final BlockPos playerPos;
        final long timestamp;

        PendingDeletion(BlockPos pos, int dimensionId, BlockPos playerPos, long timestamp) {
            this.pos = pos;
            this.dimensionId = dimensionId;
            this.playerPos = playerPos;
            this.timestamp = timestamp;
        }
    }
}
