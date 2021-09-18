package server.rules;

/**
 * All constant values needed for game creation and execution.
 * 
 * @author Nemanja Srdanovic
 *
 */
public class GameConstants {

	public static final int DEFAULT_PORT = 18235;

	public static final int MAX_PLAYER_NUM_PER_GAME = 2;
	public static final int NUM_OF_CASTLE_PER_HALF_MAP = 1;
	public static final int ROUND_AFTER_WHICH_REAL_POSITION_VISIBLE = 10;
	public static final int MAX_PARALLEL_GAME_NUM = 999;

	public static final int NUM_OF_HALF_MAP_FIELDS = 32;
	public static final int HALF_MAP_HEIGHT = 4;
	public static final int HALF_MAP_WIDTH = 8;

	public static final int MIN_MAP_WATER_FIELDS = 4;
	public static final int MIN_MAP_MOUNTAIN_FIELDS = 3;
	public static final int MIN_MAP_GRASS_FIELDS = 15;

	public static final int MAX_MAP_WATER_FIELDS_ON_HORIZONTAL_EGE = 3;
	public static final int MAX_MAP_WATER_FIELDS_ON_VERTICAL_EGE = 1;

	public static final int SQUARE_MAP_WIDTH_EXTEND = 0;
	public static final int SQUARE_MAP_HEIGHT_EXTEND = 4;

	public static final int RECTANGLE_MAP_WIDTH_EXTEND = 8;
	public static final int RECTANGLE_MAP_HEIGHT_EXTEND = 0;

	public static final int MAX_HALF_MAP_HEIGHT_FIELD = 3;
	public static final int MAX_HALF_MAP_WIDTH_FIELD = 7;

	public static final int MAX_MAP_HEIGHT_FIELD = 7;
	public static final int MAX_MAP_WIDTH_FIELD = 15;
	public static final int MIN_MAP_HEIGHT_FIELD = 0;
	public static final int MIN_MAP_WIDTH_FIELD = 0;

	public static final int HALF_MAPS_NUM_TO_COMPLETE_MAP = 2;
	public static final int TIME_MILLIS_TO_CHECK_FOR_EXPIRED_GAMES = 600000;
	public static final int TIME_MILLIS_AFTER_WHICH_GAME_EXPIRES = 600000;
}
