package qa;

import com.direwolf20.justdirethings.common.network.handler.ToolSettingsGUIPacket;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.*;
import java.util.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class DireCases {
  public static final class RecordingPlayer extends FakePlayer {
    int opened;

    RecordingPlayer(ServerLevel l) {
      super(
          l,
          new GameProfile(
              UUID.fromString("00000000-0000-0000-0000-00000000d101"), "DireFixtureOnly"));
    }

    @Override
    public OptionalInt openMenu(MenuProvider p) {
      opened++;
      p.createMenu(9, getInventory(), this);
      return OptionalInt.of(9);
    }
  }

  public static void run(ServerLevel level) throws Exception {
    var player = new RecordingPlayer(level);
    IPayloadContext ctx =
        (IPayloadContext)
            Proxy.newProxyInstance(
                IPayloadContext.class.getClassLoader(),
                new Class[] {IPayloadContext.class},
                (p, m, a) -> {
                  if (m.getName().equals("player")) return player;
                  throw new UnsupportedOperationException(m.getName());
                });
    var method =
        ToolSettingsGUIPacket.class.getDeclaredMethod("lambda$handle$0", IPayloadContext.class);
    method.setAccessible(true);
    Checks.test(
        "dire_empty_hand_open_rejected",
        () -> {
          player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
          player.opened = 0;
          method.invoke(null, ctx);
          Checks.require(player.opened == 0, "Opened an empty-hand menu");
        });
    Checks.test(
        "dire_nonempty_hand_open_preserved",
        () -> {
          player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));
          player.opened = 0;
          method.invoke(null, ctx);
          Checks.require(player.opened == 1, "Valid menu was blocked");
        });
  }
}
