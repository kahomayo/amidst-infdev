#!/usr/bin/env python3

import sys

def biome(f1, f2):
    f2 *= f1
    if f1 < 0.1:
        return '\033[31mt'
    if f2 < 0.2:
        if f1 < 0.5:
            return '\033[31mt'
        if f1 < 0.95:
            return '\033[32ms'
        return 'd'
    else:
        if f2 > 0.5 and f1 < 0.7:
            return '\033[33mS'
        if f1 < 0.5:
            return '\033[34mT'
        if f1 < 0.97:
            if f2 < 0.35:
                return '\033[35mx'
            return '\033[96mf'
        else:
            if f2 < 0.45:
                return '\033[36mp'
            if f2 < 0.9:
                return '\033[91mF'
            return '\033[92mR'

width = int(sys.argv[1])

for temperature in range(0, width + 1):
    for rainfall in range(0, width + 1):
        print(biome(temperature / width, rainfall / width),  end=' ')
    print(end='\033[0m\n')

print('====')
print(biome(0.0, 0.0))
print(biome(0.4, 1.0))
print(biome(0.6, 0.0))
print(biome(0.6, 0.5))
print(biome(0.6, 1.0))
print(biome(0.7, 1.0))
print(biome(1.0, 0.0))
print(biome(1.0, 0.3))
print(biome(1.0, 0.6))
print(biome(1.0, 1.0))