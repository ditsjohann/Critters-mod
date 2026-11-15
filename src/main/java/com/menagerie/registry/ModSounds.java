package com.menagerie.registry;

import com.menagerie.Menagerie;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/** Sound events. Names match the entries in assets/menagerie/sounds.json (the original Mo' Creatures sounds). */
public final class ModSounds {
    public static final SoundEvent HORSE_GRUNT = register("horsegrunt");
    public static final SoundEvent HORSE_HURT = register("horsehurt");
    public static final SoundEvent HORSE_DYING = register("horsedying");
    public static final SoundEvent HORSE_MAD = register("horsemad");
    public static final SoundEvent HORSE_GRUNT_GHOST = register("horsegruntghost");
    public static final SoundEvent HORSE_HURT_GHOST = register("horsehurtghost");
    public static final SoundEvent HORSE_DYING_GHOST = register("horsedyingghost");
    public static final SoundEvent HORSE_MAD_GHOST = register("horsemadghost");
    public static final SoundEvent HORSE_GRUNT_UNDEAD = register("horsegruntundead");
    public static final SoundEvent HORSE_HURT_UNDEAD = register("horsehurtundead");
    public static final SoundEvent HORSE_DYING_UNDEAD = register("horsedyingundead");
    public static final SoundEvent HORSE_MAD_UNDEAD = register("horsemadundead");
    public static final SoundEvent DONKEY_GRUNT = register("donkeygrunt");
    public static final SoundEvent DONKEY_HURT = register("donkeyhurt");
    public static final SoundEvent DONKEY_DYING = register("donkeydying");
    public static final SoundEvent ZEBRA_GRUNT = register("zebragrunt");
    public static final SoundEvent ZEBRA_HURT = register("zebrahurt");
    public static final SoundEvent WING_FLAP = register("wingflap");
    public static final SoundEvent MAGIC_APPEAR = register("appearmagic");
    public static final SoundEvent VANISH = register("vanish");
    public static final SoundEvent DRINKING = register("drinking");
    public static final SoundEvent EATING = register("eating");
    public static final SoundEvent ARMOR_PUT = register("armorput");
    public static final SoundEvent ARMOR_OFF = register("armoroff");
    public static final SoundEvent LION_GRUNT = register("liongrunt");
    public static final SoundEvent LION_HURT = register("lionhurt");
    public static final SoundEvent LION_DEATH = register("liondeath");
    public static final SoundEvent CUB_GRUNT = register("cubgrunt");
    public static final SoundEvent CUB_HURT = register("cubhurt");
    public static final SoundEvent CUB_DYING = register("cubdying");

    private ModSounds() {
    }

    private static SoundEvent register(String name) {
        ResourceLocation id = Menagerie.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {
        // class loading registers the sounds
    }
}
