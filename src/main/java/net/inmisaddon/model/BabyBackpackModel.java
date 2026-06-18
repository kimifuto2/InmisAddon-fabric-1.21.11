package net.inmisaddon.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BabyBackpackModel extends Model {

    private static final Function<Identifier, RenderLayer> LAYER_FACTORY = RenderLayers::entityCutoutNoCull;

    public BabyBackpackModel(ModelPart root) {
        super(root, LAYER_FACTORY);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 24.0F, 0.0F));
        base.addChild("cube_r1", ModelPartBuilder.create().uv(50, 0).mirrored().cuboid(-5.0F, -5.0F, -4.0F, 5.0F, 10.0F, 2.0F, new Dilation(0.0F)).mirrored(false),
                ModelTransform.of(-1.0F, -5.0F, -1.0F, -3.1416F, -1.3963F, 3.1416F));
        base.addChild("cube_r2", ModelPartBuilder.create().uv(50, 0).mirrored().cuboid(-7.0F, -5.0F, 0.0F, 5.0F, 10.0F, 2.0F, new Dilation(0.0F)).mirrored(false),
                ModelTransform.of(-1.0F, -5.0F, 1.0F, 0.0F, -1.3963F, 0.0F));
        base.addChild("cube_r3",
                ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -4.0F, -4.0F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F)).uv(0, 0).mirrored()
                        .cuboid(-1.0F, -4.0F, 3.0F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false).uv(54, 12).cuboid(2.0F, -9.0F, -2.0F, 1.0F, 3.0F, 4.0F, new Dilation(0.0F)).uv(0, 14)
                        .cuboid(2.0F, -4.0F, -3.0F, 1.0F, 4.0F, 6.0F, new Dilation(0.0F)).uv(28, 8).mirrored().cuboid(-1.0F, -10.0F, -3.0F, 3.0F, 10.0F, 6.0F, new Dilation(0.01F)).mirrored(false),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
