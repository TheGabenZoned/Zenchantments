# Zenchantments Reference

This document describes the enchantments currently registered by Zenchantments for Minecraft 26.2.

## Using Enchantments

Enchantments can be obtained through normal enchanting when their configured probability is greater than zero. They can also be applied directly with the plugin command when the sender has permission:

```text
/ench enchant <enchantment> [level]
```

The command applies the enchantment to the item in the user's main hand. Names are case-insensitive; use the internal names shown in the tables below (for example, `BlazesCurse`, `GoldRush`, or `SonicShock`).

The item categories in this guide are the categories accepted by the configuration file. `Mace` applies to the Mace. `Spear` includes wooden, stone, copper, golden, iron, diamond, and netherite spears. `All` includes every supported tool, weapon, armor piece, and utility item.

A configured probability of `0.0` disables random acquisition but does not prevent administrators from applying the enchantment manually. A probability of `-1.0` disables the enchantment completely. Maximum levels and supported categories can be changed per world in the generated world configuration file.

Enabled enchantments are also offered as librarian villager trades. When a librarian acquires trades, one random enabled Zenchantment not already offered is added as a single extra trade per villager. These trades use emeralds and a book, offer a random valid enchantment level, and do not replace the librarian's normal enchanted-book trades.

## Regular Enchantments

| Enchantment | Max level | Applies to | Effect |
|---|---:|---|---|
| Anthropomorphism | I | Pickaxe | Creates floating blocks that orbit the player while sneaking and can be launched at monsters. |
| Arborist | III | Hoe | Improves drops from leaves, including apples, sticks, saplings, and a chance of a golden apple. |
| Bind | I | All | Keeps the enchanted item in the player's inventory after death. |
| Blanket | III | Axe, Shovel, Sword, Pickaxe, Hoe | Extinguishes fire in an area when activated. |
| Brace | III | Spear | Deals bonus damage when the target is moving toward the spear wielder. Levels provide 20%, 35%, and 50% bonus damage. |
| Blaze's Curse (`BlazesCurse`) | I | Chestplate | Prevents damage from lava and fire but causes damage in water and rain. |
| Blizzard | III | Bow, Trident | Creates a freezing blizzard around the projectile impact point. |
| Bounce | V | Boots | Preserves momentum while moving across slime blocks. |
| Burrow | III | Mace | Burrows entities downward by up to three blocks when a Mace smash attack lands. |
| Burst | V | Bow | Rapidly fires a series of arrows. |
| Caffeine | III | Helmet | Delays phantom spawning for the wearer. |
| Chitin | IV | Elytra | Reduces damage taken by the Elytra wearer. |
| Combustion | IV | Chestplate | Sets attackers on fire when the wearer is attacked. |
| Conversion | I | Sword | Converts experience into health while sneaking and right-clicking. |
| Decapitation | IV | Sword | Increases the chance of obtaining an enemy head when it dies. |
| Fire | I | Pickaxe, Shovel, Axe | Drops the smelted form of a broken block when one exists. |
| Firestorm | III | Bow, Trident | Creates a firestorm around the projectile impact point. |
| Fireworks | IV | Bow, Trident | Causes fired projectiles to produce fireworks on impact. |
| Force | III | Sword | Pushes or pulls nearby mobs. Sneak-right-click switches between modes. |
| Fuse | I | Bow, Trident | Instantly ignites explosive entities and blocks. |
| Germination | III | Hoe | Uses bone meal from the inventory to grow nearby plants. |
| Glide | III | Leggings | Slows the player\'s descent while sneaking. |
| Gluttony | I | Helmet | Automatically consumes food for the player. |
| Gold Rush (`GoldRush`) | III | Shovel | Has a chance to drop gold nuggets when mining sand. |
| Grab | I | Pickaxe, Shovel, Axe | Pulls mined item and experience drops toward the player. |
| Groundbreaker | IV | Mace | Applies Slowness and Mining Fatigue to entities hit by a Mace smash attack. |
| Green Thumb (`GreenThumb`) | III | Leggings | Grows nearby foliage around the player. |
| Harvest | III | Hoe | Harvests fully grown crops in an area when activated. |
| Haste | IV | Pickaxe, Shovel, Axe | Grants a mining speed boost. |
| Helping Hand (`HelpingHand`) | I | Chestplate | Selects the appropriate tool from the hotbar for the block being targeted. |
| Hook | II | Spear | Pulls a struck target toward the attacker by 0.5 or 1 block. |
| Ice Aspect (`IceAspect`) | II | Sword, Trident | Temporarily freezes the target. |
| Jump | IV | Boots | Grants a jump boost. |
| Laser | III | Pickaxe, Axe | Fires a beam that breaks blocks and damages mobs. |
| Level | III | Bow, Sword, Pickaxe, Trident | Increases experience dropped by killed mobs and mined ores. |
| Long Cast (`LongCast`) | II | Rod | Launches fishing hooks farther. |
| Lumber | I | Axe | Breaks an entire connected tree at once. |
| Magnetism | I | Leggings | Attracts nearby item entities toward the player. |
| Master Key (`MasterKey`) | I | Chestplate | Opens iron doors when activated. |
| Meador | I | Boots | Grants speed and jump boosts. |
| Momentum | III | Spear | Successful charges build stacks that increase charge damage and speed. Maximum stacks are 2, 3, and 4 by level. Missing a charge or waiting too long clears the stacks. |
| Mow | I | Shears | Shears nearby sheep. |
| Mystery Fish (`MysteryFish`) | I | Rod | Catches unusual aquatic mobs such as squid and guardians. |
| Nether Step (`NetherStep`) | III | Boots | Allows slow and safe walking across lava. |
| Night Vision (`NightVision`) | I | Helmet | Grants night vision. |
| Persephone | III | Hoe | Plants seeds from the player\'s inventory around the player. |
| Phalanx | III | Spear | During a charge, nearby spear-wielding allied players and owned pets grant up to three formation bonuses of +5% damage and +5% knockback resistance each. |
| Pierce | I | Pickaxe | Provides several mining modes switched by sneaking and activating the enchantment. |
| Plough | III | Hoe | Tills nearby soil in an area. |
| Potion | III | Bow, Trident | Gives the shooter random positive potion effects when attacking. |
| Potion Resistance (`PotionResistance`) | IV | Helmet, Chestplate, Leggings, Boots | Reduces the effects of potions on the wearer. |
| Quick Shot (`QuickShot`) | I | Bow, Trident | Fires projectiles at full speed immediately. |
| Quake | Not configured | Sword-compatible main-hand items | Creates a damaging shockwave that throws nearby hostile mobs upward. This enchantment is registered in code but is not present in the bundled default configuration. |
| Rainbow | I | Shears | Drops random flower and wool colors when used. |
| Rainbow Slam (`RainbowSlam`) | IV | Sword | Performs a powerful area slam against nearby mobs. |
| Reaper | IV | Bow, Sword, Trident | Applies temporary wither and blindness to targets. |
| Reveal | IV | Pickaxe | Reveals nearby ores through stone. |
| Saturation | III | Leggings | Reduces hunger usage. |
| Seismic Slam | III | Mace | Creates a circular shockwave around the target that deals reduced damage and applies high knockback. The maximum radius is 12 blocks. |
| Short Cast (`ShortCast`) | II | Rod | Pulls fishing hooks back toward the player. |
| Shred | V | Pickaxe, Shovel | Breaks blocks in a radius around the original block. |
| Skewer | II | Spear | A jab continues through additional entities in a straight line. Level I hits up to 2 total entities; level II hits up to 3. |
| Siphon | IV | Sword, Bow, Trident | Drains health from attacked mobs and gives it to the player. |
| Sonic Shock (`SonicShock`) | III | Elytra | Damages mobs when flying past them at high speed. |
| Spectral | I | Shovel | Cycles a block through related block types. |
| Speed | IV | Boots | Grants a speed boost. |
| Spikes | III | Boots | Damages entities the player lands on. |
| Stationary | I | Bow, Sword, Trident | Prevents knockback caused by the player\'s attacks. |
| Stock | I | Chestplate | Refills the player\'s held item when its stack runs out. |
| Stream | I | Elytra | Creates a particle trail while the player is flying. |
| Switch | I | Pickaxe | Replaces a clicked block with the leftmost suitable block in the hotbar while sneaking. |
| Terraformer | I | Shovel | Places blocks from the inventory around the player. |
| Toxic | IV | Bow, Sword, Trident | Makes targets nauseous and unable to eat. |
| Tracer | IV | Bow, Trident | Guides projectiles toward targets. |
| Transformation | III | Sword | Occasionally transforms an attacked mob into a similar creature. |
| Trough | I | Chestplate, Leggings | Feeds nearby animals. |
| Variety | I | Axe, Hoe | Drops random wood or leaf types. |
| Vortex | I | Sword, Bow, Trident | Teleports mob drops and experience directly to the player. |
| Weight | IV | Boots | Slows the player while increasing strength and knockback resistance. |

