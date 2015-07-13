package ru.shevchenko.tictactoe.game;

import ru.shevchenko.tictactoe.model.Cell;
import ru.shevchenko.tictactoe.model.CellState;
import ru.shevchenko.tictactoe.model.GameStatus;
import ru.shevchenko.tictactoe.model.Line;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 13/07/15.
 */
public interface OnGameStateChangeListener {
    void onTurnMade(CellState state, Cell cell);
    void win(GameStatus status, Line line);
    void draw();
}
