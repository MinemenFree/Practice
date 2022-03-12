package rip.crystal.practice.api.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;

@UtilityClass
public class MaterialUtil {

    public Material getMaterial(String string) {
        try {
            return Material.getMaterial(Integer.parseInt(string));
        }
        catch (IllegalArgumentException ex) {
            return Material.getMaterial(string);
        }
    }
}