package de.ellpeck.nyx.entities.renderer;

import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NyxModelCometKitty extends ModelOcelot {
    ModelRenderer helmet;

    public NyxModelCometKitty() {
        super();
        this.setTextureOffset("helmet.main", 38, 19);
        this.helmet = new ModelRenderer(this, "helmet");
        this.helmet.addBox("main", -3F, -3.5F, -4.5F, 6, 6, 7);
        this.helmet.setRotationPoint(0.0F, 15.0F, -9.0F);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        this.helmet.rotateAngleX = headPitch / (180.0F / (float) Math.PI);
        this.helmet.rotateAngleY = netHeadYaw / (180.0F / (float) Math.PI);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        if (this.isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5F / 2.0F, 1.5F / 2.0F, 1.5F / 2.0F);
            GlStateManager.translate(0.0F, 10.0F * scale, 4.0F * scale);
            this.helmet.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.helmet.render(scale);
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);
        EntityOcelot cat = (EntityOcelot) entity;
        this.helmet.rotationPointY = 15.0F;
        this.helmet.rotationPointZ = -9.0F;

        if (cat.isSneaking()) {
            this.helmet.rotationPointY += 2.0F;
        } else if (cat.isSitting()) {
            this.helmet.rotationPointY += -3.3F;
            ++this.helmet.rotationPointZ;
        }
    }
}
