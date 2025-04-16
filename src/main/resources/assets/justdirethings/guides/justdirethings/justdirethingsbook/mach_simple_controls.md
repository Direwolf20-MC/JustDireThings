---
navigation:
  title: "Simple Machines"
  icon: "justdirethings:blockbreakert1"
  position: 1
  parent: justdirethings:machines.md
---

# Simple Machines

Simple machines typically have the following controls.

**Redstone Button**

The redstone button controls when the machine runs. By default redstone is ignored, meaning the machine always runs.

Low means the machine will only run when NOT receiving a redstone signal.

High means the machine will only run when it is receiving a redstone signal

Finally, pulse will make the machine do a single operation per redstone pulse. For simple machines, this usually means doing 1 thing, like breaking 1 block. For advanced machines, it may mean breaking all the blocks in its area.

**Speed(ticks) Button**

This is how often the machine runs while active. When set to the default (20) it will operate once every second (or 20 ticks).

It can be reduced as far as 1, meaning it will operate every tick (or 20 times per second), but avoid doing this unless absolutely necessary, as this could contribute to lag.

