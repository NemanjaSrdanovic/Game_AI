package server.converter;

import MessagesBase.ETerrain;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.ETreasureState;
import server.enumerations.EAvatarPositionValue;
import server.enumerations.ECastleValue;
import server.enumerations.EPlayerStateValue;
import server.enumerations.ETerrainType;
import server.enumerations.ETreasureValue;
import server.exceptions.ConverterException;

public class EnumConverter {

	/**
	 * Instantiates a new enum-converter object.
	 * 
	 * @param Converter
	 */
	public EnumConverter() {

	}

	/**
	 * This method is used by the enum-converter to convert the client ECastleValue
	 * enumeration into a network protocols convenient which can be send to the
	 * server.
	 * 
	 * @param value ( ECastleValue from server.enumeration e.g. EnemyCastlePresent)
	 * @return (EFortState from the network protocol)
	 */
	public EFortState ECastleValue_To_NetworkFortState(ECastleValue value) {

		if (value == null)
			throw new ConverterException("Converter exception: The castle value can�t be null.");

		switch (value) {

		case EnemyCastlePresent:
			return EFortState.EnemyFortPresent;
		case MyCastlePresent:
			return EFortState.MyFortPresent;
		case NoOrUnknownCastleState:
			return EFortState.NoOrUnknownFortState;
		default:

			throw new ConverterException("Converter exception: Unknown castle value.");
		}
	}

	/**
	 * This method is used by the enum-converter to convert the network protocol
	 * EFortState enumeration into a server convenient, which can be used in the
	 * client class.
	 * 
	 * @param state (EFortState from the network protocol)
	 * @return ( ECastleValue from server.enumeration e.g. EnemyCastlePresent)
	 */
	public ECastleValue NetworkFortState_To_ECastleValue(EFortState state) {

		if (state == null)
			throw new ConverterException("Converter exception: The fort state can�t be null.");

		switch (state) {
		case EnemyFortPresent:
			return ECastleValue.EnemyCastlePresent;
		case MyFortPresent:
			return ECastleValue.MyCastlePresent;
		case NoOrUnknownFortState:
			return ECastleValue.NoOrUnknownCastleState;
		default:

			throw new ConverterException("Converter exception: Unknown fort state.");
		}
	}

	/**
	 * This method is used by the enum-converter to convert the client
	 * EPlayerStateValue enumeration into a network protocols convenient which can
	 * be send to the server.
	 * 
	 * @param value ( EPlayerStateValue from server.enumeration e.g. Lost, Won)
	 * @return (EPlayerGameState from the network protocol)
	 */
	public EPlayerGameState EPlayerStateValue_To_NetworkPlayerGameState(EPlayerStateValue value) {

		if (value == null)
			throw new ConverterException("Converter exception: The player game state value can�t be null.");

		switch (value) {

		case Lost:
			return EPlayerGameState.Lost;
		case ShouldActNext:
			return EPlayerGameState.ShouldActNext;
		case ShouldWait:
			return EPlayerGameState.ShouldWait;
		case Won:
			return EPlayerGameState.Won;
		default:

			throw new ConverterException("Converter exception: Unknown player game state value.");
		}

	}

	/**
	 * This method is used by the enum-converter to convert the network protocol
	 * EPlayerGameState enumeration into a server convenient, which can be used in
	 * the client class.
	 * 
	 * @param state (EPlayerGameState from the network protocol)
	 * @return ( EPlayerStateValue from server.enumeration e.g. Lost, Won)
	 */
	public EPlayerStateValue NetworkPlayerGameState_To_EPlayerStateValue(EPlayerGameState state) {

		if (state == null)
			throw new ConverterException("Converter exception: The player game state can�t be null.");

		switch (state) {

		case Lost:
			return EPlayerStateValue.Lost;
		case ShouldActNext:
			return EPlayerStateValue.ShouldActNext;
		case ShouldWait:
			return EPlayerStateValue.ShouldWait;
		case Won:
			return EPlayerStateValue.Won;
		default:

			throw new ConverterException("Converter exception: Unknown player game state.");
		}

	}

	/**
	 * This method is used by the enum-converter to convert the client
	 * EAvatarPositionValue enumeration into a network protocols convenient which
	 * can be send to the server.
	 * 
	 * @param value (EAvatarPositionValue from server.enumeration- MyAvatarPosition)
	 * @return (EPlayerPositionState from the network protocol)
	 */
	public synchronized EPlayerPositionState EAvatarPositionValue_To_NetworkPlayerPositionState(
			EAvatarPositionValue value) {

		if (value == null)
			throw new ConverterException("Converter exception: The avatar position value can�t be null.");

		switch (value) {

		case BothAvatarPosition:
			return EPlayerPositionState.BothPlayerPosition;
		case EnemyAvatarPosition:
			return EPlayerPositionState.EnemyPlayerPosition;
		case MyAvatarPosition:
			return EPlayerPositionState.MyPosition;
		case NoAvatarPresent:
			return EPlayerPositionState.NoPlayerPresent;
		default:

			throw new ConverterException("Converter exception: Unknown avatar position value.");
		}

	}

