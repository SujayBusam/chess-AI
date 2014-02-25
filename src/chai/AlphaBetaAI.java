package chai;

import java.util.Random;
import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;

public class AlphaBetaAI implements ChessAI {

	private int statesVisited = 0;
	private int depthReached = 0;

	// Constants
	private static int OFFSET = 1000;
	private static int MAX_UTILITY = Integer.MAX_VALUE - OFFSET;
	private static int MIN_UTILITY = Integer.MIN_VALUE + OFFSET;
	private static int MAX_DEPTH;
	private static int AI_PLAYER;

	// Inner class for a MAX or MIN node on the chess game tree
	private class MoveNode {

		// Instance variables
		private short move;
		private int value;

		// Constructor
		public MoveNode(short move, int value) {
			this.move = move;
			this.value = value;
		}

		// Get the MIN/MAX value associated with this move
		public int getValue() {
			return value;
		}

		// Get the move
		public short getMove() {
			return this.move;
		}
	}


	// Minimax AI constructor
	public AlphaBetaAI(int depth) {
		MAX_DEPTH = depth;
	}

	public short getMove(Position position) {

		AI_PLAYER = position.getToPlay();

		MoveNode bestMove = null;
		int iterationDepth = MAX_DEPTH;
		// Iterative searching of best move
		for (int i = 1; i <= iterationDepth; i++) {

			MAX_DEPTH = i;
			bestMove = alphaBetaSearch(position);

			// Print stats
			System.out.println("# states visited: " + statesVisited);
			System.out.println("Depth reached: " + depthReached + "\n");

			// Reset Stats
			statesVisited = 0;
			depthReached = 0;

			if (bestMove.getValue() == MAX_UTILITY) {
				System.out.println("Move value: " + bestMove.getValue());
				return bestMove.getMove();
			}
		}

		System.out.println("Move value: " + bestMove.getValue());
		return bestMove.getMove();
	}

	private MoveNode alphaBetaSearch(Position position) {
		///////// Initialize best move ///////////
		MoveNode bestMove = null;
		short[] moves = position.getAllMoves();
		try {
			position.doMove(moves[0]);
			bestMove = new MoveNode(moves[0], minValue(position, 1, 
					Integer.MIN_VALUE, Integer.MAX_VALUE));
			position.undoMove();
		}
		catch (IllegalMoveException e) {
			e.printStackTrace();
		}


		///////// Get the best move ///////////
		for (int i = 1; i < moves.length; i++) {
			try {
				position.doMove(moves[i]);
				MoveNode currentMove = new MoveNode(moves[i], minValue(position, 1,
						Integer.MIN_VALUE, Integer.MAX_VALUE));
				position.undoMove();

				// Update best move as needed
				if (currentMove.getValue() >= bestMove.getValue()) {
					bestMove = currentMove;
				}
			}

			catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}

		return bestMove;
	}



	private int minValue(Position position, int depth, int a, int b) {
		incrementVisited();
		incrementDepth(depth);

		// Initialize alpha, beta
		int alpha = a;
		int beta = b;

		// If position passes cutoff test, return utility function of position
		if (cutoffTest(position, depth)) {
			return utilityFn(position, depth);
		}

		// Otherwise return the minimum of the maxValues of all the possible
		// subsequent positions
		int minValue = MAX_UTILITY;
		for (short move: position.getAllMoves()) {
			try {
				position.doMove(move);
				minValue = Math.min(minValue, maxValue(position, depth+1,
						alpha, beta));
				position.undoMove();

				// Pruning
				if (minValue <= alpha){
					return minValue;
				}

				// Set beta
				beta = Math.min(beta, minValue);
			}

			catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}

		return minValue;
	}


	private int maxValue(Position position, int depth, int a, int b) {
		incrementVisited();
		incrementDepth(depth);

		// Initialize alpha, beta
		int alpha = a;
		int beta = b;

		// If position passes cutoff test, return utility function of position
		if (cutoffTest(position, depth)) {
			return utilityFn(position, depth);
		}


		// Otherwise return the maximum of the minValues of all the possible
		// subsequent positions
		int maxValue = MIN_UTILITY;
		for (short move: position.getAllMoves()) {
			try {
				position.doMove(move);
				maxValue = Math.max(maxValue, minValue(position, depth+1,
						alpha, beta));
				position.undoMove();

				// Pruning
				if (maxValue >= beta){
					return maxValue;
				}

				// Set alpha
				alpha = Math.max(alpha, maxValue);
			}

			catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}

		return maxValue;
	}

	// Helper method. Returns true if position has reached terminating 
	// state or maximum depth has been reached
	private boolean cutoffTest(Position position, int depth) {
		return (depth == MAX_DEPTH || position.isTerminal()); 
	}

	// Returns the utility value of the terminal position
	private int utilityFn(Position position, int depth) {

		// If it's a game terminating position, return appropriate value
		if (position.isTerminal()) {
			// Return 0 for a stalemate
			if (position.isStaleMate()) {
				return 0;
			}

			// Return large value or its negative for checkmate
			if (position.isMate()) {
				if (position.getToPlay() != AI_PLAYER) {
					// AI has won
					return MAX_UTILITY;
				}

				else {
					// AI has lost
					return MIN_UTILITY;
				}
			}

			// Must be a draw. Return 0
			else {
				return 0;
			}
		}

		// Otherwise, max depth was reached, so return evaluation function
		else {
			return evalFn(position);
		}
	}


	// Return the evaluation value of the terminal position
	private int evalFn(Position position) {

		if (position.getToPlay() == AI_PLAYER) {
			return position.getMaterial();
		} 

		else {
			return (-1 * position.getMaterial());
		}

	}


	private void incrementDepth(int depth) {
		depthReached = Math.max(depth, depthReached);
	}

	private void incrementVisited() {
		statesVisited++;
	}
}
