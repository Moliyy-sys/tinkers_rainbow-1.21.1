package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class CrimsonBurstModifier extends Modifier implements MeleeHitModifierHook {

    private static final int POINTS_TO_BURST = 5;
    private static final int COOLDOWN_TICKS = 20 * 20;

    private static final ResourceLocation KEY_ACCUM =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "crimson_accum");
    private static final ResourceLocation KEY_POINTS =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "crimson_points");

    private static final String KEY_COOLDOWN_UNTIL = "tinkers_rainbow:crimson_cooldown_until";

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier,
                              ToolAttackContext context, float damageDealt) {

        LivingEntity target = context.getLivingTarget();
        if (target == null || target.level().isClientSide() || damageDealt <= 0) {
            return;
        }

        long currentTick = target.level().getGameTime();
        long cooldownUntil = target.getPersistentData().getLong(KEY_COOLDOWN_UNTIL);

        if (currentTick < cooldownUntil) {
            return;
        }

        if (!(tool instanceof ToolStack toolStack)) {
            return;
        }

        float totalDamage = toolStack.getPersistentData().getFloat(KEY_ACCUM);
        int points = toolStack.getPersistentData().getInt(KEY_POINTS);

        totalDamage += damageDealt;
        points++;

        if (points >= POINTS_TO_BURST) {
            toolStack.getPersistentData().putFloat(KEY_ACCUM, 0);
            toolStack.getPersistentData().putInt(KEY_POINTS, 0);

            target.hurt(target.damageSources().magic(), totalDamage);

            target.getPersistentData().putLong(KEY_COOLDOWN_UNTIL, currentTick + COOLDOWN_TICKS);
        } else {
            toolStack.getPersistentData().putFloat(KEY_ACCUM, totalDamage);
            toolStack.getPersistentData().putInt(KEY_POINTS, points);
        }
    }
}