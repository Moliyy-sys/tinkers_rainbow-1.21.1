package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.LootingModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.LootingContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Map;

public class AdvanceLootingModifier extends Modifier
        implements LootingModifierHook,
        ProcessLootModifierHook,
        EnchantmentModifierHook {

    private static final List<Item> EXTRA_LOOT = List.of(
            Items.BONE,
            Items.LEATHER,
            Items.EGG,
            Items.SPIDER_EYE,
            Items.GUNPOWDER,
            Items.ROTTEN_FLESH

    );

    private static final float BASE_DROP_CHANCE = 0.25F;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.WEAPON_LOOTING);
        hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
        hookBuilder.addHook(this, ModifierHooks.ENCHANTMENTS);
    }



    @Override
    public int updateLooting(IToolStackView tool, ModifierEntry modifier,
                             LootingContext context, int looting) {
        return looting + modifier.getLevel();
    }



    @Override
    public int updateEnchantmentLevel(IToolStackView tool, ModifierEntry modifier,
                                      Holder<Enchantment> enchantment, int level) {
        if (enchantment.is(Enchantments.LOOTING)) {
            return level + modifier.getLevel() + 1;
        }
        return level;
    }



    @Override
    public void updateEnchantments(IToolStackView tool, ModifierEntry modifier,
                                   HolderLookup.RegistryLookup<Enchantment> lookup,
                                   Map<Holder<Enchantment>, Integer> map) {
        Holder<Enchantment> looting = lookup.getOrThrow(Enchantments.LOOTING);
        map.merge(looting, modifier.getLevel() + 1, Integer::sum);
    }



    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier,
                            List<ItemStack> generatedLoot, LootContext context) {
        if (!context.hasParam(LootContextParams.DAMAGE_SOURCE)) {
            return;
        }

        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof LivingEntity target)) {
            return;
        }

        LivingEntity holder = context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
        if (holder == null) {
            return;
        }

        DamageSource damageSource = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        LootingContext lootingContext = new LootingContext(
                holder, target, damageSource, EquipmentSlot.MAINHAND
        );
        int effectiveLooting = LootingModifierHook.getLooting(tool, lootingContext, 0);

        RandomSource random = context.getRandom();
        addExtraLoot(generatedLoot, modifier.getLevel(), effectiveLooting, random);
    }

    private void addExtraLoot(List<ItemStack> loot, int modifierLevel,
                              int lootingLevel, RandomSource random) {
        if (EXTRA_LOOT.isEmpty()) {
            return;
        }

        float chance = Math.min(BASE_DROP_CHANCE * (1 + 0.15F * modifierLevel), 1.0F);
        if (random.nextFloat() >= chance) {
            return;
        }

        int count = 1 + random.nextInt(lootingLevel + 1);
        Item chosen = EXTRA_LOOT.get(random.nextInt(EXTRA_LOOT.size()));
        loot.add(new ItemStack(chosen, count));
    }
}