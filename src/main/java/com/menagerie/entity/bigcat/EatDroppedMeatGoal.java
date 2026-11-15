package com.menagerie.entity.bigcat;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

/**
 * Big cats walk to raw meat or fish lying on the ground and eat it. This is how wild cubs are
 * readied for taming in the original (its {@code getClosestItem} logic in {@code MoCEntityBigCat}).
 */
public class EatDroppedMeatGoal extends Goal {
    private final BigCat cat;
    private ItemEntity target;
    private int cooldown;

    public EatDroppedMeatGoal(BigCat cat) {
        this.cat = cat;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        this.cooldown = 20;
        if (!this.cat.wantsToEat() || this.cat.isVehicle() || this.cat.isOrderedToSit() || this.cat.getTarget() != null) {
            return false;
        }
        List<ItemEntity> meat = this.cat.nearbyMeat(12.0);
        this.target = meat.stream().min(Comparator.comparingDouble(this.cat::distanceToSqr)).orElse(null);
        return this.target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.cat.wantsToEat() && this.cat.getTarget() == null;
    }

    @Override
    public void start() {
        this.cat.getNavigation().moveTo(this.target, 1.1);
    }

    @Override
    public void stop() {
        this.target = null;
        this.cat.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.cat.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (this.cat.distanceToSqr(this.target) < 4.0) {
            this.target.getItem().shrink(1);
            if (this.target.getItem().isEmpty()) {
                this.target.discard();
            }
            this.cat.eatMeat();
            this.target = null;
        } else if (this.cat.getNavigation().isDone()) {
            this.cat.getNavigation().moveTo(this.target, 1.1);
        }
    }
}
