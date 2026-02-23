---
navigation:
  title: "Simple Sensor"
  icon: "justdirethings:sensort1"
  position: 9
  parent: justdirethings:machines.md
item_ids:
  - justdirethings:sensort1
---

# Simple Sensor

The Simple Sensor detects blocks or entities in its area of effect, and emits a redstone signal when its conditions are met. The slot in the UI is a single filter slot, allowing you to filter on a block or mob.

In its default mode, of 'block' with an empty filter, it'll emit a redstone signal if theres a block in front of it.

The **redstone** button lets you toggle between emitting a strong or weak redstone signal when conditions are met.

The **Allow List** button toggles between the filter slot being an allow list or deny list. Remember if you're targeting a block, to set this to allow. By default, in deny mode with an empty filter it will emit a signal on any block.

The **target** button lets you cycle between targeting Blocks, Air, Hostile, Passive, Adult, Child, Player, All Living, and Item entities. See the [Simple Clicker](./mach_clickert1.md) book entry for details on what each of these do.

Items mode will detect items sitting in the world in front of the block.

When targeting blocks, you can right click the filter slot to designate a specific block STATE to filter on!
For example, you can detect whether a door is opened or closed, or a level is on or off.

Simply right click on the filter slot when a block is in it, and you'll see a list of all blockstates on the left. Click on each one to cycle through the options.

## Simple Sensor



<Recipe id="justdirethings:sensort1" />

