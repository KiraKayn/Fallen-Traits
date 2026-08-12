package net.kayn.fallen_traits.content.traits.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class TraitCompatibility {

    private record Pending(MobTrait self, Supplier<? extends MobTrait> other) {}

    private static final List<Pending> PENDING = new ArrayList<>();
    private static Map<MobTrait, Set<MobTrait>> resolved;

    public static void register(MobTrait self, Supplier<? extends MobTrait> other) {
        PENDING.add(new Pending(self, other));
    }

    private static Map<MobTrait, Set<MobTrait>> get() {
        if (resolved == null) {
            resolved = new HashMap<>();
            for (Pending p : PENDING) {
                MobTrait other = p.other().get();
                resolved.computeIfAbsent(p.self(), k -> new HashSet<>()).add(other);
                resolved.computeIfAbsent(other, k -> new HashSet<>()).add(p.self());
            }
        }
        return resolved;
    }

    public static boolean isIncompatible(MobTrait trait, LivingEntity target) {
        Set<MobTrait> set = get().get(trait);
        if (set == null || set.isEmpty()) return false;
        if (!MobTraitCap.HOLDER.isProper(target)) return false;
        MobTraitCap cap = MobTraitCap.HOLDER.get(target);
        for (MobTrait other : set) {
            if (cap.hasTrait(other)) return true;
        }
        return false;
    }

}