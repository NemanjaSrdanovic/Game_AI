package test.client.move;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import client.enumerations.EMovementType;
import client.enumerations.ETerrainType;
import client.game.Avatar;
import client.map.Coordinate;
import client.map.Field;
import client.move.AI;

class AITest {

	private static AI ai;
	private static Avatar avatar;
	private static AITestObjects testObjects;

	/**
	 * Executed once, before each tests in this class to prepare dependencies.
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	void setUpBeforeClass() throws Exception {

		testObjects = new AITestObjects();
		avatar = new Avatar();

	}

	/**
	 * Executed once, after each tests in this class use to clean up dependencies.
	 * 
	 * @throws Exception
	 */
	@AfterEach
	void tearDownAfterClass() throws Exception {

		ai = null;
		avatar = null;
		testObjects = null;
	}

	/**
	 * Test where the AI receives a map containing one correct move (from current
	 * position), and checks if the AI will always chose that move or if a game
	 * logic problem exists.
	 */
	@Test
	public void avatarOnCastleField_CalculatesMove_MovesRightToOnlyNeighbourField() {

		ai = new AI(testObjects.getOnlyOneNeighbourMap(), testObjects.getMyMapSide());
		avatar.setTreasureCollected(false);
		avatar.setCurrentPosition(testObjects.getOnlyOneNeighbourMap().getCastleField());
		Assertions.assertEquals(EMovementType.Right, ai.getNextMove(avatar));

	}

	/**
	 * Test where the AI receives a map containing four correct move options (from
	 * current position) but one field contains the treasure, and checks if the AI
	 * will always chose that move or if a game logic problem exists.
	 */
	@Test
	public void avatarSurroundedByUnvisitedGrass_TreasureSawLeft_MovesLeftToTreasureField() {

		ai = new AI(testObjects.getGameMap(), testObjects.getAllNeighboursGrassMap());
		avatar.setCurrentPosition(testObjects.getAvatarNextToTreasureField());
		avatar.setTreasureSaw(testObjects.getTreasureField());
		Assertions.assertEquals(EMovementType.Left, ai.getNextMove(avatar));

	}

	/**
	 * Test where the AI receives a map containing multiple correct move options
	 * (from current position) but one field a few moves away contains the enemy
	 * castle, and checks if the AI will always chose that move or if a game logic
	 * problem exists.
	 */
	@Test
	public void avatarStandingOnMountain_EnemyCastleSaw_MovesDownToGrassFieldBeforeCastleField() {

		ai = new AI(testObjects.getTwoFieldsFromEnemyCastleMap(), testObjects.getMyMapSide());
		avatar.setCurrentPosition(testObjects.getAvatarPositionWhenCastleSaw());
		avatar.setTreasureCollected(true);
		avatar.setEnemyCastleSaw(testObjects.getEnemyCastleField());

		Assertions.assertEquals(EMovementType.Down, ai.getNextMove(avatar));
	}

	/**
	 * Test if the AI will correctly calculate the possible enemyCastle positions
	 * and move towards them when the avatar has found the treasure and the game is
	 * over 10 rounds (enemyAvatar position saved)
	 */
	@Test
	public void avatarHasFoundTreasure_DeterminingPosibleCastlePositions_MovesTowardsOpponentsMapSide() {

		ai = new AI(testObjects.getGameMap(), testObjects.getAllNeighboursGrassMap());
		avatar.setTreasureCollected(true);
		Field enemyField = new Field(new Coordinate(12, 4), ETerrainType.Grass);
		avatar.setEnemyPosition(enemyField);
		avatar.setCurrentPosition(testObjects.getAvatarNextToTreasureField());

		Executable execution = () -> ai.getNextMove(avatar);

		Assertions.assertDoesNotThrow(execution);
	}

	/**
	 * Test the AI behaviour after all possible enemyCastle positions have been
	 * visited and no castle found. Will there be a game logic problem or will the
	 * AI as calculated search on the other unvisited grass fields on the enemy game
	 * map.
	 */
	@Test
	public void avatarSearchedAllPosibleCastlePositions_SearchNearestGrass_MovesTowardsGrass() {

		ai = new AI(testObjects.allPosibleEnemyCastlePositionsVisited(), testObjects.getMyMapSide());
		avatar.setTreasureCollected(true);
		avatar.setCurrentPosition(testObjects.getEnemyAvatarPositionOnTenthTurn());
		avatar.setEnemyPosition(testObjects.getEnemyAvatarPositionOnTenthTurn());
		Assertions.assertEquals(EMovementType.Up, ai.getNextMove(avatar));
	}

}
