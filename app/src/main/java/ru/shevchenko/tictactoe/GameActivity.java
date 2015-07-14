package ru.shevchenko.tictactoe;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.shevchenko.tictactoe.adapter.CellToViewAdapter;
import ru.shevchenko.tictactoe.game.GameManager;
import ru.shevchenko.tictactoe.game.OnGameStateChangeListener;
import ru.shevchenko.tictactoe.model.Cell;
import ru.shevchenko.tictactoe.model.CellState;
import ru.shevchenko.tictactoe.model.GameMode;
import ru.shevchenko.tictactoe.model.GameStatus;
import ru.shevchenko.tictactoe.model.Line;


public class GameActivity extends Activity {

    @Bind(R.id.game_base_layout)
    RelativeLayout baseLayout;
    @Bind(R.id.game_board_layout)
    GridLayout boardLayout;
    @Bind(R.id.game_status)
    TextView gameStatus;
    @Bind(R.id.game_ai_start)
    TextView letAiStart;

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
                    gameStatus.setText("O turn...");
                } else if (state.equals(CellState.O)) {
                    currentCellView.setText("O");
                    gameStatus.setText("X turn...");
                }
            }

            @Override
            public void win(GameStatus state, Line line) {
                if (state.equals(GameStatus.X_WIN)) {
                    gameStatus.setText("X player wins");
                } else {
                    gameStatus.setText("O player wins");
                }
            }

            @Override
            public void draw() {
                gameStatus.setText("Draw");
            }
        });
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
        gameStatus.setText("");
        gameManager.startNewGame(gameManager.getGameMode());
        clearBoard();
    }

    @OnClick(R.id.game_ai_start)
    void onAiStatClick(View view) {
        clearBoard();
        gameManager.startAi();
    }

    @OnClick(R.id.game_change_mode)
    void onGameModeChange(View view) {
        TextView textView = (TextView) view;
        gameStatus.setText("");
        if (textView.getTag().equals("pvp")) {
            textView.setTag("pvai");
            textView.setText(R.string.pvai);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.r2d2, 0, 0);
            letAiStart.setVisibility(View.GONE);

            clearBoard();
            gameManager.startNewGame(GameMode.PVP);
        } else if (textView.getTag().equals("pvai")) {
            textView.setTag("pvp");
            textView.setText(R.string.pvp);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.group, 0, 0);
            letAiStart.setVisibility(View.VISIBLE);

            clearBoard();
            gameManager.startNewGame(GameMode.VS_AI);
        }
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
