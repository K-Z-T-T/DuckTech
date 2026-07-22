package org.quiltmc.users.duckteam.DuckTech.blocks;

import net.minecraftforge.registries.RegistryObject;
import org.quiltmc.users.duckteam.DuckTech.DuckTech;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import org.quiltmc.users.duckteam.DuckTech.blocks.blockentity.*;

public class DTBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DuckTech.MODID);

    public static final RegistryObject<BlockEntityType<ShredderBlockEntity>> SHREDDER_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("shredder_block_entity",
                    () -> BlockEntityType.Builder.of(ShredderBlockEntity::new, DTBlocks.SHREDDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<AdvanceShredderBlockEntity>> ADVANCE_SHREDDER_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("advance_shredder_block_entity",
                    () -> BlockEntityType.Builder.of(AdvanceShredderBlockEntity::new, DTBlocks.ADVANCE_SHREDDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<LevitationMachineBlockEntity>> LEVITATION_MACHINE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("levitation_machine_block_entity", () -> BlockEntityType.Builder.of(
                    LevitationMachineBlockEntity::new, DTBlocks.LEVITATION_MACHINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<VoidEssenceCollectorBlockEntity>> VOID_ESSENCE_COLLECTOR_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("void_essence_collector_block_entity", () -> BlockEntityType.Builder.of(
                    VoidEssenceCollectorBlockEntity::new, DTBlocks.VOID_ESSENCE_COLLECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<AirEssenceCollectorBlockEntity>> AIR_ESSENCE_COLLECTOR_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("air_essence_collector_block_entity", () -> BlockEntityType.Builder.of(
                    AirEssenceCollectorBlockEntity::new, DTBlocks.AIR_ESSENCE_COLLECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<VoidHopperBlockEntity>> VOID_HOPPER_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("void_hopper_block_entity", () -> BlockEntityType.Builder.of(
                    VoidHopperBlockEntity::new, DTBlocks.VOID_HOPPER.get()).build(null));

    public static final RegistryObject<BlockEntityType<EssenceFurnaceBlockEntity>> ESSENCE_FURNACE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("essence_furnace_block_entity", () -> BlockEntityType.Builder.of(
                    EssenceFurnaceBlockEntity::new, DTBlocks.ESSENCE_FURNACE.get()).build(null));

    public static final RegistryObject<BlockEntityType<EssenceConversionMachineBlockEntity>> ESSENCE_CONVERSION_MACHINE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("essence_conversion_machine_block_entity", () -> BlockEntityType.Builder.of(
                    EssenceConversionMachineBlockEntity::new, DTBlocks.ESSENCE_CONVERSION_MACHINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<InjectionMachineBlockEntity>> INJECTION_MACHINE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("injection_machine_block_entity", () -> BlockEntityType.Builder.of(
                    InjectionMachineBlockEntity::new, DTBlocks.INJECTION_MACHINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<FE2ThermalEssenceMachineBlockEntity>> FE2THERMAL_ESSENCE_MACHINE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("fe2thermal_essence_machine_block_entity", () -> BlockEntityType.Builder.of(
                    FE2ThermalEssenceMachineBlockEntity::new, DTBlocks.FE2THERMAL_ESSENCE_MACHINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BedrockBreakerBlockEntity>> BEDROCK_BREAKER_BE =
            BLOCK_ENTITY_TYPES.register("bedrock_breaker_block_entity", () -> BlockEntityType.Builder.of(
                    BedrockBreakerBlockEntity::new, DTBlocks.BEDROCK_BREAKER.get()).build(null));
}
