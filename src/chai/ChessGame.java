package chai;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;

public class ChessGame {

	public Position position;
	public static String m5 = ("1k6/8/7R/7R/8/8/8/7K w - - 0 1");
	public static String m1 = ("1k6/7R/6R1/8/8/8/8/7K w - - 0 1");
	public static String start = ("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	public static String m6 = ("rnbqr1k1/ppp3p1/4pR1p/4p2Q/3P4/B1PB4/P1P3PP/R5K1 w - - 0 1");
	public static String m6_2 = ("r7/3bb1kp/q4p1N/1pnPp1np/2p4Q/2P5/1PB3P1/2B2RK1 w - - 0 1");

	public int rows = 8;
	public int columns = 8;

	public ChessGame() {
		position = new Position(m6_2);

	}

	public int getStone(int col, int row) {
		return position.getStone(Chess.coorToSqi(col, row));
	}
	
	public boolean squareOccupied(int sqi) {
		return position.getStone(sqi) != 0;
		
	}

	public boolean legalMove(short move) {
		
		for(short m: position.getAllMoves()) {
			if(m == move) return true;
		}
		System.out.println(java.util.Arrays.toString(position.getAllMoves()));
		System.out.println(move);
		return false;
	
	}

	// find a move from the list of legal moves from fromSqi to toSqi
	// return 0 if none available
	public short findMove(int fromSqi, int toSqi) {
		
		for(short move: position.getAllMoves()) {
			if(Move.getFromSqi(move) == fromSqi && 
					Move.getToSqi(move) == toSqi) return move;
		}
		return 0;
	}
	
	public void doMove(short move) {
		try {

			System.out.println("making move " + move);

			position.doMove(move);
			System.out.println(position + "\n");
		} catch (IllegalMoveException e) {
			System.out.println("illegal move!");
		}
	}
	
	// check for checkmate
	public boolean isMate() {
		return position.isMate();
	}
	
	// check for stalemate
	public boolean isStaleMate() {
		return position.isStaleMate();
	}

	public static void main(String[] args) {
		System.out.println();

		// Create a starting position using "Forsyth–Edwards Notation". (See
		// Wikipedia.)
		Position position = new Position(m6_2);

		System.out.println(position);

	}
	
	

}
