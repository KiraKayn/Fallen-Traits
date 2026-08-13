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
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TraitCompatibility {

    private record Pending(MobTrait self, Supplier<? extends MobTrait> other, Predicate<LivingEntity> bypass) {}

    private static final List<Pending> PENDING = new ArrayList<>();
    private static Map<MobTrait, Set<MobTrait>> resolvedPairs;
    private static Map<Set<MobTrait>, Predicate<LivingEntity>> bypasses;

    public static void register(MobTrait self, Supplier<? extends MobTrait> other) {
        register(self, other, mob -> false);
    }

    public static void register(MobTrait self, Supplier<? extends MobTrait> other, Predicate<LivingEntity> bypass) {
        PENDING.add(new Pending(self, other, bypass));
    }

    private static void resolvePending() {
        if (resolvedPairs != null) return;
        resolvedPairs = new HashMap<>();
        bypasses = new HashMap<>();
        for (Pending p : PENDING) {
            MobTrait other = p.other().get();
            resolvedPairs.computeIfAbsent(p.self(), k -> new HashSet<>()).add(other);
            resolvedPairs.computeIfAbsent(other, k -> new HashSet<>()).add(p.self());
            bypasses.put(Set.of(p.self(), other), p.bypass());
        }
    }

    private static boolean bypassed(MobTrait a, MobTrait b, LivingEntity target) {
        Predicate<LivingEntity> p = bypasses.get(Set.of(a, b));
        return p != null && p.test(target);
    }

    public static boolean isIncompatible(MobTrait trait, LivingEntity target) {
        resolvePending();
        Set<MobTrait> set = resolvedPairs.get(trait);
        if (set == null || set.isEmpty()) return false;
        if (!MobTraitCap.HOLDER.isProper(target)) return false;
        MobTraitCap cap = MobTraitCap.HOLDER.get(target);
        for (MobTrait other : set) {
            if (cap.hasTrait(other) && !bypassed(trait, other, target)) return true;
        }
        return false;
    }

    public static void resolve(LivingEntity mob) {
        resolvePending();
        if (!MobTraitCap.HOLDER.isProper(mob)) return;
        MobTraitCap cap = MobTraitCap.HOLDER.get(mob);
        List<MobTrait> keys = new ArrayList<>(cap.traits.keySet());
        for (int i = 0; i < keys.size(); i++) {
            Set<MobTrait> incompatible = resolvedPairs.get(keys.get(i));
            if (incompatible == null) continue;
            for (int j = i + 1; j < keys.size(); j++) {
                MobTrait later = keys.get(j);
                if (cap.hasTrait(later) && incompatible.contains(later) && !bypassed(keys.get(i), later, mob)) {
                    cap.removeTrait(later);
                }
            }
        }
    }

}