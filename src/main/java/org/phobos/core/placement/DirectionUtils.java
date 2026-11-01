package org.phobos.core.placement;

import net.minecraft.util.math.Direction;

public final class DirectionUtils {
   public static Direction[] directionArray = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
   public static Direction[] directionArray2 = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
   public static Direction[] directionArray3 = new Direction[]{Direction.UP, Direction.DOWN};
   public static Direction[] directionArray4 = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

   public static Direction resolveDirection(Direction direction) {
      return switch (direction) {
         case NORTH -> Direction.WEST;
         case SOUTH -> Direction.EAST;
         case WEST -> Direction.SOUTH;
         case EAST -> Direction.NORTH;
         default -> null;
      };
   }

   public static Direction resolveDirection2(Direction direction) {
      return switch (direction) {
         case NORTH -> Direction.EAST;
         case SOUTH -> Direction.WEST;
         case WEST -> Direction.NORTH;
         case EAST -> Direction.SOUTH;
         default -> null;
      };
   }

   public DirectionUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
