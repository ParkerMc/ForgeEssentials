package com.forgeessentials.protection;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;

public enum MobType {
    BOSS, GOLEM, HOSTILE, PASSIVE, TAMED, TAMEABLE, VILLAGER, UNKNOWN;

    public static MobType getMobType(EntityLiving entity)
    {
        if (entity instanceof EntityDragon || entity instanceof EntityWither)
            return MobType.BOSS;

        if (entity instanceof EntityGolem)
            return MobType.GOLEM;

        if (entity instanceof EntitySlime)
            return ((EntitySlime) entity).getSlimeSize() >= 2 ? MobType.HOSTILE : MobType.PASSIVE;

        if (entity instanceof EntityTameable)
            return ((EntityTameable) entity).isTamed() ? MobType.TAMED : MobType.TAMEABLE;

        // Check for other creatures
        if (entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid)
            return MobType.PASSIVE;

        if (entity instanceof EntityVillager)
            return MobType.VILLAGER;

        if (entity instanceof EntityMob || entity instanceof EntityGhast)
            return MobType.HOSTILE;

        return MobType.UNKNOWN;
    }

}