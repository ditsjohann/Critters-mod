package com.critters.block;

import com.critters.entity.ActionHolder;
import com.critters.registry.CritterSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Client-side stand-in for a critter living in a Terrarium or Aquarium. It is never added to the
 * world; it just wanders between random points inside the glass so the renderer can draw it.
 * Positions are block-local (0..1).
 */
public final class DisplayCritter {
    private static final double GLASS_INSET = 1.5 / 16.0;

    private final ItemStack source;
    private final Entity entity;
    private final float scale;
    private final boolean flies;
    private final double stride;
    private final float walkSpeed;

    public double x, y, z;
    public double xo, yo, zo;
    public float yaw, yawO;

    private double tx, ty, tz;
    private int wait;
    private boolean placed;

    DisplayCritter(ItemStack source, Entity entity, @Nullable CritterSpecies species, RandomSource random) {
        this.source = source;
        this.entity = entity;
        float size = Math.max(entity.getBbWidth(), entity.getBbHeight());
        this.scale = Math.min(1.0F, 0.36F / Math.max(0.05F, size));
        this.flies = species != null && species.family == CritterSpecies.Family.FLYER;
        double speed = species != null && species.speed > 0 ? species.speed : 0.15;
        this.stride = speed * 0.04;
        this.walkSpeed = (float) Math.min(1.0, speed * 3.0);
        this.yaw = this.yawO = random.nextFloat() * 360.0F;
        this.wait = random.nextInt(40);
    }

    public ItemStack source() {
        return source;
    }

    public Entity entity() {
        return entity;
    }

    public float scale() {
        return scale;
    }

    void tick(boolean aquarium, BlockPos pos, RandomSource random) {
        boolean swims = aquarium || flies;
        double halfWidth = entity.getBbWidth() * scale / 2.0;
        double min = Math.min(0.5, GLASS_INSET + halfWidth);
        double max = 1.0 - min;
        double floor = aquarium ? 2.5 / 16.0 : 3.0 / 16.0;
        double ceil = Math.max(floor, (aquarium ? 13.0 / 16.0 : 14.5 / 16.0) - entity.getBbHeight() * scale);
        if (!swims) {
            ceil = floor;
        }

        if (!placed) {
            placed = true;
            x = Mth.lerp(random.nextDouble(), min, max);
            z = Mth.lerp(random.nextDouble(), min, max);
            y = Mth.lerp(random.nextDouble(), floor, ceil);
            xo = x;
            yo = y;
            zo = z;
            pickTarget(random, min, max, floor, ceil);
        }

        xo = x;
        yo = y;
        zo = z;
        yawO = yaw;
        entity.tickCount++;

        boolean moving = false;
        if (wait > 0) {
            wait--;
        } else {
            double dx = tx - x;
            double dy = ty - y;
            double dz = tz - z;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double step = aquarium ? 0.012 : flies ? 0.014 : stride;
            if (dist <= step) {
                x = tx;
                y = ty;
                z = tz;
                wait = 20 + random.nextInt(aquarium || flies ? 30 : 90);
                // now and then perform one of its special actions while resting
                if (entity instanceof ActionHolder holder && random.nextInt(3) == 0) {
                    int duration = holder.critterActions().playRandomLocal();
                    wait = Math.max(wait, duration + 5);
                }
                pickTarget(random, min, max, floor, ceil);
            } else {
                x += dx / dist * step;
                y += dy / dist * step;
                z += dz / dist * step;
                moving = true;
                if (dx * dx + dz * dz > 1.0E-6) {
                    float targetYaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
                    yaw = Mth.approachDegrees(yaw, targetYaw, 12.0F);
                }
            }
        }
        x = Mth.clamp(x, min, max);
        z = Mth.clamp(z, min, max);
        y = Mth.clamp(y, floor, ceil);

        if (entity instanceof LivingEntity living) {
            living.walkAnimation.update(moving ? (swims ? 0.6F : walkSpeed) : 0.0F, 0.4F);
            living.yBodyRotO = yawO;
            living.yBodyRot = yaw;
            living.yHeadRotO = yawO;
            living.yHeadRot = yaw;
        }
        entity.setOnGround(!swims);
        if (aquarium) {
            entity.wasTouchingWater = true; // keeps fish swimming instead of flopping on their side
        }
        entity.yRotO = yawO;
        entity.setYRot(yaw);
        double wx = pos.getX() + x;
        double wy = pos.getY() + y;
        double wz = pos.getZ() + z;
        entity.xo = pos.getX() + xo;
        entity.yo = pos.getY() + yo;
        entity.zo = pos.getZ() + zo;
        entity.setPosRaw(wx, wy, wz);
    }

    private void pickTarget(RandomSource random, double min, double max, double floor, double ceil) {
        tx = Mth.lerp(random.nextDouble(), min, max);
        tz = Mth.lerp(random.nextDouble(), min, max);
        ty = Mth.lerp(random.nextDouble(), floor, ceil);
    }
}
