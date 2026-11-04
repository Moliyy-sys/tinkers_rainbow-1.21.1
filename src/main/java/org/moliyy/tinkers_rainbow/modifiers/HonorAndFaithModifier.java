package org.moliyy.tinkers_rainbow.modifiers;

import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.moliyy.tinkers_rainbow.Config;
import org.moliyy.tinkers_rainbow.utils.ICooldownModifier;
import org.moliyy.tinkers_rainbow.utils.ModifierInteractionHelper;
import org.moliyy.tinkers_rainbow.utils.SyncUtils;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = "tinkers_rainbow")
public class HonorAndFaithModifier extends Modifier implements
        GeneralInteractionModifierHook, ICooldownModifier {

    private static final Map<UUID, JumpData> JUMPING_PLAYERS = new HashMap<>();
    private static final ResourceLocation COOLDOWN_KEY =
            ResourceLocation.fromNamespaceAndPath("tinkers_rainbow", "honor_and_faith_cooldown");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player,
                                       InteractionHand hand, InteractionSource source) {
        if (player.level().isClientSide || tool.isBroken()) return InteractionResult.PASS;

        if (!ModifierInteractionHelper.isSelected(tool, modifier, player)) {
            return InteractionResult.PASS;
        }

        long currentTime = player.level().getGameTime();
        long lastUse = tool.getPersistentData().get(COOLDOWN_KEY, (tag, key) -> tag.getLong(key));

        if (lastUse > 0 && currentTime - lastUse < Config.COMMON.honorAndFaithCooldown.get()) {
            return InteractionResult.FAIL;
        }

        player.setDeltaMovement(player.getDeltaMovement().x, 1.2, player.getDeltaMovement().z);
        player.hurtMarked = true;
        player.resetFallDistance();

        JUMPING_PLAYERS.put(player.getUUID(), new JumpData(modifier.getLevel(), currentTime + 200));

        tool.getPersistentData().put(COOLDOWN_KEY, LongTag.valueOf(currentTime));

        SyncUtils.syncHand(player, hand);

        return InteractionResult.SUCCESS;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        JumpData data = JUMPING_PLAYERS.remove(player.getUUID());
        if (data == null) return;

        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) *
                Config.COMMON.honorAndFaithDamageMultiplier.get().floatValue();

        AABB area = player.getBoundingBox().inflate(4.0);
        for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && e.isAlive())) {
            entity.hurt(player.damageSources().playerAttack(player), damage);
        }

        event.setCanceled(true);
        event.setDistance(0.0F);
        player.resetFallDistance();
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        JumpData data = JUMPING_PLAYERS.get(player.getUUID());
        if (data != null) {
            if (player.level().getGameTime() > data.expireTime()) {
                JUMPING_PLAYERS.remove(player.getUUID());
            }
        }
    }

    @Override
    public ResourceLocation getCooldownKey() {
        return COOLDOWN_KEY;
    }

    @Override
    public int getCooldownTicks(IToolStackView tool, ModifierEntry modifier) {
        return Config.COMMON.honorAndFaithCooldown.get();
    }

    private record JumpData(int level, long expireTime) {}
}