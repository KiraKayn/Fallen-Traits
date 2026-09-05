package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.compat.jade.TraitInfo;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

@Mixin(TraitInfo.class)
public abstract class TraitInfoMixin {

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true, remap = false)
    private void fallen_traits$orderedTraitInfo(ITooltip list, EntityAccessor entity,
                                                IPluginConfig config, CallbackInfo ci) {
        Entity raw = entity.getEntity();
        if (raw instanceof LivingEntity le) {
            if (MobTraitCap.HOLDER.isProper(le)) {
                MobTraitCap cap = MobTraitCap.HOLDER.get(le);

                List<Component> lines = new ArrayList<>(cap.getTitle(true, false));

                if (!cap.traits.isEmpty()) {
                    lines.add(Component.translatable("fallen_traits.jade.trait_count",
                            cap.traits.size()).withStyle(ChatFormatting.GRAY));

                    List<MutableComponent> legendary = new ArrayList<>();
                    List<MutableComponent> normal = new ArrayList<>();

                    for (var entry : new ArrayList<>(cap.traits.entrySet())) {
                        MobTrait trait = entry.getKey();
                        if (trait == null) continue;
                        if (trait instanceof LegendaryTrait) {
                            legendary.add(trait.getFullDesc(entry.getValue()));
                        } else {
                            normal.add(trait.getFullDesc(entry.getValue()));
                        }
                    }

                    if (!legendary.isEmpty()) {
                        lines.add(Component.translatable("fallen_traits.jade.legendary_traits",
                                legendary.size()).withStyle(ChatFormatting.GRAY));
                        appendTraitLines(lines, legendary);
                    }

                    if (!normal.isEmpty()) {
                        appendTraitLines(lines, normal);
                    }
                }

                list.addAll(lines);
                ci.cancel();
            }
        }
    }

    private static void appendTraitLines(List<Component> lines, List<MutableComponent> traits) {
        MutableComponent temp = null;
        int count = 0;
        for (MutableComponent comp : traits) {
            if (temp == null) {
                temp = comp;
                count = 1;
            } else {
                temp.append(Component.literal(" / ").withStyle(ChatFormatting.WHITE)).append(comp);
                count++;
                if (count >= 3) {
                    lines.add(temp);
                    count = 0;
                    temp = null;
                }
            }
        }
        if (count > 0) {
            lines.add(temp);
        }
    }
}