package net.inmisaddon.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.trinkets.api.TrinketsApi;
import draylar.inmis.client.BackpackFeature;
import draylar.inmis.item.BackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.inmisaddon.util.BackpackUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Pair;

@Environment(EnvType.CLIENT)
@Mixin(BackpackFeature.class)
public abstract class BackpackFeatureMixin extends FeatureRenderer {

    public BackpackFeatureMixin(FeatureRendererContext context) {
        super(context);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderMixin(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, EntityRenderState state, float limbAngle, float limbDistance, CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        var chestSlot = client.player.getEquippedStack(EquipmentSlot.CHEST);
        if (chestSlot.getItem() instanceof BackpackItem) {
            boolean isTrinketBackpack = TrinketsApi.getTrinketComponent(client.player)
                .map(comp -> comp.getAllEquipped().stream()
                    .anyMatch(pair -> pair.getRight().getItem() instanceof BackpackItem))
                .orElse(false);
            if (isTrinketBackpack) return;

            PlayerEntityModel model = (PlayerEntityModel) this.getContextModel();
            if (BackpackUtil.renderBackpack(model, matrices, queue, light, chestSlot, state)) {
                info.cancel();
            }
        }
    }
}
