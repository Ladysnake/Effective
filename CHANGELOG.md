# Effective - Changelog:

## Effective 2.0 - 1.19.2
- Rewrote the mod from scratch
- Merged some Illuminations features into Effective. Here's a list of all the Illuminations features that made it in:
  - Added and improved Fireflies
    - Fireflies now disappear faster and spawn more frequently, giving the effect a more consistent look
    - Fixed fireflies not being attracted to light
    - Fixed various bugs like fireflies stopping or teleporting
  - Added and improved Chorus Petals:
      - Chorus petals now stay around approximately 10x longer and fade into their blueish purple state more slowly
      - Chorus petals now lay flat when touching the ground or water
      - Chorus petals now cause ripples when landing on water
  - Added Eyes in the Dark (on Halloween)
  - Added and completely overhauled Will o' Wisps
  - Added Illuminations cosmetics (though they may get removed or reworked in the future)
    - Reworked the Will o' Wisp pet cosmetic to allow players to choose two custom colors to personalize their Will o' Wisp
    - As a result, Golden Will and Founding Skull cosmetics were removed as they can be replicated with custom colors
- Improved fireball visuals
- Improved dragon fireball and dragon breath visuals
- Added sculk dust particles
- Added spectral arrows trails and made them emissive
- Added air bubbles coming out of chests when opened underwater
- Improved cascades
  - Waterfall cloud particles now have an animated texture
  - Waterfall cloud size, cascade sound pitch, volume and distance are now dependent on the strength of the flowing water
  - Waterfalls with the strongest current (flowing directly from above) now generate a mist effect
  - Waterfall cloud density as well as mist density can be customized in the configuration options
- Improved Glow Squid particles
- Rewrote the configuration from scratch and added options to control whether the effect is enabled and / or its density for all effects
- Added sliders in the Mod Menu config for all numerical configuration options
- Merged both configuration options of Glow Squid hypnotizing into one
- Added a new secret config option for a new secret effect :)

## Effective 1.6 - 1.19.2
- Added a screen shake effect for:
  - Wardens roaring
  - Wardens using their sonic boom attack
  - Ravagers roaring after being stunned
  - The Ender Dragon's long roar when perched
- Added four new config options to disable each screen shake trigger individually
- Removed the need for waterfalls to be visible by the player in order to play sound
- Added Ukrainian translation, thanks to ttrafford7 and V972!
- Added Portuguese (Brazil) translation, thanks to FITFC!

## Effective 1.5 - 1.19.2
- Added Allay trails
  - Allay will now leave a colored trail and twinkles when flying around, inspired from the Minecraft Legends announce trailer
  - Allay trails can be disabled and trail twinkle density can be configured
- Added golden Allays
  - Allays now have a 50% chance of being a golden variant, also inspired from the Minecraft Legends announce trailer
  - Can be disabled in the configuration

## Effective 1.4.1 - 1.19.2
- Added glowing plankton waterfall clouds in warm oceans at night
- Fixed splash rims looking incorrectly with Sodium
- Waterfalls no longer play sound if the player cannot see them
- Now includes Satin API
- Migrated the mod to Quilt

## Effective 1.4 - 1.19.2
- Hypnotizing glow squids
  - Glow squids can now hypnotize you, displaying a hypnotizing shader that gradually gets stronger the longer you look at them
  - Glow squids will attract your cursor if they appear on your screen, this attraction getting stronger with the shader
  - Both cursor attraction and hypnotizing can be disabled with two new configuration options
- Glow squids named "jeb_" now glow rainbow and have a rainbow hypnotizing pattern
- Splashes and ripples (from rain or splashes) in warm oceans at night will now glow from glowing plankton
  - Glowing plankton can be disabled with a new configuration option

## Effective 1.3 - 1.19.2
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
- Updated to Minecraft 1.19.2

## Effective 1.2.2 - 1.19
- Updated to Minecraft 1.19

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
