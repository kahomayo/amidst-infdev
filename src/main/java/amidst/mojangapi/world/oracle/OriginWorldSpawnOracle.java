package amidst.mojangapi.world.oracle;

import amidst.mojangapi.world.coordinates.CoordinatesInWorld;

public class OriginWorldSpawnOracle implements WorldSpawnOracle {
    @Override
    public CoordinatesInWorld get() {
        return CoordinatesInWorld.from(0, 0);
    }
}
