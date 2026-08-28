package net.inmisaddon.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import dev.emi.trinkets.api.TrinketsApi;
import draylar.inmis.Inmis;
import draylar.inmis.item.BackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.inmisaddon.InmisAddonClient;
import net.inmisaddon.model.BabyBackpackModel;
import net.inmisaddon.model.BackpackModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BackpackUtil {

    public static boolean isTrinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");

    private static final BackpackModel backpackModel = new BackpackModel(BackpackModel.getTexturedModelData().createModel());
    private static final BabyBackpackModel babyBackpackModel = new BabyBackpackModel(BabyBackpackModel.getTexturedModelData().createModel());
    private static final MinecraftClient client = MinecraftClient.getInstance();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean renderBackpack(PlayerEntityModel playerEntityModel, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, ItemStack itemStack, EntityRenderState state) {
        if (itemStack.getItem() instanceof BackpackItem backpackItem) {
            if (InmisAddonClient.isFirstPersonLoaded && client.player != null && client.player.isMainPlayer() && client.options.getPerspective().isFirstPerson() && client.player.isSwimming()) {
                return true;
            }
            matrices.push();

            ModelPart root = playerEntityModel.getRootPart();
            root.applyTransform(matrices);

            if (playerEntityModel instanceof BipedEntityModel<?> biped) {
                biped.body.applyTransform(matrices);
            }

            matrices.translate(0D, -0.9D, 0.2D);

            int color = -1;
            if (itemStack.isIn(ItemTags.DYEABLE)) {
                Integer dyedColor = itemStack.get(DataComponentTypes.DYED_COLOR) != null
                        ? itemStack.get(DataComponentTypes.DYED_COLOR).rgb()
                        : null;
                if (dyedColor != null) {
                    color = 0xFF000000 | dyedColor;
                } else {
                    // Undyed leather backpack: match the base Inmis leather tint default.
                    color = -6265536;
                }
            }

            Model model = itemStack.isOf(Inmis.BACKPACKS.get(0)) ? babyBackpackModel : backpackModel;
            Identifier textureId = Identifier.of("inmisaddon", "textures/entity/" + Registries.ITEM.getId(backpackItem).getPath() + ".png");
            RenderLayer renderLayer = RenderLayers.entityCutoutNoCull(textureId);

            queue.getBatchingQueue(0).submitModel(model, state, matrices, renderLayer, light, OverlayTexture.DEFAULT_UV, color, null, 0, null);

            matrices.pop();
            return true;
        }
        return false;
    }

    public static boolean isBackpackEquipped(PlayerEntity playerEntity) {
        if (playerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BackpackItem) {
            return true;
        } else if (isTrinketsLoaded) {
            return TrinketsApi.getTrinketComponent(playerEntity).get().isEquipped(stack -> stack.getItem() instanceof BackpackItem);
        } else {
            return false;
        }
    }

    @Nullable
    public static ItemStack getEquippedBackpack(PlayerEntity playerEntity) {
        if (isTrinketsLoaded && TrinketsApi.getTrinketComponent(playerEntity).get().isEquipped(stack -> stack.getItem() instanceof BackpackItem)) {
            return TrinketsApi.getTrinketComponent(playerEntity).get().getEquipped(stack -> stack.getItem() instanceof BackpackItem).get(0).getRight();
        } else if (playerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BackpackItem) {
            return playerEntity.getEquippedStack(EquipmentSlot.CHEST);
        }
        return null;
    }

}
