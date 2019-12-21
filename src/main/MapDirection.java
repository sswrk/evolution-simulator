package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MapDirection {
	NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;
	
	public MapDirection next() {
		if (this == NORTH)
			return NORTHEAST;
		if (this == NORTHEAST)
			return EAST;
		if (this == EAST)
			return SOUTHEAST;
		if (this == SOUTHEAST)
			return SOUTH;
		if (this == SOUTH)
			return SOUTHWEST;
		if (this == SOUTHWEST)
			return WEST;
		if (this == WEST)
			return NORTHWEST;
		return NORTH;
	}


	public Vector2d toUnitVector() {
		if (this == NORTH)
			return new Vector2d(0, 1);
		if (this == NORTHEAST)
			return new Vector2d(1, 1);
		if (this == EAST)
			return new Vector2d(1, 0);
		if (this == SOUTHEAST)
			return new Vector2d(1, -1);
		if (this == SOUTH)
			return new Vector2d(0, -1);
		if (this == SOUTHWEST)
			return new Vector2d(-1, -1);
			
		return new Vector2d(-1, 1);
	}
	
	MapDirection random() {
		List<MapDirection> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		int SIZE = VALUES.size();
		Random RANDOM = new Random();
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}
