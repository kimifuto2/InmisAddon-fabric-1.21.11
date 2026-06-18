package net.inmisaddon.mixin.compat;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.trinkets.api.SlotReference;
import draylar.inmis.Inmis;
import draylar.inmis.client.TrinketBackpackRenderer;
import draylar.inmis.item.TrinketBackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.inmisaddon.model.BabyBackpackModel;
import net.inmisaddon.model.BackpackModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

@SuppressWarnings("rawtypes")
@Environment(EnvType.CLIENT)
@Mixin(TrinketBackpackRenderer.class)
public abstract class TrinketBackpackRendererMixin {

    private final BackpackModel backpackModel = new BackpackModel(BackpackModel.getTexturedModelData().createModel());
    private final BabyBackpackModel babyBackpackModel = new BabyBackpackModel(BabyBackpackModel.getTexturedModelData().createModel());

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderMixin(ItemStack stack, SlotReference slotReference, EntityModel contextModel, MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
            LivingEntityRenderState state, float limbAngle, float limbDistance, CallbackInfo info) {

        if (!Inmis.CONFIG.trinketRendering) {
            info.cancel();
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            info.cancel();
            return;
        }

        if (!client.player.getEquippedStack(EquipmentSlot.CHEST).isEmpty() && client.player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof TrinketBackpackItem) {
            info.cancel();
            return;
        }

        matrices.push();
        if (contextModel instanceof BipedEntityModel) {
            ((BipedEntityModel) contextModel).body.applyTransform(matrices);
        }
        matrices.translate(0D, -0.9D, 0.2D);

        int color = -1;
        if (stack.isIn(ItemTags.DYEABLE) && stack.get(DataComponentTypes.DYED_COLOR) != null) {
            color = stack.get(DataComponentTypes.DYED_COLOR).rgb();
        }

        Model model = stack.isOf(Inmis.BACKPACKS.get(0)) ? babyBackpackModel : backpackModel;
        Identifier textureId = Identifier.of("inmisaddon", "textures/entity/" + Registries.ITEM.getId(stack.getItem()).getPath() + ".png");
        RenderLayer renderLayer = RenderLayers.entityCutoutNoCull(textureId);

        queue.getBatchingQueue(0).submitModel(model, state, matrices, renderLayer, light, OverlayTexture.DEFAULT_UV, color, null, 0, null);

        matrices.pop();

        info.cancel();
    }
}
