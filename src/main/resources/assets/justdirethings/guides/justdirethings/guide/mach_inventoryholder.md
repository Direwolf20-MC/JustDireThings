---
navigation:
  title: "Inventory Holder"
  icon: "justdirethings:inventory_holder"
  position: 2
  parent: justdirethings:machines.md
item_ids:
  - justdirethings:inventory_holder
---

# Inventory Holder

The Inventory Holder is essential a super advanced Armor Stand!

When placed in the world, it acts sort of like a chest, simply right click to access the inventory.  You'll notice it has a UI matching your player's inventory - with slots for the Hotbar, items, Armor, and even your offhand!

Initially you can insert whatever you like into the inventory. Once an item is placed into the inventory, you can control-click on it to 'save' that item, including its stack size.

By doing this, you've now applied a filter to that slot, ensuring that only items of that type can fit there. If you want this filter to apply NBT filtering, toggle Compare NBT with the button on the top right.

When you shift click into the inventory, the following logic will apply in this order:


- Attempt to find a matching filter, and insert there.
- Attempt to insert into the same slot in the inventory as it was in the players (For example, top right corner to top right corner).
- Finally, insert as normal (starting with top left corner).

If you toggle on 'Filtered Items Only' via the button on the top right, items will not be allowed to be inserted into 'unfiltered' slots.  This means that if you attempt to put an item into a slot without a filter, it won't be allowed.

If you toggle on 'Compare Stack Sizes', the filtered slots will only accept up to their saved stack size.

On the top left, there are buttons to control how pipes, hoppers, etc can interact with this machine.

When Filtered Items Only is enabled, it will only allow you to pipe into slots with a matching filter, and 'Compare Stack sizes' works as above for insert.

When extracting, slots with a filter applied will be ignored, and Compare Stack Sizes determines if extracting will try to match the filter's stack size.

The 'Show Fake Player' button on the top left will toggle the rendered player above the block.

Ctrl+Shift Clicking one of the hotbar slots in the UI will place a red border around that slot. This determines which inventory slot is rendered in the fake player's 'main hand'.  This is purely cosmetic.

Towards the bottom of the UI is a Pull Items and Push Items button.  Push Items will attempt to insert all the items in your inventory into the block, and match the filter slots you've defined.

Note: The 'filtered items only' and 'compare stack sizes' buttons on the top right of the UI are respected when you push.

Pull Inventory will pull items out of the block, and place them in your inventory.

The swap inventory button attempts to swap each inventory slot in your player's inventory with the matching inventory slots in the block's inventory.

It is recommended to do this with minimal filtering, otherwise items won't be able to switch over, however you are welcome to configure this in any way you like, feel free to experiment with the different options!

## Inventory Holder



<Recipe id="justdirethings:inventory_holder" />

