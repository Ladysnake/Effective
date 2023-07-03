![Image](https://github.com/Ladysnake/Effective/blob/main/title.png?raw=true)


### A Quilt client-side mod improving Minecraft's ambience through particles, visual effects and sounds.

The mod adds various effects like water splashes, waterfall clouds, fireflies, improved visual effects, screen shake and many more.

Every effect is customisable and can be toggled in the configuration.

## [Video showcase (Effective 2.1 + in depth cascade system explanation)](https://www.youtube.com/watch?v=DNuaVti4tk0)

## Gallery
![Image](https://github.com/Ladysnake/Effective/blob/main/gallery/cascades.png?raw=true)

![Image](https://github.com/Ladysnake/Effective/blob/main/gallery/fireflies.png?raw=true)

![Image](https://github.com/Ladysnake/Effective/blob/main/gallery/chests.png?raw=true)

![Image](https://github.com/Ladysnake/Effective/blob/main/gallery/wisps.png?raw=true)

![Image](https://github.com/Ladysnake/Effective/blob/main/gallery/allays.png?raw=true)

![Image](https://github.com/Ladysnake/Effective/blob/main/gallery/sculk.png?raw=true)


## Current features:
### Water effects:
- Splashes, droplets and ripples when entities fall in water
- Waterfall clouds
    - Appears when water flows into two block deep water source blocks / a water source above a wool block (to make it silent)
    - Can also be used on beaches to simulate waves crashing
- Waterfall mist
    - Appears when water directly flows from above into source blocks
    - Can be used to create fog or mist ambiences
    - If a water source above a wool block is used instead of two water sources, the mist will be colored after the wool
- Flowing water droplets
- Rain ripples
- Glowing plankton
    - Makes splashes, droplets, ripples and waterfall clouds glow blue in warm oceans
- Chests open underwater emit bubbles
    - Chests with soul sand below them open randomly when underwater

### Entity effects:
- Glow squids hypnotize players
- Allay trails
- Golden Allays

### Screen shake effects for:
- Warden roars
- Warden sonic booms
- Ravager roars
- Ender dragon roars

### Illuminated effects:
- Fireflies in forests and humid biomes
    - Frequency depending on the biome's humidity, the higher the more appear
- Chorus petals around Chorus trees and when breaking Chorus flowers
    - Frequency depending on the Chorus flower's age
- Will o' Wisps in soul sand valleys
- Eyes in the dark that creep on you during Halloween

### Improved effects for:
- Fireballs
- Dragon fireballs and breath
- Spectral arrows
- Glow squid particles

### Miscellaneous effects:
- Sculk dust floating out of sculk blocks
- Cosmetics for supporters: [support the mod here!](https://ko-fi.com/s/2ef8e053cb)
    - [Support dashboard](https://doctor4t.ladysnake.org/login)
- Ultrakill parry effect when deflecting Ghast fireballs


## FAQ

#### Q: Can I use this mod on a vanilla server?

**A: Yes**. If you have installed this mod on your Minecraft client, you will  be able to connect to vanilla Minecraft servers and the ambient effects  will work as intended.

#### Q: Why Quilt? Why not Forge or Fabric?


**A: Short answer:** some utilities and libraries I need for my mods are only available for Quilt. I also do not wish to continue supporting loaders led by transphobes / racists / horrible people in general, especially when a better product like Quilt exists. If you want the long version of it, it's here.

<details>
<summary>Now if you want the long version...</summary>

I don't have any problem dissociating a project from the person behind it when that project is the best there is, however, Fabric's lead dev's transphobia genuinely got in the way of code contributions that would've been great additions to the loader's API (these contributions being made by trans people). Certain systems like the custom biome, dimension or multipart APIs of Fabric have a lot of problems and sometimes straight up don't work, while these issues have been fixed on Quilt.

For Forge, it's not so much about the devs being bad people, it's more that the way Forge does things and forces modders to do things in a particular (and in my opinion, complicated) way genuinely got me to quit modding before I switched to Fabric when it first came out. On top of that, Forge devs have in the past caused me and my friends quite a bit of trouble and unfortunate interactions, and when I did allow Forge ports of my mods to be made, I would have people come to me regarding issues on the Forge version, which I had nothing to do with.

Additionally, Quilt has a bigger team, and I personally know most of the devs. Therefore, I can also try to help by directly giving feedback or contributions to them if need be, unlike with Fabric or Forge. To also quote a friend I completely agree with: "The argument of "X modloader has no popular mods ergo no popular mods should move to it" is circular and, to put it bluntly, a tad idiotic. I've heard it with Fabric before, and as I did not listen previously I will not listen now. Quilt is a modloader that I can trust the development team of, and thus I develop on it."

If you're upset about the mod not being available to you because you don't wanna use Quilt, I can understand that. However, please also understand that if it weren't for Quilt, I probably wouldn't have updated my mod anyway, so you're technically not missing out on anything! Ultimately, whether or not you want to use this mod is entirely your choice, so do not play victim and put the blame on me if you do not wish to go through the steps to play with it.

Finally, I mod for myself above all, releasing my mods is just a bonus I provide for free. This is a hobby that should stay enjoyable, and altough I'm trying my best to keep it that way, it's becoming harder and harder as time passes due to entitled players and the incredibly toxic modding community. This is my work, my mod, and I am free to do whatever I want with it. If I don't want it to be available for loaders I dislike and don't want to support, I am free to make that choice without having to justify it. However, I still hope this explanation gives a little more insight and helps you understand!

🐀❤️

</details>

#### Q: But I really wanna play with this mod and I play on Fabric!

**A: You can run Fabric mods on Quilt**. 99% of Fabric mods run perfectly on Quilt, so if you wanna use this mod alongside Fabric ones, consider switching. It's not as big of a task to move as it seems. If you find an incompatibility from a Fabric mod on Quilt, [you can report it here](https://forum.quiltmc.org/t/mod-incompatibility-megathread/261) and the Quilt team will do its best.

#### Q: Can I port this mod to Forge / Fabric?

**A: No**. I am not comfortable with ports of my mod being made for other loaders I do not wish to support, for the reason listed above.

#### Q: Can I port this mod to Bedrock?

**A: Yes**. Unlike Fabric or Forge, if you wish to make a version of this mod for Minecraft Bedrock edition, I do not have an issue with it. However, please contact me first to get proper written permission.

#### Q: Can you backport this mod to 1.x? When will you update to 1.x?

**A: No**. I mod Minecraft as a hobby with very limited time. Therefore, I will not backport this mod to older versions of Minecraft, and I will update it to future versions when I feel like it. If you want it updated to newer versions, feel free to contribute to the mod and make a pull request, it's appreciated! Otherwise, don't come begging.

#### Q: Can I include this mod in a modpack?**

**A: Yes** you can. Go ahead, don't bother asking. Please however provide credit and a link to either the GitHub repository, Modrinth or CurseForge project page.

###### Copyright (C) 2023 Ladysnake
