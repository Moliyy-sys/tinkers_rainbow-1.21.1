package org.moliyy.tinkers_rainbow.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.moliyy.tinkers_rainbow.TinkersRainbow;
import org.moliyy.tinkers_rainbow.model.MuffinEntity;

public class RainbowEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, TinkersRainbow.MODID);


    public static final DeferredHolder<EntityType<?>, EntityType<MuffinEntity>> MUFFIN = ENTITY_TYPES.register(
            "muffin",
            () -> EntityType.Builder.of(MuffinEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(10)
                    .build("muffin")
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);

        eventBus.addListener(RainbowEntityTypes::registerAttributes);
    }

    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(MUFFIN.get(), MuffinEntity.createAttributes().build());
    }
}
