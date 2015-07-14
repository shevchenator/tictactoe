package ru.shevchenko.tictactoe.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.shevchenko.tictactoe.model.Board;
import ru.shevchenko.tictactoe.model.Cell;
import ru.shevchenko.tictactoe.model.CellState;
import ru.shevchenko.tictactoe.model.GameStatus;
import ru.shevchenko.tictactoe.model.GameStatusProcessor;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 13/07/15.
 */
public class AiPlayer {

    public interface OnMoveChooseListener {
        void onChoose(Cell cell);
    }

    private static final int WIN_SCORE = 10;

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

    public void chooseMove(final Board board, final OnMoveChooseListener listener) {
        if (listener == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (board.isBlank() || board.getAvailablePlaces().size() == 8) {
                    listener.onChoose(makeFistMove(board));
                    return;
                }

                minMax(board, 0, aiSeed, -AiPlayer.WIN_SCORE - 1, AiPlayer.WIN_SCORE + 1);
                listener.onChoose(choice);
            }
        }).start();
    }

    private int minMax(Board board, int depth, CellState currentMove, int alpha, int beta) {
        if (isGameOver(board)) {
            return calculateScore(board, depth);
        }

        Cell bestMove = null;
        for (Cell cell : board.getAvailablePlaces()) {
            board.place(currentMove, cell);
            int score = minMax(board, depth + 1, currentMove.equals(aiSeed) ? opponentSeed : aiSeed, alpha, beta);
            board.clearCell(cell);

            if (currentMove.equals(aiSeed) && score > alpha) {
                alpha = score;
                bestMove = cell;
            } else if (currentMove.equals(opponentSeed) && score < beta) {
                beta = score;
                bestMove = cell;
            }

            if (alpha >= beta) {
                break;
            }
        }
        choice = bestMove;
        return currentMove.equals(aiSeed) ? alpha : beta;
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
                return AiPlayer.WIN_SCORE - depth;
            } else {
                return depth - AiPlayer.WIN_SCORE;
            }
        }
    }

    private Cell makeFistMove(Board board) {
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(0, 0));
        cells.add(new Cell(2, 0));
        cells.add(new Cell(0, 2));
        cells.add(new Cell(2, 2));
        cells.add(new Cell(1, 1));
        if (aiSeed.equals(CellState.O)) {
            if (board.getCellState(new Cell(1,1)).equals(CellState.BLANK)) {
                return new Cell(1,1);
            }
            for (Cell cell : cells) {
                if (!board.getCellState(cell).equals(CellState.BLANK)) {
                    cells.remove(cell);
                    break;
                }
            }
        }
        return cells.get(new Random().nextInt(cells.size()));
    }

    public CellState getAiSeed() {
        return aiSeed;
    }
}
