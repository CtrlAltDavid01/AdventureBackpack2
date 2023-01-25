package com.darkona.adventurebackpack.entity.ai;

import com.darkona.adventurebackpack.reference.BackpackTypes;
import com.darkona.adventurebackpack.util.Wearing;
import java.util.List;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;

/**
 * Created by Darkona on 12/10/2014.
 */
public class EntityAIAvoidPlayerWithBackpack extends EntityAIBase {
    private BackpackTypes type;

    public final IEntitySelector field_98218_a = new AvoidEntitySelector(this);

    /**
     * The com.darkona.adventurebackpack.entity we are attached to
     */
    public EntityCreature theEntity;

    private double farSpeed;
    private double nearSpeed;
    private Entity closestLivingEntity;
    private float distanceFromEntity;

    /**
     * The PathEntity of our com.darkona.adventurebackpack.entity
     */
    private PathEntity entityPathEntity;

    /**
     * The PathNavigate of our com.darkona.adventurebackpack.entity
     */
    private PathNavigate entityPathNavigate;

    /**
     * The class of the com.darkona.adventurebackpack.entity we should avoid
     */
    private Class targetEntityClass;

    public EntityAIAvoidPlayerWithBackpack(
            EntityCreature par1EntityCreature,
            Class par2Class,
            float par3,
            double par4,
            double par6,
            BackpackTypes type) {
        this.theEntity = par1EntityCreature;
        this.targetEntityClass = par2Class;
        this.distanceFromEntity = par3;
        this.farSpeed = par4;
        this.nearSpeed = par6;
        this.entityPathNavigate = par1EntityCreature.getNavigator();
        this.setMutexBits(1);
        this.type = type;
    }

    @Override
    public boolean shouldExecute() {
        if (this.targetEntityClass == EntityPlayer.class) {
            if (this.theEntity instanceof EntityTameable && ((EntityTameable) this.theEntity).isTamed()) {
                return false;
            }

            List<?> list = this.theEntity.worldObj.selectEntitiesWithinAABB(
                    this.targetEntityClass,
                    this.theEntity.boundingBox.expand(this.distanceFromEntity, 3.0D, this.distanceFromEntity),
                    this.field_98218_a);

            if (list.isEmpty()) {
                return false;
            }

            for (Object player : list) {
                if (BackpackTypes.getType(Wearing.getWearingBackpack((EntityPlayer) player)) == type) {
                    this.closestLivingEntity = (Entity) player;
                }
            }

            if (this.closestLivingEntity == null) {
                return false;
            }

            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(
                    this.theEntity,
                    16,
                    7,
                    Vec3.createVectorHelper(
                            this.closestLivingEntity.posX,
                            this.closestLivingEntity.posY,
                            this.closestLivingEntity.posZ));

            if (vec3 == null) {
                return false;
            } else if (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord)
                    < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity)) {
                return false;
            } else {
                this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
                return this.entityPathEntity != null && this.entityPathEntity.isDestinationSame(vec3);
            }
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        return !this.entityPathNavigate.noPath();
    }

    @Override
    public void startExecuting() {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }

    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
    }

    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0D) {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        } else {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }

    static EntityCreature func_98217_a(EntityAIAvoidPlayerWithBackpack par0EntityAIAvoidEntity) {
        return par0EntityAIAvoidEntity.theEntity;
    }
}
