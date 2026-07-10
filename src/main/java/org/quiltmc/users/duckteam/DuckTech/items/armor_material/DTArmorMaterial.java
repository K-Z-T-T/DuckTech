package org.quiltmc.users.duckteam.DuckTech.items.armor_material;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class DTArmorMaterial {
    public static final ArmorMaterial BASIC_ESSENCE_LEVEL_ONE = new BasicEssenceLevelOne(
    ) {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return 0;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return 0;
        }
    };
    public static final ArmorMaterial BASIC_ESSENCE_LEVEL_TWO = new BasicEssenceLevelTwo(
    ) {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return 0;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return 0;
        }
    };
    public static final ArmorMaterial BASIC_ESSENCE_LEVEL_THREE = new BasicEssenceLevelThree(
    ) {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return 0;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return 0;
        }
    };
}