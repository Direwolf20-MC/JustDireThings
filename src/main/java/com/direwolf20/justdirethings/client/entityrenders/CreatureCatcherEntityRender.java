package com.direwolf20.justdirethings.client.entityrenders;

import com.direwolf20.justdirethings.common.entities.CreatureCatcherEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

// TODO(port, stage-18): reinstate the shrinking captured-mob overlay rendering.
// Old 1.21.1 code rotated the thrown item around Y, then in the same call also rendered the
// CreatureCatcherEntity.getEntityFromItemStack result at a shrinking/interpolated position via
// EntityRenderDispatcher.render(...). 26.1 entity rendering is now render-state driven — to
// reproduce this, we would have to build a LivingEntityRenderState for the captured mob every
// frame and submit it via EntityRenderDispatcher.submit(...). Deferred until post-port.
public class CreatureCatcherEntityRender extends ThrownItemRenderer<CreatureCatcherEntity> {
    public CreatureCatcherEntityRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
}
