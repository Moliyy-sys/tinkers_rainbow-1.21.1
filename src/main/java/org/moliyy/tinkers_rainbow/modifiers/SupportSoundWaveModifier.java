package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.effects.RainbowEffects;
import org.moliyy.tinkers_rainbow.utils.ICooldownModifier;
import org.moliyy.tinkers_rainbow.utils.SyncUtils;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class SupportSoundWaveModifier extends Modifier implements
        InventoryTickModifierHook, ICooldownModifier {

    private static final ResourceLocation COOLDOWN_KEY  =
            ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "support_sound_wave_cooldown");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder,
                                int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {

        if (level.isClientSide || !(holder instanceof Player player) || !isSelected || tool.isBroken()) {
            return;
        }

        if (player.swinging && player.swingTime == 1) {

            long lastUse = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));
            long currentTime = level.getGameTime();
            int cooldownTicks = Config.COMMON.supportSoundWaveCooldown.get();

            if (currentTime - lastUse < cooldownTicks) {
                return;
            }

            tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime));

            SyncUtils.syncHand(player, InteractionHand.MAIN_HAND);

            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            Vec3 end = start.add(look.scale(15.0)); // 射线长度 15 格

            AABB searchBox = new AABB(start, end).inflate(1.0);
            List<LivingEntity> hitEntities = level.getEntitiesOfClass(LivingEntity.class, searchBox,
                    e -> e != player && e.isAlive() && e.getBoundingBox().inflate(0.5).clip(start, end).isPresent());

            float waveDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5F;

            for (LivingEntity entity : hitEntities) {
                entity.addEffect(new MobEffectInstance(RainbowEffects.SUPPORT_DEBUFF, 400, 0));

                entity.invulnerableTime = 0;
                entity.hurt(player.damageSources().indirectMagic(player, player), waveDamage);
            }

            level.playSound(null, start.x, start.y, start.z,
                    SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0F, 1.0F);

            spawnSonicBoomParticles(level, start, look);
        }
    }

    private void spawnSonicBoomParticles(Level level, Vec3 start, Vec3 look) {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 1; i <= 15; i++) {
                Vec3 pos = start.add(look.scale(i));
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    public ResourceLocation getCooldownKey() {
        return COOLDOWN_KEY;
    }

    @Override
    public int getCooldownTicks(IToolStackView tool, ModifierEntry modifier) {
        return Config.COMMON.supportSoundWaveCooldown.get();
    }
}