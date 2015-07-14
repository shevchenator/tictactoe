package ru.shevchenko.tictactoe;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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
            public void onTurnMade(final CellState state, final Cell cell) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView currentCellView = ButterKnife.findById(GameActivity.this, CellToViewAdapter.cellToView(cell));

                        if (state.equals(CellState.X)) {
                            currentCellView.setImageResource(R.drawable.x_normal);
                            gameStatus.setText("O turn...");
                        } else if (state.equals(CellState.O)) {
                            currentCellView.setImageResource(R.drawable.o_normal);
                            gameStatus.setText("X turn...");
                        }
                        showMoveStatus(state);
                    }
                });
            }

            @Override
            public void win(final GameStatus state, final Line line) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWinnerStatus(state);
                        markWinningLine(state, line);
                    }
                });
            }

            @Override
            public void draw() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameStatus.setText("Draw");
                    }
                });
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

    private void showMoveStatus(CellState madeMoveState) {
        if (gameManager.getGameMode().equals(GameMode.VS_AI)) {
            if (madeMoveState.equals(gameManager.getAiSeed())) {
                gameStatus.setText("Your turn...");
            } else {
                gameStatus.setText("AI turn...");
            }
        } else {
            if (madeMoveState.equals(CellState.X)) {
                gameStatus.setText("O player turn...");
            } else if (madeMoveState.equals(CellState.O)) {
                gameStatus.setText("X player turn...");
            }
        }
    }

    private void showWinnerStatus(GameStatus winner) {
        if (gameManager.getGameMode().equals(GameMode.VS_AI)) {
            if (winner.toWinnerState().equals(gameManager.getAiSeed())) {
                gameStatus.setText("AI wins!");
            } else {
                gameStatus.setText("You win! No way!");
            }
        } else {
            if (winner.equals(GameStatus.X_WIN)) {
                gameStatus.setText("X player wins!");
            } else {
                gameStatus.setText("O player wins!");
            }
        }
    }

    private void markWinningLine(GameStatus status, Line line) {
        int imgRes = status.equals(GameStatus.X_WIN) ? R.drawable.x_win : R.drawable.o_win;
        for (Cell cell : line.getCellRow()) {
            ImageView currentCellView = ButterKnife.findById(GameActivity.this, CellToViewAdapter.cellToView(cell));
            currentCellView.setImageResource(imgRes);
        }
    }

    private void clearBoard() {
        for (int i = 0; i < boardLayout.getChildCount(); i++) {
            ImageView cell = (ImageView) boardLayout.getChildAt(i);
            cell.setImageResource(0);
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
            ImageView cell = (ImageView) boardLayout.getChildAt(i);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) cell.getLayoutParams();
            params.height = cellDimension - params.topMargin - params.bottomMargin;
            params.width = cellDimension - params.leftMargin - params.rightMargin;
            cell.setLayoutParams(params);
        }
    }
}
