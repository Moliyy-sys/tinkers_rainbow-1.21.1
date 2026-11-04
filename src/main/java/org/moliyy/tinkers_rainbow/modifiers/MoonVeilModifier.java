package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.utils.ICooldownModifier;
import org.moliyy.tinkers_rainbow.utils.SyncUtils;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MoonVeilModifier extends Modifier implements
        ModifyDamageModifierHook, ICooldownModifier {

    public static final ResourceLocation COOLDOWN_KEY =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "moon_veil_cooldown");
    public static final ResourceLocation ACTIVE_KEY =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "moon_veil_active");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_HURT);
    }


    @Override
    public ResourceLocation getCooldownKey() {
        return COOLDOWN_KEY;
    }

    @Override
    public int getCooldownTicks(IToolStackView tool, ModifierEntry modifier) {
        return Config.COMMON.moonVeilCooldown.get();
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (amount <= 0.0F) return amount;

        LivingEntity entity = context.getEntity();
        if (!(entity instanceof Player player) || player.level().isClientSide()) {
            return amount;
        }

        long currentTime = player.level().getGameTime();
        long activeUntil = tool.getPersistentData().get(ACTIVE_KEY, (tag, key) -> tag.getLong(key));
        long cooldownUntil = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));

        int durationTicks = Config.COMMON.moonVeilDuration.get();
        int cooldownTicks = Config.COMMON.moonVeilCooldown.get();
        float healRatio = Config.COMMON.moonVeilHealPercentage.get().floatValue();

        if (currentTime < activeUntil) {
            player.heal(amount * healRatio);
            spawnParticles(player);
            return 0.0F;
        }

        if (currentTime >= cooldownUntil) {
            tool.getPersistentData().put(ACTIVE_KEY, LongTag.valueOf(currentTime + durationTicks));
            tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime + cooldownTicks));

            SyncUtils.syncArmor(player, slotType);

            player.heal(amount * healRatio);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 0.5F, 1.2F);
            spawnParticles(player);
            return 0.0F;
        }

        return amount;
    }

    private void spawnParticles(Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ENCHANT,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    10, 0.5, 0.5, 0.5, 0.2);
        }
    }
}