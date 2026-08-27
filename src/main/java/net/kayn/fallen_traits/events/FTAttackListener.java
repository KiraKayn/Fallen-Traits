package net.kayn.fallen_traits.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import net.kayn.fallen_traits.init.FTItems;
import net.minecraft.world.entity.LivingEntity;

public class FTAttackListener implements AttackListener {

    @Override
    public void onCreateSource(CreateSourceEvent event) {
        LivingEntity mob = event.getAttacker();
        DamageTypeWrapper type = event.getResult();
        if (type == null) return;
        DamageTypeWrapper root = type.toRoot();
        if (root == L2DamageTypes.MOB_ATTACK || root == L2DamageTypes.PLAYER_ATTACK) {
            if (CurioCompat.hasItemInCurioOrSlot(mob, FTItems.HAND_OF_CREATION.get())) {
                event.enable(DefaultDamageState.BYPASS_MAGIC);
            }
        }
    }

}