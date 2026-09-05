package net.kayn.fallen_traits.mixin.l2hostility;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MobTraitCap.class)
public abstract class MobTraitCapTitleMixin {

    @Inject(method = "getTitle", at = @At("HEAD"), cancellable = true, remap = false)
    private void fallen_traits$orderedTitle(boolean showLevel, boolean showTrait,
                                            CallbackInfoReturnable<List<Component>> cir) {
        MobTraitCap self = (MobTraitCap) (Object) this;

        List<Component> ans = new ArrayList<>();
        if (showLevel && self.lv > 0) {
            ans.add(dev.xkmc.l2hostility.init.data.LangData.LV.get(self.lv)
                    .withStyle(net.minecraft.network.chat.Style.EMPTY.withColor(
                            self.fullDrop ? dev.xkmc.l2hostility.init.data.LHConfig.CLIENT.overHeadLevelColorAbyss.get()
                                    : dev.xkmc.l2hostility.init.data.LHConfig.CLIENT.overHeadLevelColor.get())));
        }

        if (!showTrait) {
            cir.setReturnValue(ans);
            return;
        }

        List<MutableComponent> legendary = new ArrayList<>();
        List<MutableComponent> normal = new ArrayList<>();

        for (var entry : new ArrayList<>(self.traits.entrySet())) {
            MobTrait trait = entry.getKey();
            if (trait == null) continue;
            if (trait instanceof LegendaryTrait) {
                legendary.add(trait.getFullDesc(entry.getValue()));
            } else {
                normal.add(trait.getFullDesc(entry.getValue()));
            }
        }

        appendLines(ans, legendary);
        appendLines(ans, normal);

        cir.setReturnValue(ans);
    }

    private static void appendLines(List<Component> lines, List<MutableComponent> traits) {
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