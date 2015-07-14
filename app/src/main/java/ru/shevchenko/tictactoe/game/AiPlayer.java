package ru.shevchenko.tictactoe.game;

import android.util.Log;
import android.util.Pair;

import ru.shevchenko.tictactoe.model.Board;
import ru.shevchenko.tictactoe.model.Cell;
import ru.shevchenko.tictactoe.model.CellState;
import ru.shevchenko.tictactoe.model.GameStatus;
import ru.shevchenko.tictactoe.model.GameStatusProcessor;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 13/07/15.
 */
public class AiPlayer {

    private CellState aiSeed;
    private CellState opponentSeed;
    private Cell choice;

    public AiPlayer(CellState aiSeed) {
        this.aiSeed = aiSeed;

        if (aiSeed.equals(CellState.X)) {
            opponentSeed = CellState.O;
        } else {
            opponentSeed = CellState.X;
        }
    }

    public Pair<Cell, CellState> makeMove(Board board) {
        minMax(board.clone(), 0, aiSeed);
        return new Pair<>(choice, aiSeed);
    }

    private int minMax(Board board, int depth, CellState currentMove) {
        if (isGameOver(board)) {
            return calculateScore(board, depth);
        }

        int bestSoFar;
        Cell bestMove = null;
        if (currentMove.equals(aiSeed)) {
            bestSoFar = -2;
        } else {
            bestSoFar = 2;
        }

//        Map<Cell, Integer> candidateMoves = new HashMap<>();
        for (Cell cell : board.getAvailablePlaces()) {
            Board clonedBoard = board.clone();
            clonedBoard.place(currentMove, cell);
            int score = minMax(clonedBoard, depth + 1, currentMove.equals(aiSeed) ? opponentSeed : aiSeed);

            if (depth == 0) {
                Log.d("WTF", cell + " -> " + score + "\n");
            }

            if (currentMove.equals(aiSeed) && score > bestSoFar) {
                bestSoFar = score;
                bestMove = cell;
            } else if (currentMove.equals(opponentSeed) && score < bestSoFar) {
                bestSoFar = score;
                bestMove = cell;
            }
        }
        choice = bestMove;
        return bestSoFar;
    }

    private boolean isGameOver(Board board) {
        GameStatusProcessor statusProcessor = new GameStatusProcessor(board);
        GameStatus gameStatus = statusProcessor.checkGameStatus();

        return gameStatus.equals(GameStatus.X_WIN) ||
                gameStatus.equals(GameStatus.O_WIN) ||
                gameStatus.equals(GameStatus.DRAW);
    }

    private int calculateScore(Board board, int depth) {
        GameStatusProcessor statusProcessor = new GameStatusProcessor(board);
        GameStatus gameStatus = statusProcessor.checkGameStatus();

        if (gameStatus.equals(GameStatus.DRAW)) {
            return 0;
        } else {
            CellState winnerState = statusProcessor.getWinnerState();

            if (winnerState.equals(aiSeed)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public CellState getAiSeed() {
        return aiSeed;
    }
}
