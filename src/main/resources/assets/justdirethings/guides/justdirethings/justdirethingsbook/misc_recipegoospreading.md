---
navigation:
  title: "Basic Goo Recipe Automation"
  icon: "justdirethings:gooblock_tier1"
  position: 1
  parent: justdirethings:misc.md
---

# Basic Goo Recipe Automation

Some simple setup to semi-autocraft stuff from Goo Spreading Recipes using only simple machines

## Goo Spreading Recipes with Blocks

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="./gamescenes/goorecipe.nbt" />

  <BoxAnnotation color="#FFFFFF" min="3 1 1" max="4 2 2">
        Allow list with Gooblock with blockstate filtered to alive=false
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="2 1 2" max="3 2 3">
      Redstone mode set to High + item to revive your goo <ItemImage id="minecraft:sugar"/>
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="1 1 2" max="2 2 3">
        Input Blocks <ItemImage id="minecraft:iron_block"/>
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="1 1 0" max="2 2 1">
       A generic Pickaxe <ItemImage id="minecraft:iron_pickaxe"/> + redstone mode set to High
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="0 1 1" max="1 2 2">
       Allow list with your block result
  </BoxAnnotation>

</GameScene>

## How to use

- Place every block on right way like the example above

- Insert inside the <ItemLink id="justdirethings:clickert1"/> your goo revive item

- Insert inside the <ItemLink id="justdirethings:blockbreakert1"/> a pickaxe

- Insert inside the <ItemLink id="justdirethings:blockplacert1"/> your input blocks

After insered any block item it will start converting all of your blocks




## Goo Spreading Recipes with Fluids

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="./gamescenes/goorecipe_fluid.nbt" />

  <BoxAnnotation color="#FFFFFF" min="3 1 1" max="4 2 2">
        Allow list with Gooblock with blockstate filtered to alive=false
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="2 1 2" max="3 2 3">
        Redstone mode set to High + item to revive your goo <ItemImage id="minecraft:nether_wart"/>
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="1 1 2" max="2 2 3">
        Fluid to convert <ItemImage id="justdirethings:unrefined_t2_fluid_bucket"/>
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="1 1 0" max="2 2 1">
       Redstone mode set to High
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="0 1 1" max="1 2 2">
       Allow list with your fluid result <ItemImage id="justdirethings:refined_t2_fluid_bucket"/>
  </BoxAnnotation>

</GameScene>

## How to use

- Place every block on right way like the example above

- Insert inside the <ItemLink id="justdirethings:clickert1"/> your goo revive item

- Insert inside the <ItemLink id="justdirethings:fluidplacert1"/> your input blocks

After insered any block item it will start converting all of your blocks