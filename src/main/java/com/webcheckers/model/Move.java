package com.webcheckers.model;
/**
 * class to handle Moves, uses Positions.
 * @author Priya, Dante Secada-Oz
 *
 */
public class Move {

	/** Move type */
	public enum MOVETYPE {SINGLE,
							TAKE,
							WRONGDIRECTION,
							INVALIDCHAIN,
							INVALIDEND,
							NOPIECE,
							SELFTAKE,
							SINGLEFIRST,
							BETTERMOVE,
							INVALIDSTARTCOORD,
							INVALIDENDCOORD,
							ENDOCCUPIED,
							KINGINGENDSTURN,
							PARTIALMULTI,
							VALID}
	// Wrong Direction
	// Invalid Chain
	// Invalid Co-ordinates
	// No Piece to Take
	// Single On First Turn
	// Must take opponent piece

	private MOVETYPE type;

	/** Source Position of Move */
	private Position start;
	/** Destination Position of Move */
	private Position end;
	/** Kinged */
	private boolean pieceWasKinged;

	/** Piece Moved */
	private Piece pieceMoved;
	/** Piece Taken */
	private Piece pieceTaken;
	/** Position Taken */
	private Position positionTaken;

	/** Constructor for single
	 *
	 * @param start start position
	 * @param end end position
	 * @param pieceMoved pieceMoved as part of move
	 * */
	public Move(Position start, Position end, Piece pieceMoved, MOVETYPE type) {
		this.start = start;
		this.end = end;
		this.pieceMoved = pieceMoved;
		this.type = type;
		if (end.getRow() == 0 && pieceMoved.getType().equals(Piece.TYPE.SINGLE)) {
			this.pieceWasKinged = true;
		}
	}

	/** Constructor for Take
	 *
	 * @param start start position
	 * @param end end position
	 * @param pieceMoved piece moved as part of move
	 * @param pieceTaken piece taken as part of move
	 * */
	public Move(Position start, Position end, Piece pieceMoved, Piece pieceTaken) {
		this.start = start;
		this.end = end;
		this.pieceMoved = pieceMoved;
		this.pieceTaken = pieceTaken;
		this.positionTaken = new Position((start.getRow() + end.getRow())/2,
											(start.getCell() + end.getCell())/2);
		this.type = MOVETYPE.TAKE;
		if (end.getRow() == 0 && pieceMoved.getType().equals(Piece.TYPE.SINGLE)) {
			this.pieceWasKinged = true;
		}
	}

	/** Origin of move
	 *
	 * @return origin position of move
	 * */
	public Position getStart() {
		return this.start;
	}

	/** Destination of move
	 *
	 * @return destination position of move
	 * */
	public Position getEnd() {
		return this.end;
	}

	/** Position of piece taken in move
	 *
	 * @return piece used in move
	 * */
	public Position getPositionTaken() {
		return this.positionTaken;
	}

	/** Piece moved
	 *
	 * @return  Piece used in move
	 * */
	public Piece getPieceMoved() {
		return this.pieceMoved;
	}

	/** Piece taken in move
	 *
	 * @return  Piece used in move
	 * */
	public Piece getPieceTaken() {
		return this.pieceTaken;
	}

	/** Move type
	 *
	 * @return move type
	 * */
	public MOVETYPE getType() { return this.type; }

	/** Was piece moved kinged as a result of end position
	 *
	 * @return was kinged
	 * */
	public boolean gotKinged() { return this.pieceWasKinged; }

}



