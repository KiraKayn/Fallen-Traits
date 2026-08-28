package net.kayn.fallen_traits.content.item.curio;

import dev.xkmc.l2complements.content.item.curios.CurioItem;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.kayn.fallen_traits.init.FTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class YggdrasilRoot extends CurioItem implements ICurioItem {

    public static final String ROOT = "YggdrasilRoot";
    public static final String KEY_ITEMS = "items";

    public YggdrasilRoot(Properties properties) {
        super(properties, 256);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();

        if (!CurioCompat.getItems(
                wearer,
                equipped -> equipped.getItem() == LHItems.RESTORATION.get()
        ).isEmpty()) return false;

        return CurioCompat.getItems(
                wearer,
                equipped -> equipped.getItem() instanceof YggdrasilRoot
        ).isEmpty();
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity le = slotContext.entity();
        if (le.level().isClientSide) return;
        if (!le.isAlive()) return;

        List<EntitySlotAccess> list = CurioCompat.getItemAccess(le);
        CompoundTag tag = stack.getTag();

        if (tag != null && tag.contains(ROOT)) {
            CompoundTag root = tag.getCompound(ROOT);
            ListTag items = root.getList(KEY_ITEMS, Tag.TAG_COMPOUND);

            for (int i = items.size() - 1; i >= 0; i--) {
                CompoundTag entry = items.getCompound(i);
                long time = entry.getLong("UnsealStartTime");
                int dur = entry.getInt("sealTime");
                int effectiveDur = (int) Math.ceil(dur / FTConfig.COMMON.yggdrasilRootUnsealSpeedMultiplier.get());

                if (le.level().getGameTime() >= time + (long) effectiveDur) {
                    ItemStack result = ItemStack.of(entry.getCompound("sealedItem"));
                    EntitySlotAccess slot = CurioCompat.decode(entry.getString("SealedSlotKey"), le);
                    boolean removed = false;

                    if (slot != null && slot.get().isEmpty()) {
                        slot.set(result);
                        removed = true;
                    } else if (le instanceof Player player) {
                        if (player.addItem(result)) {
                            removed = true;
                        }
                    }

                    if (removed) {
                        items.remove(i);
                    }
                }
            }

            if (items.isEmpty()) {
                tag.remove(ROOT);
            } else {
                root.put(KEY_ITEMS, items);
            }
            return;
        }

        if (stack.getDamageValue() + 1 < stack.getMaxDamage()) {
            ListTag items = new ListTag();
            int maxSlots = FTConfig.COMMON.yggdrasilRootMaxUnsealSlots.get();
            int picked = 0;

            for (EntitySlotAccess e : list) {
                if (picked >= maxSlots) break;
                if (stack.getDamageValue() + 1 >= stack.getMaxDamage()) break;
                if (!(e.get().getItem() instanceof SealedItem)) continue;

                ItemStack sealed = e.get();
                e.set(ItemStack.EMPTY);
                String id = e.getID();
                long time = le.level().getGameTime();
                stack.hurtAndBreak(1, le, (x) -> {});

                Tag data = sealed.getOrCreateTag().get("sealedItem");
                if (data != null) {
                    CompoundTag entry = new CompoundTag();
                    entry.putInt("sealTime", sealed.getOrCreateTag().getInt("sealTime"));
                    entry.put("sealedItem", data);
                    entry.putString("SealedSlotKey", id);
                    entry.putLong("UnsealStartTime", time);

                    items.add(entry);
                    picked++;
                }
            }

            if (!items.isEmpty()) {
                CompoundTag root = new CompoundTag();
                root.put(KEY_ITEMS, items);
                if (tag == null) {
                    stack.getOrCreateTag().put(ROOT, root);
                } else {
                    tag.put(ROOT, root);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                Component.literal(FTConfig.COMMON.yggdrasilRootMaxUnsealSlots.get() + "").withStyle(ChatFormatting.RED),
                Component.literal((int) Math.round(FTConfig.COMMON.yggdrasilRootUnsealSpeedMultiplier.get() * 100) + "%").withStyle(ChatFormatting.RED)
        ).withStyle(ChatFormatting.GOLD));

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(ROOT)) {
            CompoundTag root = tag.getCompound(ROOT);
            ListTag items = root.getList(KEY_ITEMS, Tag.TAG_COMPOUND);
            list.add(Component.translatable(getDescriptionId() + ".desc_unsealing",
                    Component.literal(items.size() + "").withStyle(ChatFormatting.GOLD)
            ).withStyle(ChatFormatting.RED));

            for (int i = 0; i < items.size(); i++) {
                CompoundTag entry = items.getCompound(i);
                ItemStack sealed = ItemStack.of(entry.getCompound("sealedItem"));
                list.add(Component.literal(" - ").append(sealed.getHoverName()).withStyle(ChatFormatting.RED));
            }
        }
    }
}