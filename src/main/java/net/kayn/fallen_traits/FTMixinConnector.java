package net.kayn.fallen_traits;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class FTMixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("fallen_traits.mixins.json");

        boolean isCataclysmLoaded = getClass().getClassLoader().getResource(
                "com/github/L_Ender/cataclysm/Cataclysm.class") != null;

        boolean isSoulsWeaponryLoaded = getClass().getClassLoader().getResource(
                "net/soulsweaponry/SoulsWeaponry.class") != null;
        if (isCataclysmLoaded) {
            Mixins.addConfiguration("fallen_traits.cataclysm.mixins.json");
        }
        if (isSoulsWeaponryLoaded) {
            Mixins.addConfiguration("fallen_traits.soulsweaponry.mixins.json");
        }
    }
}