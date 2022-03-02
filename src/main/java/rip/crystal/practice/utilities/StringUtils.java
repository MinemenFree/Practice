package rip.crystal.practice.utilities;

import org.bukkit.ChatColor;

import java.util.Collections;

public class StringUtils {

    public static String getStringPoint(int points, ChatColor color, int pointsToWin){
        String x = "⬤";
        switch (points){
            case 1: return color + "⬤" + "&f" + String.join("", Collections.nCopies(pointsToWin - points, x));
            case 2: return color + "⬤⬤" + "&f" + String.join("", Collections.nCopies(pointsToWin - points, x));
            case 3: return color + "⬤⬤⬤" + "&f" + String.join("", Collections.nCopies(pointsToWin - points, x));
            case 4: return color + "⬤⬤⬤⬤" + "&f"+ String.join("", Collections.nCopies(pointsToWin - points, x));
            case 5: return color + "⬤⬤⬤⬤⬤";
            default: return String.join("", Collections.nCopies(pointsToWin - points, x));
        }
    }

//    public static String getStringPoint(int points, ChatColor color){
//        switch (points){
//            case 1: return color + "✘" + "&f✘✘✘";
//            case 2: return color + "✘✘" + "&f✘✘✘";
//            case 3: return color + "✘✘✘" + "&f✘✘";
//            case 4: return color + "✘✘✘✘" + "&f✘";
//            case 5: return color + "✘✘✘✘✘";
//            case 6: return color + "✘✘✘✘✘✘";
//            case 7: return color + "✘✘✘✘✘✘✘";
//            case 8: return color + "✘✘✘✘✘✘✘✘";
//            case 9: return color + "✘✘✘✘✘✘✘✘✘";
//            case 10: return color + "✘✘✘✘✘✘✘✘✘";
//            default: return "&f✘✘✘✘✘";
//        }
//    }

}