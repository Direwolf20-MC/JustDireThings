package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.JustDireArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.TippableArrowRenderState;
import net.minecraft.resources.Identifier;

public class JustDireArrowRenderer extends ArrowRenderer<JustDireArrow, TippableArrowRenderState> {
    public static final Identifier NORMAL_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");
    public static final Identifier TIPPED_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/tipped_arrow.png");

    public JustDireArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTextureLocation(TippableArrowRenderState state) {
        return state.isTipped ? TIPPED_ARROW_LOCATION : NORMAL_ARROW_LOCATION;
    }

    @Override
    public TippableArrowRenderState createRenderState() {
        return new TippableArrowRenderState();
    }

    @Override
    public void extractRenderState(JustDireArrow entity, TippableArrowRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isTipped = entity.getColor() > 0;
    }
}
