# Effective - Changelog:

## Effective 1.4.1 - 1.18.2
- Added glowing plankton waterfall clouds in warm oceans at night
- Fixed splash rims looking incorrectly with Sodium
- Now includes Satin API
- Hypnotizing glow squids
    - Glow squids can now hypnotize you, displaying a hypnotizing shader that gradually gets stronger the longer you look at them
    - Glow squids will attract your cursor if they appear on your screen, this attraction getting stronger with the shader
    - Both cursor attraction and hypnotizing can be disabled with two new configuration options
- Glow squids named "jeb_" now glow rainbow and have a rainbow hypnotizing pattern
  - Incompatible with Iris shaders (the rainbow glow squid can turn invisible)
- Splashes and ripples (from rain or splashes) in warm oceans at night will now glow from glowing plankton
    - Glowing plankton can be disabled with a new configuration option

## Effective 1.3 - 1.18.2
- Overhauled splashes:
    - Splashes will now be colored depending on the water they originate from
    - Reworked the splash texture to be more fitting with the vanilla Minecraft aesthetic
    - Added a config option to adjust the transparency of the splashes' white rim
- Added ripples on water when it's raining
    - Ripple density can be adjusted or disabled in the config
- Added a config option to control the density or remove flowing water splashing particles
- Added two new config options to adjust cascade sounds volume and distance
- Fixed splashes sometimes being completely black (thanks to lonefelidae16)
- Fixed occasional crashes that could happen randomly (thanks to lonefelidae16)
- Widened the logic to fix splashes sometimes not appearing as well as now taking into account waterlogged blocks (like kelp or seagrass) and not just water source blocks
- Fishing bobbers no longer produce splashes or droplet particles
- Changed config library from Cloth Config to MidnightLib

## Effective 1.2.1 - 1.18.2
- Updated to Minecraft 1.18.2

## Effective 1.2 - 1.18.1
Massive thanks to MoriyaShiine for most of these additions and changes!
- Lapis lazuli block updates (piston moved, placed, broken...) connected to a flowing water block now cause connected flowing water blocks to spawn waterfall particles
- Updating flowing water blocks now spawns waterfall particles
- Completely remade waterfall sounds to be more distinct from rain sound effects and improve background ambience in areas with cascades
- Added different configuration options and mod menu compatibility to easily access these options
- Increased cascade particle generation distance
- Massively improved performance and waterfall calculations
- Massively improved performance with dynamic audio mods such as Sound Physics Remastered
- Fixed concurrent modification crashes

## Effective 1.1.1 - 1.18.1
- Fixed the first wave of splash particles being inverted
- Made waterfall sound effects quieter yet still present far away from them
- Removed lava splashes for now, as they were causing more issues than adding to the ambience
- Removed cascades appearing underwater when no air is present

## Effective 1.1 - 1.18.1
- Added two settings to enable or disable splashes and cascade effects available in the config file of your Minecraft instance config folder, thanks to devpelux
- Implemented various optimizations and performance fixes, thanks to Sollace
- Fixed the cascade sounds playing too many times resulting in them being louder than they are supposed to be, thanks to Sollace
- Fixed Canvas and Sodium cascade incompatibilities, thanks to spiralhalo
- Added russian subtitles, thanks to Felix14-v2

### Effective 1.0 - 1.18
Initial release with water splashes, lava splashes and cascade effects.


see full changelog [here](https://github.com/Ladysnake/Effective/blob/main/CHANGELOG.md "Changelog")
