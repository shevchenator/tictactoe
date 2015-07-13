package ru.shevchenko.tictactoe;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.shevchenko.tictactoe.adapter.CellToViewAdapter;
import ru.shevchenko.tictactoe.game.GameManager;
import ru.shevchenko.tictactoe.game.OnGameStateChangeListener;
import ru.shevchenko.tictactoe.model.Cell;
import ru.shevchenko.tictactoe.model.CellState;
import ru.shevchenko.tictactoe.model.GameStatus;
import ru.shevchenko.tictactoe.model.Line;


public class GameActivity extends Activity {

    @Bind(R.id.game_base_layout)
    RelativeLayout baseLayout;
    @Bind(R.id.game_board_layout)
    GridLayout boardLayout;

    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        baseLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutBoard();
                if (Build.VERSION.SDK_INT >= 16) {
                    baseLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    baseLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        gameManager = new GameManager(new OnGameStateChangeListener() {
            @Override
            public void onTurnMade(CellState state, Cell cell) {
                TextView currentCellView = ButterKnife.findById(GameActivity.this, CellToViewAdapter.cellToView(cell));

                if (state.equals(CellState.X)) {
                    currentCellView.setText("X");
                } else if (state.equals(CellState.O)) {
                    currentCellView.setText("O");
                }
            }

            @Override
            public void win(GameStatus state, Line line) {
                Toast.makeText(GameActivity.this, "" + state, Toast.LENGTH_LONG).show();
            }

            @Override
            public void draw() {
                Toast.makeText(GameActivity.this, "Draw", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @OnClick({R.id.game_cell_1_1,
            R.id.game_cell_1_2,
            R.id.game_cell_1_3,
            R.id.game_cell_2_1,
            R.id.game_cell_2_2,
            R.id.game_cell_2_3,
            R.id.game_cell_3_1,
            R.id.game_cell_3_2,
            R.id.game_cell_3_3})
    void onCellClick(View view) {
        gameManager.makeMove(CellToViewAdapter.viewToCell(view.getId()));
    }

    @OnClick(R.id.game_new)
    void onNewGameStartPressed(View view) {
        gameManager.startNewGame();
        clearBoard();
    }

    private void clearBoard() {
        for (int i = 0; i < boardLayout.getChildCount(); i++) {
            TextView cell = (TextView) boardLayout.getChildAt(i);
            cell.setText("");
        }
    }

    private void layoutBoard() {
        int boardHeight = baseLayout.getHeight();
        int boardWidth = baseLayout.getWidth();
        int cellDimension;

        if (boardHeight > boardWidth) {
            cellDimension = boardWidth / 3 - 30;
        } else {
            cellDimension = boardHeight/ 3 - 30;
        }

        for (int i = 0; i < boardLayout.getChildCount(); i++) {
            TextView cell = (TextView) boardLayout.getChildAt(i);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) cell.getLayoutParams();
            params.height = cellDimension - params.topMargin - params.bottomMargin;
            params.width = cellDimension - params.leftMargin - params.rightMargin;
            cell.setLayoutParams(params);
        }
    }
}
