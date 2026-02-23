---
navigation:
  title: "Energy Transmitter"
  icon: "justdirethings:energytransmitter"
  position: 1
  parent: justdirethings:machines.md
item_ids:
  - justdirethings:energytransmitter
---

# Energy Transmitter

The energy transmitter is a revolutionary way to transmit energy from your generators to machines!

First, connect this machine to a power source, like the [Coal](./mach_generatort1.md) or [Fuel](./mach_generatorfluidt1.md) generators. This will cause the internal buffer to fill with Forge Energy.

Second, define the Area of Effect, like you can with most [Advanced Machines](./mach_advanced_controls.md).

Any blocks inside the area of effect that can receive Forge Energy will start getting filled up by the internal buffer.

NOTE: There is a small amount of energy loss as a result of this wireless energy transfer. This is intended, and is meant as a balance for 'wireless energy' transfer. This loss is configurable.

If you wish, you may filter the machines that receive power using the filter slots in the UI, same with most other [Advanced Machines](./mach_advanced_controls.md).

If another energy transmitter is inside the area of effect, they will keep each other balanced automatically. Energy balancing does not incur any energy loss. You can disable particle rendering with a button in the UI.

## Energy Transmitter



<Recipe id="justdirethings:energytransmitter" />

