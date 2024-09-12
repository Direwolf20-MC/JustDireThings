package com.direwolf20.justdirethings.util;

import net.minecraft.world.entity.player.Player;

public class ExperienceUtils {
    // Calculate experience required to go from level 0 to target level
    public static int getTotalExperienceForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }

    // Remove levels from a player, ensuring they don't lose more than available
    public static int removeLevels(Player player, int levelsToRemove) {
        int currentTotalExp = getPlayerTotalExperience(player);
        int targetLevel = Math.max(0, player.experienceLevel - levelsToRemove);

        // Calculate how much exp is required to be at the target level
        int targetTotalExp = getTotalExperienceForLevel(targetLevel);
        int expToRemove = currentTotalExp - targetTotalExp;

        player.giveExperienceLevels(-levelsToRemove);
        return expToRemove;  // Amount of exp removed
    }

    // Calculate experience required to go from one level to the next
    public static int getExperienceForNextLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }

    // Calculate total experience points player currently has (given level and progress)
    public static int getPlayerTotalExperience(Player player) {
        int exp = getTotalExperienceForLevel(player.experienceLevel);
        exp += Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
        return exp;
    }

    public static int getExpNeededForNextLevel(Player player) {
        return player.getXpNeededForNextLevel() - (int) (player.experienceProgress * player.getXpNeededForNextLevel());
    }

    // Remove points from a player, ensuring they don't lose more than available
    public static int removePoints(Player player, int pointsToRemove) {
        int currentTotalExp = getPlayerTotalExperience(player);
        int expToRemove = Math.min(currentTotalExp, pointsToRemove);
        player.giveExperiencePoints(-expToRemove);
        return expToRemove;  // Amount of exp removed
    }
}
