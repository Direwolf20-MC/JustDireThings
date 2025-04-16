---
navigation:
  title: "Basic Fluid Drop Recipe Automation"
  icon: "justdirethings:polymorphic_fluid_bucket"
  position: 1
  parent: justdirethings:misc.md
---

# Basic Fluid Drop Recipe Automation

A simple setup to semi-autocraft stuff from Fluid Drop Recipes using only simple machines

<GameScene zoom="4" interactive={true}>
  <ImportStructure src="./gamescenes/fluidrecipe.nbt" />

  <BoxAnnotation color="#FFFFFF" min="1 1 0" max="2 2 1">
        Allow list with your output fluid
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="1 1 2" max="2 2 3">
        Allow list with your input fluid
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="1 2 2" max="2 3 3">
       Redstone mode set to Pulse + input Items <ItemImage id="justdirethings:polymorphic_catalyst"/>
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="0 1 1" max="1 2 2">
        Redstone mode set to High
  </BoxAnnotation>

  <BoxAnnotation color="#FFFFFF" min="2 1 1" max="3 2 2">
        Input Fluid <ItemImage id="minecraft:water_bucket"/>
  </BoxAnnotation>


</GameScene>

## How to use

- Place every block on right way like the example above

- Insert inside the <ItemLink id="justdirethings:droppert1"/> your input item

- Insert inside the <ItemLink id="justdirethings:fluidplacert1"/> your input fluid

After insered any fluid it will start converting all of your fluid