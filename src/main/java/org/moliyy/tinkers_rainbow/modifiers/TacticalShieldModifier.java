package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.utils.ICooldownModifier;
import org.moliyy.tinkers_rainbow.utils.SyncUtils;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TacticalShieldModifier extends Modifier implements ModifyDamageModifierHook, EquipmentChangeModifierHook, ICooldownModifier {

    public static final ResourceLocation COOLDOWN_KEY = ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "tactical_shield_cooldown");
    public static final ResourceLocation SHIELD_KEY = ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "tactical_shield_value");
    public static final ResourceLocation MAX_SHIELD_KEY = ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "tactical_shield_max");


    private static final EquipmentSlot[] PRIORITY_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_HURT, ModifierHooks.EQUIPMENT_CHANGE);
    }

    @Override
    public ResourceLocation getCooldownKey() {
        return COOLDOWN_KEY;
    }

    @Override
    public int getCooldownTicks(IToolStackView tool, ModifierEntry modifier) {
        return Config.COMMON.shieldCooldown.get();
    }

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player player && !player.level().isClientSide) {
            long cooldownUntil = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));
            long currentTime = player.level().getGameTime();

            if (currentTime >= cooldownUntil) {
                float maxShield = (float) (player.getMaxHealth() * Config.COMMON.shieldHealthMultiplier.get());
                tool.getPersistentData().put(MAX_SHIELD_KEY, FloatTag.valueOf(maxShield));
                tool.getPersistentData().put(SHIELD_KEY, FloatTag.valueOf(maxShield));
            }
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();

        if (entity instanceof Player player && !player.level().isClientSide) {
            boolean stillHasModifier = false;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                IToolStackView equippedTool = context.getReplacementTool();
                if (slot.isArmor()) {

                }
            }
        }
        tool.getPersistentData().remove(SHIELD_KEY);
        tool.getPersistentData().remove(COOLDOWN_KEY);
    }

    private boolean isPrimarySlot(EquipmentContext context, EquipmentSlot slotType) {
        for (EquipmentSlot slot : PRIORITY_SLOTS) {
            IToolStackView tool = context.getToolInSlot(slot);

            if (tool != null && tool.getModifier(RainbowModifiers.tactical_shield.getId()) != null) {

                return slot == slotType;
            }
        }
        return false;
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier,
                                   EquipmentContext context, EquipmentSlot slotType,
                                   DamageSource source, float amount, boolean isDirectDamage) {
        if (amount <= 0.0F) return amount;


        if (!isPrimarySlot(context, slotType)) {
            return amount;
        }

        LivingEntity entity = context.getEntity();
        if (!(entity instanceof Player player) || player.level().isClientSide()) {
            return amount;
        }

        long currentTime = player.level().getGameTime();
        long cooldownUntil = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));

        if (currentTime < cooldownUntil) {
            return amount;
        }

        float currentShield = tool.getPersistentData().get(SHIELD_KEY, (tag, key) -> tag.getFloat(key));
        float maxShield = tool.getPersistentData().get(MAX_SHIELD_KEY, (tag, key) -> tag.getFloat(key));

        if (maxShield <= 0) {
            maxShield = (float) (player.getMaxHealth() * Config.COMMON.shieldHealthMultiplier.get());
            currentShield = maxShield;
            tool.getPersistentData().put(MAX_SHIELD_KEY, FloatTag.valueOf(maxShield));
            tool.getPersistentData().put(SHIELD_KEY, FloatTag.valueOf(maxShield));
        }

        if (currentShield > 0.0F) {
            float reduction = Config.COMMON.tacticalDamageReduction.get().floatValue();
            float reducedDamage = amount * (1.0F - reduction);

            if (source.getEntity() instanceof LivingEntity attacker) {
                float reflectDamage = maxShield * Config.COMMON.tacticalReflectPercentage.get().floatValue();
                attacker.hurt(player.damageSources().thorns(player), reflectDamage);
            }

            if (currentShield >= reducedDamage) {
                tool.getPersistentData().put(SHIELD_KEY, FloatTag.valueOf(currentShield - reducedDamage));
                SyncUtils.syncArmor(player, slotType);
                return 0.0F;
            } else {
                float overflowDamage = reducedDamage - currentShield;
                tool.getPersistentData().put(SHIELD_KEY, FloatTag.valueOf(0.0F));

                tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime + Config.COMMON.shieldCooldown.get()));
                SyncUtils.syncArmor(player, slotType);

                return overflowDamage;
            }
        }

        return amount;
    }
}