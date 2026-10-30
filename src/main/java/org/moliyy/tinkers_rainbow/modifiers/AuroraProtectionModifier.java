package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class AuroraProtectionModifier extends Modifier implements ProtectionModifierHook {

    private static final int COOLDOWN_TICKS = 400;

    private static final ResourceLocation KEY_LAST_PROTECT =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "aurora_last_protect");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.PROTECTION);
    }

    @Override
    public float getProtectionModifier(IToolStackView tool, ModifierEntry modifier,
                                       EquipmentContext context, EquipmentSlot slotType,
                                       DamageSource source, float modifierValue) {
        // 工具损坏或无耐久，词条失效
        if (tool.isBroken() || tool.getCurrentDurability() <= 0) {
            return modifierValue;
        }

        LivingEntity wearer = context.getEntity();
        if (wearer == null || wearer.level().isClientSide()) {
            return modifierValue;
        }

        // 需要可写的 ToolStack 才能修改持久化数据
        if (!(tool instanceof ToolStack toolStack)) {
            return modifierValue;
        }

        long currentTick = wearer.level().getGameTime();

        // 读取上次触发时间；stored > 0 说明触发过，stored - 1 才是真实 tick
        int stored = toolStack.getPersistentData().getInt(KEY_LAST_PROTECT);
        if (stored > 0) {
            long lastTick = stored - 1L;
            if (currentTick - lastTick < COOLDOWN_TICKS) {
                // 冷却中，不提供保护
                return modifierValue;
            }
        }

        // 冷却结束：记录本次触发时间（+1 避免 0 值歧义）
        toolStack.getPersistentData().putInt(KEY_LAST_PROTECT, (int) (currentTick + 1));

        // 扣除 1 点耐久
        ToolDamageUtil.damage(tool, 1, wearer, null);

        // 提供保护值：每级词条 +1 点
        return modifierValue + modifier.getLevel();
    }
}