	/**
	 * This method is used by the enum-converter to convert the network protocol
	 * EPlayerPositionState enumeration into a server convenient, which can be used
	 * in the client class.
	 * 
	 * @param state (EPlayerPositionState from the network protocol)
	 * @return (EAvatarPositionValue from server.enumeration e.g. MyAvatarPosition)
	 */
	public EAvatarPositionValue NetworkPlayerPositionState_To_EAvatarPositionValue(EPlayerPositionState state) {

		if (state == null)
			throw new ConverterException("Converter exception: The player position state can�t be null.");

		switch (state) {

		case BothPlayerPosition:
			return EAvatarPositionValue.BothAvatarPosition;
		case EnemyPlayerPosition:
			return EAvatarPositionValue.EnemyAvatarPosition;
		case MyPosition:
			return EAvatarPositionValue.MyAvatarPosition;
		case NoPlayerPresent:
			return EAvatarPositionValue.NoAvatarPresent;
		default:

			throw new ConverterException("Converter exception: Unknown player position state.");
		}

	}

	/**
	 * This method is used by the enum-converter to convert the client ETerrainType
	 * enumeration into a network protocols convenient which can be send to the
	 * server.
	 * 
	 * @param type (ETerrainType from server.enumeration e.g. Grass)
	 * @return (ETerrain from the network protocol)
	 */
	public ETerrain ETerrainType_To_NetworkTerrain(ETerrainType type) {

		if (type == null)
			throw new ConverterException("Converter exception: The terrain type can�t be null.");

		switch (type) {

		case Grass:
			return ETerrain.Grass;
		case Mountain:
			return ETerrain.Mountain;
		case Water:
			return ETerrain.Water;
		default:

			throw new ConverterException("Converter exception: Unknown terrain type.");
		}

	}

	/**
	 * This method is used by the enum-converter to convert the network protocol
	 * ETerrain enumeration into a server convenient, which can be used in the
	 * client class.
	 * 
	 * @param type (ETerrain from the network protocol)
	 * @return (ETerrainType from server.enumeration e.g. Grass)
	 */
	public ETerrainType NetworkTerrain_To_ETerrainType(ETerrain type) {

		if (type == null)
			throw new ConverterException("Converter exception: The terrain type can�t be null.");

		switch (type) {

		case Grass:
			return ETerrainType.Grass;
		case Mountain:
			return ETerrainType.Mountain;
		case Water:
			return ETerrainType.Water;
		default:

			throw new ConverterException("Converter exception: Unknown terrain.");
		}

	}

	/**
	 * This method is used by the enum-converter to convert the client
	 * ETreasureValue enumeration into a network protocols convenient which can be
	 * send to the server.
	 * 
	 * @param value (ETreasureValue from server.enumeration e.g. MyTreasurePresent)
	 * @return (ETreasureState from the network protocol)
	 */
	public ETreasureState ETreasureValue_To_NetworkTreasureState(ETreasureValue value) {

		if (value == null)
			throw new ConverterException("Converter exception: The treasure value can�t be null.");

		switch (value) {

		case MyTreasurePresent:
			return ETreasureState.MyTreasureIsPresent;
		case NoOrUnknownTreasureValue:
			return ETreasureState.NoOrUnknownTreasureState;
		default:

			throw new ConverterException("Converter exception: Unknown treasure value.");
		}

	}

	/**
	 * This method is used by the enum-converter to convert the network protocol
	 * ETreasureState enumeration into a server convenient, which can be used in the
	 * client class.
	 * 
	 * @param value (ETreasureState from the network protocol)
	 * @return (ETreasureValue from server.enumeration e.g. MyTreasurePresent)
	 */
	public ETreasureValue NetworkTreasureState_To_ETreasureValue(ETreasureState value) {

		if (value == null)
			throw new ConverterException("Converter exception: The treasure state can�t be null.");

		switch (value) {

		case MyTreasureIsPresent:
			return ETreasureValue.MyTreasurePresent;
		case NoOrUnknownTreasureState:
			return ETreasureValue.NoOrUnknownTreasureValue;
		default:

			throw new ConverterException("Converter exception: Unknown treasure state.");
		}

	}
}
