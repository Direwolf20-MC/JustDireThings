---
navigation:
  title: "Experience Holder"
  icon: "justdirethings:experienceholder"
  position: 3
  parent: justdirethings:machines.md
item_ids:
  - justdirethings:experienceholder
---

# Experience Holder

The experience holder does what it says on the tin. It holds experience!

In the UI, you'll see the current levels stored (represented by the green number), and the partial levels stored (represented by the green exp bar). Clicking the + or - buttons will transfer 1 level from you (the player) to the block.

Hold Shift while clicking these buttons to transfer 10 levels at a time, and hold Ctrl while clicking to transfer ALL your levels at once!

You'll notice this has an Area of Effect on it, which serves two purposes. First of which is automating nearby player's experience levels.

Using the Number Button to the left of the - button, set the desired amount of experience a player inside the area of affect should have. Then, use the redstone control button to determine when this occurs, either on pulse (by default) or always. The machine will use the Speed (ticks) to attempt to modify nearby player(s) levels every so often.

The machine will add or remove levels to the player as needed to get that player's level at the target -- assuming of course theres enough exp stored in the machine to raise them that high!

Toggle the 'owner only' button to ensure this only gives experience to the player who originally placed the block.

Use this to automatically store your experience when you return to your base by setting it to 0, or keep your experience always at level 30 while enchanting at an enchanting table! Every time you enchant something, this machine can automatically top you off back to exactly level 30!

In addition, there is a 'Collect Experience' button, which will attempt to find experience orbs inside the area of effect.  When this is enabled, it will absorb any exp orbs nearby, and store them.  This feature ignores the redstone state of the block, and either runs or not based on this buttons setting.

The block is a fluid tank as well, and can transfer exp points into XP Fluid at a rate of 20mb per 1 experience point.  You can interact with this block using either a bucket or the [Fluid Canister](./item_fluid_canister.md).

Its also possible to extract or insert the fluid using pipes or Laserio, etc.

The block will retain its experience when broken, so no worries if you need to relocate it!

## Experience Holder



<Recipe id="justdirethings:experienceholder" />

