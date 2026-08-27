package net.kayn.fallen_traits.content.item.curio;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.rtxyd.fallen.lib.runtime.forgemod.util.EntityCakyHandler;
import net.rtxyd.fallen.lib.util.IObjectCaky;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class RageGlove extends CurseCurioItem {

    public static final String GLOVE_STACK_KEY = "ft.glove_stack";

    public RageGlove(Properties props) {
        super(props);
    }

    @Override
    public int getExtraLevel() {
        return FTConfig.COMMON.furyExtraDifficulty.get();
    }

    @Override
    public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
        var event = cache.getLivingHurtEvent();
        if (event == null || event.getAmount() <= 0) return;
        Stack data = getStack(user);
        long time = user.level().getGameTime();
        int timeout = FTConfig.COMMON.furyStackTimeoutTicks.get();
        if (time - data.lastHit > timeout) {
            data.count = 0;
        }
        double inc = FTConfig.COMMON.furyDamageIncreasePerHit.get();
        double max = FTConfig.COMMON.furyMaxDamageMultiplier.get();
        double factor = Math.min(max, 1 + data.count * inc);
        cache.addHurtModifier(DamageModifier.multBase((float) factor));
        data.count++;
        data.lastHit = time;
    }

    private Stack getStack(LivingEntity user) {
        return EntityCakyHandler.resolveWith(user, GLOVE_STACK_KEY, IObjectCaky.Type.MANUAL, e -> new Stack(), e -> 0);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        return CurioCompat.getItems(
                wearer,
                equipped -> equipped.getItem() instanceof HandOfCreation
        ).isEmpty();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        int inc = (int) Math.round(FTConfig.COMMON.furyDamageIncreasePerHit.get() * 100);
        int max = (int) Math.round(FTConfig.COMMON.furyMaxDamageMultiplier.get() * 100);
        list.add(Component.translatable(getDescriptionId() + ".desc",
                Component.literal(inc + "%").withStyle(ChatFormatting.RED),
                Component.literal(max + "%").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GOLD));
    }

    public static class Stack {
        public int count = 0;
        public long lastHit = 0;
    }

}