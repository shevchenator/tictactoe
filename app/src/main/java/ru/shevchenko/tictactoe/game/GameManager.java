package ru.shevchenko.tictactoe.game;

import ru.shevchenko.tictactoe.model.Board;
import ru.shevchenko.tictactoe.model.Cell;
import ru.shevchenko.tictactoe.model.CellState;
import ru.shevchenko.tictactoe.model.GameMode;
import ru.shevchenko.tictactoe.model.GameStatusProcessor;
import ru.shevchenko.tictactoe.model.GameStatus;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 12/07/15.
 */
public class GameManager {
    private Board board;
    private GameStatusProcessor statusProcessor;
    private GameStatus currentStatus;
    private GameMode gameMode;
    private CellState previousTurnState = CellState.O;
    private boolean areMovesBlocked = false;

    private AiPlayer aiPlayer;
    private OnGameStateChangeListener gameStateChangeListener;

    public GameManager(OnGameStateChangeListener gameStateChangeListener) {
        this.gameStateChangeListener = gameStateChangeListener;
        startNewGame(GameMode.PVP);
        this.statusProcessor = new GameStatusProcessor(board);
    }

    public void startNewGame(GameMode gameMode) {
        this.gameMode = gameMode;
        this.board = new Board();
        this.currentStatus = GameStatus.NOT_STARTED;
        this.previousTurnState = CellState.O;
        this.areMovesBlocked = false;
        this.statusProcessor = new GameStatusProcessor(board);
        if (gameMode.equals(GameMode.VS_AI)) {
            this.aiPlayer = new AiPlayer(CellState.O);
        } else {
            this.aiPlayer = null;
        }
    }

    public void makeMove(Cell cell) {
        if (!isGameOver() &&
                !areMovesBlocked &&
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

            if (aiPlayer != null && !isGameOver() && !previousTurnState.equals(aiPlayer.getAiSeed())) {
                areMovesBlocked = true;
                aiPlayer.chooseMove(board, new AiPlayer.OnMoveChooseListener() {
                    @Override
                    public void onChoose(Cell cell) {
                        areMovesBlocked = false;
                        makeMove(cell);
                    }
                });
            }
        }
    }

    public void startAi() {
        this.aiPlayer = new AiPlayer(CellState.X);
        aiPlayer.chooseMove(board, new AiPlayer.OnMoveChooseListener() {
            @Override
            public void onChoose(Cell cell) {
                makeMove(cell);
            }
        });
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

    public GameMode getGameMode() {
        return gameMode;
    }

    public GameStatus getCurrentGameStatus() {
        return currentStatus;
    }

    public CellState getAiSeed() {
        return aiPlayer.getAiSeed();
    }
}
