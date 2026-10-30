package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.BiConsumer;


public class GeneBoostModifier extends Modifier implements AttributesModifierHook, MeleeDamageModifierHook {

    private static final ResourceLocation HEALTH_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(TinkersRainbow.MODID, "gene_boost_health");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES, ModifierHooks.MELEE_DAMAGE);
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier,
                              EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {

        if (!slot.isArmor()) {
            return;
        }

        double amount = 20.0 * modifier.getLevel() * 1.2;

        consumer.accept(
                Attributes.MAX_HEALTH.value(),
                new AttributeModifier(
                        HEALTH_MODIFIER_ID,
                        amount,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier,
                                ToolAttackContext context, float baseDamage, float damage) {

        float multiplier = 1.0F + (0.5F * modifier.getLevel());
        return baseDamage * multiplier;
    }
}
