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

Version            | Status | Notes
:----------------- | :--    | :--
inf-20100327       | ✅     | blocks is now `e`; callback is now `a`
inf-20100330-1     | -      |
inf-20100330-2     | ✅     | blocks is now `f`
inf-201004xx       | -      |
inf-20100413       | ✅     | 
inf-20100414       | ✅     | 
inf-20100415       | ✅     | 
inf-20100420       | ✅     | 
inf-20100607       | ✅     | 
inf-20100608       | ✅     | 
inf-20100611       | ✅     | blocks is now `g`
inf-20100615       | ✅     | 
inf-20100616       | -      |
inf-20100617-1     | ✅     | blocks is now `h`; works by adding objenesis; seed is now `l`
inf-20100617-2     | ✅     |
inf-20100618       | ✅     | seed is now `m`; 
inf-20100624       | ✅     | blocks is now `b`; needed new classtranslator code
inf-20100625-1     | ✅     |
inf-20100625-2     | ✅     |
inf-20100627       | ✅     |
inf-20100629       | ✅     |
inf-20100630-1     | ✅     | seed is now `n`; callback is now `a`
inf-20100630-2     | ✅     |
a1.0.0             | ✅     | two for the price of one ;)
a1.0.1             | -      |
a1.0.1_01          | ✅     |
a1.0.2             | -      |
a1.0.2_01          | ✅     |
a1.0.2_02          | ✅     |
a1.0.3             | ✅     |
a1.0.4             | ✅     |
a1.0.4-launcher    | ✅     |
a1.0.5-1           | -      |
a1.0.5-2           | ✅     |
a1.0.5_01          | ✅     |
a1.0.6             | ✅     |
a1.0.6_01          | ✅     |
a1.0.6_02          | -      |
a1.0.6_03          | ✅     |
a1.0.7             | ✅     |
a1.0.8             | -      |
a1.0.8_01          | ✅     |
a1.0.9             | ✅     |
a1.0.10            | ✅     |
a1.0.11            | ✅     |
a1.0.12            | ✅     |
a1.0.13            | ✅     |
a1.0.13_01-1       |  -     |
a1.0.13_01-2       | ✅     |
a1.0.13_01-3       | ✅     |
a1.0.14-1          | ✅     |
a1.0.14-2          | ✅     |
a1.0.14-2-launcher | ✅     |
a1.0.15            | ✅     |
a1.0.16            | ✅     |
a1.0.16_01         | ✅     |
a1.0.16_02         | ✅     |
a1.0.17            | -      |
a1.0.17_01         | -      |
a1.0.17_02         | ✅     |
a1.0.17_03         | ✅     |
a1.0.17_04         | ✅     |
a1.1.0-1           | ✅     |
a1.1.0-1-launcher  | ✅     |
a1.1.0-2           | ✅     |
a1.1.1             | -      |
a1.1.2             | ✅     |
a1.1.2_01          | ✅     |
a1.2.0             | ✅     | No oceans from here to b1.7.3
a1.2.0_01          | ✅     |
a1.2.0_02          | ✅     |
a1.2.1             | -      |
a1.2.1_01          | ✅     |
a1.2.2-1           | -      |
a1.2.2-2           | ✅     |
a1.2.2-3           | ✅     |
a1.2.3             | ✅     |
a1.2.3_01-1        | -      |
a1.2.3_01-2        | ✅     |
a1.2.3_02          | ✅     |
a1.2.3_03          | -      |
a1.2.3_04          | ✅     |
a1.2.3_05          | ✅     |
a1.2.4             | -      |
a1.2.4_01          | ✅     |
a1.2.5             | ✅     |
a1.2.6             | ✅     |
a1.2.6-2           | -      |
b1.0               | ✅     |
b1.0_01            | ✅     |
b1.0.2             | ✅     |
b1.1-1             | ✅     |
b1.1-2             | ✅     |
b1.1_01            | ✅     |
b1.1_02            | ✅     |
b1.2               | ✅     |
b1.2_01            | ✅     |
b1.2_02            | ✅     |
b1.3-221647        | -      |
b1.3-221713        | ✅     |
b1.3-221731        | -      |
b1.3-221750        | ✅     |
b1.3_01            | ✅     |
b1.4-1             | ✅     |
b1.4-2             | ✅     |
b1.4_01            | ✅     |
b1.5               | ✅     |
b1.5_01            | ✅     |
b1.6               | ✅     |
b1.6.1             | ✅     |
b1.6.2             | ✅     |
b1.6.3             | ✅     |
b1.6.4             | ✅     |
b1.6.5             | ✅     |
b1.6.6             | ✅     |
b1.7               | ✅     |
b1.7_01            | ✅     |
b1.7.2             | ✅     |
b1.7.3             | ✅     |

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