## Admin Enchantments

These entries have probability `0.0` by default. They are intended for testing, special items, or administrator-controlled gameplay.

| Enchantment | Max level | Applies to | Effect |
|---|---:|---|---|
| Apocalypse | I | Bow, Trident | Unleashes a powerful destructive projectile effect. |
| Ethereal | I | All | Prevents the item from taking durability damage. |
| Missile | I | Bow | Fires a missile from the bow. |
| Singularity | I | Bow, Trident | Creates a black hole that attracts nearby entities and then discharges them. |
| Unrepairable | I | All | Prevents the item from being repaired. |

## Configuration Notes

The bundled defaults are in `source/resources/config.yml`. On a server, each world has its own generated configuration file in the plugin data directory. The important settings are:

| Setting | Meaning |
|---|---|
| `probability` | Chance of obtaining the enchantment through normal enchanting. `0.0` disables random acquisition. |
| `tools` | Item categories that can receive the enchantment. Multiple categories are comma-separated. |
| `max-level` | Highest level accepted by the enchantment. |
| `cooldown` | Delay between activations, measured in ticks. |
| `power` | Additional strength multiplier used by the enchantment implementation. |
| `max-enchants` | Maximum number of Zenchantments on one item. |
| `enchant-rarity` | Global multiplier for custom enchantment acquisition. |
| `description-lore` | Whether enchantment descriptions are shown in item lore. |

When adding a new enchantment to a server that already has world configuration files, run `/ench reload` after installing the updated plugin so the new configuration entry is available.
