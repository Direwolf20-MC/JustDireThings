package com.direwolf20.justdirethings.util;

import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MiscTools {
    //Thanks Soaryn!
    @NotNull
    public static BlockHitResult getHitResult(Player player) {
        var playerLook = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        var lookVec = player.getViewVector(1.0F);
        var reach = player.blockInteractionRange();
        var endLook = playerLook.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
        BlockHitResult hitResult = player.level().clip(new ClipContext(playerLook, endLook, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        return hitResult;
    }

    public static boolean inBounds(int x, int y, int w, int h, double ox, double oy) {
        return ox >= x && ox <= x + w && oy >= y && oy <= y + h;
    }

    public static Vector3f findOffset(Direction direction, int slot, Vector3f[] offsets) {
        Vector3f offsetVector = new Vector3f(offsets[slot]);
        switch (direction) {
            case UP -> {
                Quaternionf quaternionf = Axis.XP.rotationDegrees(-270);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.XP.rotationDegrees(-270));
                offsetVector.add(0, 1, 0);
            }
            case DOWN -> {
                Quaternionf quaternionf = Axis.XP.rotationDegrees(-90);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.XP.rotationDegrees(-90));
                offsetVector.add(0, 0, 1);
                //reverse = false;
            }
            //case NORTH -> offsetVector;
            case EAST -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-90);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.YP.rotationDegrees(-90));
                offsetVector.add(1, 0, 0);
            }
            case SOUTH -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-180);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.YP.rotationDegrees(-180));
                offsetVector.add(1, 0, 1);
            }
            case WEST -> {
                Quaternionf quaternionf = Axis.YP.rotationDegrees(-270);
                offsetVector = quaternionf.transform(offsetVector);
                //offsetVector.transform(Vector3f.YP.rotationDegrees(-270));
                offsetVector.add(0, 0, 1);
            }
        }
        return offsetVector;
    }

    public static ListTag stringListToNBT(List<String> list) {
        ListTag nbtList = new ListTag();
        for (String string : list) {
            CompoundTag tag = new CompoundTag();
            tag.putString("list", string);
            nbtList.add(tag);
        }
        return nbtList;
    }

    public static List<String> NBTToStringList(ListTag nbtList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            CompoundTag tag = nbtList.getCompound(i);
            list.add(tag.getString("list"));
        }
        return list;
    }

    public static MutableComponent tooltipMaker(String string, int color) {
        Style style = Style.EMPTY;
        style = style.withColor(color);
        MutableComponent current = Component.translatable(string);
        current.setStyle(style);
        return current;
    }
}
