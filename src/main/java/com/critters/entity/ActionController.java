package com.critters.entity;

import com.critters.registry.CritterSpecies;
import java.util.List;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Mob;

/**
 * Runs a critter's special actions. The server picks a random action now and then while the
 * critter is idle and syncs it through an entity data byte; the client records when it started so
 * the model can play it. Client-only display critters (terrariums) can also play actions directly.
 */
public final class ActionController {
    /** Average ticks between actions while idle. */
    private static final int CHANCE = 140;

    private final Mob mob;
    private final EntityDataAccessor<Byte> key;
    private int remaining;
    private CritterAction clientAction = CritterAction.NONE;
    private int clientStart;

    public ActionController(Mob mob, EntityDataAccessor<Byte> key) {
        this.mob = mob;
        this.key = key;
    }

    private List<CritterAction> available() {
        CritterSpecies species = CritterSpecies.byType(mob.getType());
        return species == null ? List.of() : CritterAction.forSpecies(species);
    }

    /** Server tick. idle = the critter is standing still and may start an action. */
    public void serverTick(boolean idle) {
        if (remaining > 0) {
            if (--remaining == 0) {
                mob.getEntityData().set(key, (byte) 0);
            }
            return;
        }
        if (idle && mob.getRandom().nextInt(CHANCE) == 0) {
            List<CritterAction> list = available();
            if (!list.isEmpty()) {
                start(list.get(mob.getRandom().nextInt(list.size())));
            }
        }
    }

    public void start(CritterAction action) {
        remaining = action.duration;
        mob.getEntityData().set(key, (byte) action.ordinal());
        mob.getNavigation().stop();
    }

    public void cancel() {
        if (remaining > 0 || mob.getEntityData().get(key) != 0) {
            remaining = 0;
            mob.getEntityData().set(key, (byte) 0);
        }
    }

    /** True on the server while an action freezes the critter in place. */
    public boolean isBusy() {
        return remaining > 0;
    }

    /** Client: called when the synced byte changes. */
    public void onSynced() {
        clientAction = CritterAction.byId(mob.getEntityData().get(key));
        clientStart = mob.tickCount;
    }

    /** Client-only critters (terrarium/aquarium displays) start actions themselves. */
    public void playLocal(CritterAction action) {
        clientAction = action;
        clientStart = mob.tickCount;
    }

    /** Pick a random action for a display critter; returns its duration (0 if none). */
    public int playRandomLocal() {
        List<CritterAction> list = available();
        if (list.isEmpty()) {
            return 0;
        }
        CritterAction a = list.get(mob.getRandom().nextInt(list.size()));
        playLocal(a);
        return a.duration;
    }

    /** The action playing right now (client view), NONE once it has run its course. */
    public CritterAction current() {
        if (clientAction != CritterAction.NONE && mob.tickCount - clientStart > clientAction.duration) {
            clientAction = CritterAction.NONE;
        }
        return clientAction;
    }

    /** Ticks since the current action started. */
    public float time(float partialTick) {
        return mob.tickCount - clientStart + partialTick;
    }
}
