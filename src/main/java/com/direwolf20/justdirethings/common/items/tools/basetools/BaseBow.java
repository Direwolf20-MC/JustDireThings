package com.direwolf20.justdirethings.common.items.tools.basetools;

import com.direwolf20.justdirethings.common.entities.JustDireArrow;
import com.direwolf20.justdirethings.common.items.datacomponents.JustDireDataComponents;
import com.direwolf20.justdirethings.common.items.interfaces.*;
import com.direwolf20.justdirethings.setup.Config;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseBow extends BowItem implements ToggleableTool, LeftClickableTool {
    protected final EnumSet<Ability> abilities = EnumSet.noneOf(Ability.class);
    protected final Map<Ability, AbilityParams> abilityParams = new EnumMap<>(Ability.class);

    public BaseBow(Properties properties) {
        super(properties);
    }

    public float getMaxDraw() {
        return 20;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResult.PASS;
        if (player.isShiftKeyDown()) {
            openSettings(player);
            return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(hand));
        }
        if (stack.getItem() instanceof PoweredTool poweredTool && PoweredItem.getAvailableEnergy(stack) < poweredTool.getBlockBreakFECost())
            return InteractionResult.PASS;
        return super.use(level, player, hand);
    }

    // TODO(port, stage-4): restore inventoryTick against new (ItemStack, ServerLevel, Entity, @Nullable EquipmentSlot) signature.

    @Override
    protected Projectile createProjectile(Level level, LivingEntity livingEntity, ItemStack itemStack, ItemStack stack, boolean crit) {
        ArrowItem arrowitem = stack.getItem() instanceof ArrowItem arrowitem1 ? arrowitem1 : (ArrowItem) Items.ARROW;
        ToggleableTool toggleableTool = (ToggleableTool) itemStack.getItem();
        if (arrowitem.equals(Items.ARROW)) {
            JustDireArrow justDireArrow = new JustDireArrow(level, livingEntity, stack, itemStack);
            if (crit) {
                justDireArrow.setCritArrow(true);
            }
            if (itemStack.getOrDefault(JustDireDataComponents.EPIC_ARROW, false)) {
                justDireArrow.setEpicArrow(true);
                justDireArrow.setBaseDamage(20d);
                AbilityParams abilityParams = toggleableTool.getAbilityParams(Ability.EPICARROW);
                ToggleableTool.addCooldown(itemStack, Ability.EPICARROW, abilityParams.cooldown, false);
                itemStack.set(JustDireDataComponents.EPIC_ARROW, false);
            }

            if (!toggleableTool.getEnabled(itemStack))
                return customArrow(justDireArrow, stack, itemStack);

            if (canUseAbilityAndDurability(itemStack, Ability.PHASE)) {
                justDireArrow.setPhase(true);
                Helpers.damageTool(itemStack, livingEntity, Ability.PHASE);
            }

            if (canUseAbilityAndDurability(itemStack, Ability.HOMING)) {
                justDireArrow.setHoming(true);
                Helpers.damageTool(itemStack, livingEntity, Ability.HOMING);
                boolean hostileOnly = ToggleableTool.getCustomSetting(itemStack, Ability.HOMING.getName()) == 0;
                LivingEntity aimedAtEntity = findAimedAtEntity(livingEntity, hostileOnly, justDireArrow);
                if (aimedAtEntity != null)
                    justDireArrow.setTargetEntity(aimedAtEntity);
                justDireArrow.setHostileOnly(hostileOnly);
            }

            // TODO(port, stage-5/8): potion-canister arrow abilities relied on ComponentItemHandler / IItemHandler;
            // re-wire once item-side fluid/item handlers are ported to ResourceHandler<ItemResource>.
            if (noPotionAbilitiesActive(itemStack))
                return customArrow(justDireArrow, stack, itemStack);

            return customArrow(justDireArrow, stack, itemStack);
        }
        return super.createProjectile(level, livingEntity, itemStack, stack, crit);
    }

    public LivingEntity findAimedAtEntity(LivingEntity livingEntity, boolean onlyHostile, JustDireArrow justDireArrow) {
        double range = 50;
        Vec3 startVec = livingEntity.getEyePosition(1.0F);
        Vec3 lookVec = livingEntity.getViewVector(1.0F);
        Vec3 endVec = startVec.add(lookVec.scale(range));

        // Perform the ray trace
        HitResult hitResult = livingEntity.level().clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity));

        if (hitResult.getType() != HitResult.Type.MISS) {
            endVec = hitResult.getLocation();
        }

        AABB boundingBox = new AABB(startVec, endVec).inflate(1.0D);
        List<Entity> entities = livingEntity.level().getEntities(livingEntity, boundingBox, EntitySelector.LIVING_ENTITY_STILL_ALIVE);

        LivingEntity closestEntity = null;
        double closestDistance = range * range;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity1) {
                if (onlyHostile && !justDireArrow.isHostileEntity(livingEntity1)) {
                    continue;  // Skip non-hostile entities if onlyHostile is true
                }
                AABB entityBoundingBox = entity.getBoundingBox().inflate(entity.getPickRadius());
                Vec3 entityHitVec = entityBoundingBox.clip(startVec, endVec).orElse(null);

                if (entityBoundingBox.contains(startVec)) {
                    if (closestDistance >= 0.0D) {
                        closestEntity = livingEntity1;
                        closestDistance = 0.0D;
                    }
                } else if (entityHitVec != null) {
                    double distanceToHit = startVec.distanceToSqr(entityHitVec);

                    if (distanceToHit < closestDistance) {
                        closestEntity = livingEntity1;
                        closestDistance = distanceToHit;
                    }
                }
            }
        }
        return closestEntity;
    }

    public boolean noPotionAbilitiesActive(ItemStack itemStack) {
        if (canUseAbilityAndDurability(itemStack, Ability.POTIONARROW))
            return false;
        if (canUseAbilityAndDurability(itemStack, Ability.SPLASH))
            return false;
        if (canUseAbilityAndDurability(itemStack, Ability.LINGERING))
            return false;
        return true;

    }

    @Override
    protected int getDurabilityUse(ItemStack itemStack) {
        return this instanceof PoweredTool poweredTool ? poweredTool.getBlockBreakFECost() : 1;
    }

    @Override
    public EnumSet<Ability> getAllAbilities() {
        return abilities;
    }

    @Override
    public EnumSet<Ability> getAbilities() {
        return abilities.stream()
                .filter(ability -> Config.AVAILABLE_ABILITY_MAP.get(ability).get())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Ability.class)));
    }

    @Override
    public Map<Ability, AbilityParams> getAbilityParamsMap() {
        return abilityParams;
    }

    // TODO(port, stage-4): restore appendHoverText against new signature.
    // TODO(port, stage-5): re-wire damageItem against new EnergyHandler/ItemAccess.

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        if (stack.getItem() instanceof PoweredTool)
            return super.isPrimaryItemFor(stack, enchantment) && canAcceptEnchantments(enchantment);
        return super.isPrimaryItemFor(stack, enchantment);
    }

    private boolean canAcceptEnchantments(Holder<Enchantment> enchantment) {
        return !enchantment.value().effects().has(EnchantmentEffectComponents.REPAIR_WITH_XP);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
