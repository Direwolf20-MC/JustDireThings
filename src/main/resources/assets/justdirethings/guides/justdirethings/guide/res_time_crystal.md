---
navigation:
  title: "Time Crystals"
  icon: "justdirethings:time_crystal"
  position: 20
  parent: justdirethings:resources.md
item_ids:
  - justdirethings:time_crystal_budding_block
  - justdirethings:time_crystal
  - justdirethings:time_crystal_block
  - justdirethings:time_crystal_cluster
  - justdirethings:time_crystal_cluster_large
  - justdirethings:time_crystal_cluster_medium
  - justdirethings:time_crystal_cluster_small
---

# Time Crystals

Time Crystals are no trivial task to create, but they are very worth while as they have the power to affect time itself!  Creating them is a multi step process that will be outlined in the following pages.

First, use <ItemLink id="justdirethings:gooblock_tier4"/> to convert a budding Amethyst block into a Budding Time Crystal block.

The Budding Time Crystal block must absorb time energy from each of Minecraft's 3 dimensions: The Overworld, The Nether, and The End.

<GameScene zoom="4">
    <Block x="3" id="justdirethings:time_crystal_budding_block" p:stage="0" />
    <Block x="2" id="justdirethings:time_crystal_budding_block" p:stage="1" />
    <Block x="1" id="justdirethings:time_crystal_budding_block" p:stage="2" />
    <Block x="0" id="justdirethings:time_crystal_budding_block" p:stage="3" />
    <Block x="2" y="-1" id="minecraft:grass_block" p:snowy="false"/>
    <Block x="1" y="-1" id="minecraft:netherrack"/>
    <Block x="0" y="-1" id="minecraft:end_stone" />
</GameScene>

Place the Budding Time Crystal block in The Overworld for some time. It will begin absorbing Overworld Time Energy, which you'll be able to see as blue particle effects. Once the block changes color, its ready for the next step.

<GameScene zoom="4">
    <Block id="justdirethings:time_crystal_budding_block" p:stage="1" />
</GameScene>

You'll need to move the block into The Nether at this point, however you cannot break the block without shattering it - Even with Silk touch! You'll need to find [Some Other Way](./mach_blockswappert1.md) to move it across dimensions.  Once in the nether, it will absorb Nether Time Energy, which will appear as orange particle effects. Once the block changes color, its ready to move again.

<GameScene zoom="4">
    <Block id="justdirethings:time_crystal_budding_block" p:stage="2" />
</GameScene>

Finally, move the block into The End. Here, it will absord End Time Energy, visualized as Green particle effects. Once complete, the block will turn green, and Time Crystal Buds will begin to appear.

<GameScene zoom="4">
    <Block id="justdirethings:time_crystal_budding_block" p:stage="3" />
</GameScene>

Each time a time crystal bud sprouts, it consumes some of the time energy. Ensure you don't break the time crystal buds until they've fully grown, or you will destroy them, receiving nothing in return!

Eventually, all of the time energy in the budding time crystal will be consumed.  At this point, you'll need to move it back to the Overworld and begin the charging process from the start.
Note: While out of charge, the buds will not grow any longer. In addition, if you move JUST the Budding Time Crystal, the buds themselves will shatter. Its recommended to move the Crystal and its buds [All Together](./mach_blockswappert2.md).

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="./gamescenes/time.nbt" />
</GameScene>


*Some people have reported unusual time distortions while carrying these around.  You should be cautious.*
