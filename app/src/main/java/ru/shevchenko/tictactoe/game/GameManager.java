package ru.shevchenko.tictactoe.game;

import ru.shevchenko.tictactoe.model.Board;
import ru.shevchenko.tictactoe.model.Cell;
import ru.shevchenko.tictactoe.model.CellState;
import ru.shevchenko.tictactoe.model.GameStatusProcessor;
import ru.shevchenko.tictactoe.model.GameStatus;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 12/07/15.
 */
public class GameManager {
    private Board board;
    private GameStatusProcessor statusProcessor;
    private GameStatus currentStatus;
    private CellState previousTurnState = CellState.O;

    private AiPlayer aiPlayer;

    private OnGameStateChangeListener gameStateChangeListener;

    public GameManager(OnGameStateChangeListener gameStateChangeListener) {
        this.gameStateChangeListener = gameStateChangeListener;
        this.aiPlayer = new AiPlayer(CellState.O);
        startNewGame();
        this.statusProcessor = new GameStatusProcessor(board);
    }

    public void startNewGame() {
        this.board = new Board();
        this.currentStatus = GameStatus.NOT_STARTED;
        this.previousTurnState = CellState.O;
        this.statusProcessor = new GameStatusProcessor(board);
        this.aiPlayer = new AiPlayer(CellState.O);
    }

    public void makeMove(Cell cell) {
        if (!isGameOver() &&
                board.getCellState(cell).equals(CellState.BLANK)) {
            if (previousTurnState.equals(CellState.O) && board.place(CellState.X, cell)) {
                gameStateChangeListener.onTurnMade(CellState.X, cell);
                previousTurnState = CellState.X;
            } else if (previousTurnState.equals(CellState.X) && board.place(CellState.O, cell)) {
                gameStateChangeListener.onTurnMade(CellState.O, cell);
                previousTurnState = CellState.O;
            }

            currentStatus = statusProcessor.checkGameStatus();
            reactOnStatusChange();

            if (!previousTurnState.equals(aiPlayer.getAiSeed()) && !isGameOver()) {
                makeMove(aiPlayer.makeMove(board));
            }
        }
    }

    public void startAi() {
        this.aiPlayer = new AiPlayer(CellState.X);
        makeMove(aiPlayer.makeMove(board));
    }

    private boolean isGameOver() {
        return currentStatus.equals(GameStatus.X_WIN) ||
                currentStatus.equals(GameStatus.O_WIN) ||
                currentStatus.equals(GameStatus.DRAW);
    }

    private void reactOnStatusChange() {
        switch (currentStatus) {
            case DRAW:
                gameStateChangeListener.draw();
                break;
            case X_WIN:
                gameStateChangeListener.win(GameStatus.X_WIN, statusProcessor.getWinningLine());
                break;
            case O_WIN:
                gameStateChangeListener.win(GameStatus.O_WIN, statusProcessor.getWinningLine());
                break;
        }
    }
}
