# Amidst MODDED FOR INFDEV COMPAT

A version of amidst that was modified to show oceans in infdev. This is *incredibly* slow, as it's basically just generating the chunk and looking for blocks at y=63.

To run this, you need to create profiles with the relevant versions in the vanilla launcher, just like for any other version.

Don't expect much support for this and **do not** bother the developers of regular amidst with problems.

## Biomes

This program displays biomes based on the height of the highest non-water block.

Min Y | Max Y | Biome
--- | --- | ---
0 | 54 | Deep Ocean
55 | 62 | Ocean
63 | 67 | Beach
68 | 79 | Plains
80 | 89 | Extreme Hills Edge
90 | 99 | Extreme Hills
100 | 126 | Extreme Hills M
127 | 127 | Extreme Hills Plus


## Versions

Version        | Status | Notes
:------------- | :--    | :--
inf-20100327   | ✅     | blocks is now `e`; callback is now `a`
inf-20100330-1 | -      |
inf-20100330-2 | ✅     | blocks is now `f`
inf-201004xx   | -      |
inf-20100413   | ✅     | 
inf-20100414   | ✅     | 
inf-20100415   | ✅     | 
inf-20100420   | ✅     | 
inf-20100607   | ✅     | 
inf-20100608   | ✅     | 
inf-20100611   | ✅     | blocks is now `g`
inf-20100615   | ✅     | 
inf-20100616   | -      |
inf-20100617-1 | ✅     | blocks is now `h`; works by adding objenesis; seed is now `l`
inf-20100617-2 | ✅     |
inf-20100618   | ✅     | seed is now `m`; 
inf-20100624   | ✅     | blocks is now `b`; needed new classtranslator code
inf-20100625-1 | ✅     |
inf-20100625-2 | ✅     |
inf-20100627   | ✅     |
inf-20100629   | ✅     |
inf-20100630-1 | ✅     | seed is now `n`; callback is now `a`
inf-20100630-2 | ✅     |

## What is my internet connection used for?

* Amidst **will** use web services provided by Mojang, e.g. to
  * display information about Minecraft versions.
  * display information about players like the name or the skin.
* Amidst **will** check for updates on every start.
* Amidst **will not** track you with Google Analytics, which was the case in older versions.

## Legal Information

* Amidst is **not** owned by or related to Mojang in any way.
* Amidst comes with **absolutely no warranty**.
* Amidst is free and open source software, licensed under the GPLv3.
