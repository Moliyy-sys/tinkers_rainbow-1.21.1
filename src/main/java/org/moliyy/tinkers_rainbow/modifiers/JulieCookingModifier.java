package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.moliyy.tinkers_rainbow.entity.RainbowEntityTypes;
import org.moliyy.tinkers_rainbow.model.MuffinEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class JulieCookingModifier extends Modifier
        implements EquipmentChangeModifierHook, InventoryTickModifierHook {

    public static final ModifierId MODIFIER_ID =
            new ModifierId("tinkers_rainbow", "julie_cooking");
    public static final ResourceLocation MUFFIN_RESPAWN_KEY =
            ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "julie_muffin_respawn");
    private static final String PET_BUFF_KEY = "julie_pet_buff_level";
    private static final ResourceLocation BOOST_ID =
            ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "julie_pet_boost");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (!(context.getEntity() instanceof Player player) || player.level().isClientSide) return;
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        if (!context.getChangedSlot().isArmor()) return;

        trySummon(serverLevel, player, tool);
    }


    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world,
                                LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.isClientSide || !(holder instanceof Player player)) return;
        if (!(world instanceof ServerLevel serverLevel)) return;


        boolean isArmorSlot = false;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            if (ItemStack.isSameItemSameComponents(player.getItemBySlot(slot), stack)) {
                isArmorSlot = true;
                break;
            }
        }
        if (!isArmorSlot) return;


        trySummon(serverLevel, player, tool);


        int level = modifier.getLevel();
        for (TamableAnimal pet : player.level().getEntitiesOfClass(TamableAnimal.class, player.getBoundingBox().inflate(32))) {
            if (pet.getOwner() == player) {
                if (!pet.getPersistentData().contains(PET_BUFF_KEY)) {
                    pet.getPersistentData().putInt(PET_BUFF_KEY, level);
                    applyPetBoost(pet, level);
                }
            }
        }
    }


    private static void trySummon(ServerLevel serverLevel, Player player, IToolStackView tool) {

        List<MuffinEntity> activeMuffins = serverLevel.getEntitiesOfClass(MuffinEntity.class, player.getBoundingBox().inflate(128),
                m -> m.isTame() && m.getOwner() == player && m.isAlive());

        if (!activeMuffins.isEmpty()) {
            return;
        }


        long currentTime = serverLevel.getGameTime();
        long respawnTime = tool.getPersistentData().get(MUFFIN_RESPAWN_KEY, (tag, key) -> tag.getLong(key));
        if (respawnTime > 0 && currentTime < respawnTime) {
            return;
        }


        MuffinEntity muffin = new MuffinEntity(RainbowEntityTypes.MUFFIN.get(), player.level());
        muffin.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0);
        muffin.setTame(true, true);
        muffin.setOwnerUUID(player.getUUID());
        player.level().addFreshEntity(muffin);


        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof IModifiable) {
                ToolStack t = ToolStack.from(stack);
                if (t.getModifierLevel(MODIFIER_ID) > 0) {
                    t.getPersistentData().put(MUFFIN_RESPAWN_KEY, LongTag.valueOf(0L));
                }
            }
        }
    }

    private static void applyPetBoost(TamableAnimal pet, int level) {
        AttributeInstance health = pet.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance damage = pet.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance speed = pet.getAttribute(Attributes.MOVEMENT_SPEED);

        double multiplier = 0.5 * level;

        if (health != null && health.getModifier(BOOST_ID) == null) {
            health.addTransientModifier(new AttributeModifier(BOOST_ID, multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            pet.setHealth(pet.getMaxHealth());
        }
        if (damage != null && damage.getModifier(BOOST_ID) == null) {
            damage.addTransientModifier(new AttributeModifier(BOOST_ID, multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
        if (speed != null && speed.getModifier(BOOST_ID) == null) {
            speed.addTransientModifier(new AttributeModifier(BOOST_ID, multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }
}