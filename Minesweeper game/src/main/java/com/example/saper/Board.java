package com.example.saper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import java.util.Random;

public class Board {
    private int rows;
    private int columns;
    private int mines;
    private Cell[][] cells;
    private GridPane boardPane;
    private Button[][] cellButtons;
    private GameOverListener gameOverListener;

    public Board(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        cells = new Cell[rows][columns];
        cellButtons = new Button[rows][columns];
        boardPane = new GridPane();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new Cell();
            }
        }

        placeMines();
        calculateAdjacentMines();
        createBoardGUI();
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < mines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(columns);

            if (!cells[row][col].hasMine()) {
                cells[row][col].setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int adjacentMines = 0;

                for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                    for (int colOffset = -1; colOffset <= 1; colOffset++) {
                        if (rowOffset == 0 && colOffset == 0) {
                            continue;
                        }

                        int newRow = i + rowOffset;
                        int newCol = j + colOffset;

                        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns && cells[newRow][newCol].hasMine()) {
                            adjacentMines++;
                        }
                    }
                }

                cells[i][j].setAdjacentMines(adjacentMines);
            }
        }
    }

    private void createBoardGUI() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Button cellButton = new Button(" ");
                cellButton.setMinSize(30, 30);
                int row = i;
                int col = j;
                cellButton.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (checkForMine(row, col)) {
                            showGameOverDialog();
                            if (gameOverListener != null) {
                                gameOverListener.onGameOver();
                            }
                        } else {
                            revealCell(row, col);
                        }
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                    }
                });

                cellButtons[i][j] = cellButton;
                boardPane.add(cellButton, j, i);
            }
        }
    }

    private boolean checkForMine(int row, int col) {
        return cells[row][col].hasMine();
    }

    private void showGameOverDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game over");
        alert.setHeaderText("You have lost!");
        alert.setContentText("You have clicked on a mine. Try again.");
        alert.showAndWait();
    }

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public interface GameOverListener {
        void onGameOver();
    }
    private void revealCell(int row, int col) {
        if (cells[row][col].isRevealed()) {
            return;
        }

        cells[row][col].setRevealed(true);
        Button cellButton = cellButtons[row][col];
        cellButton.setStyle("-fx-base: yellow;");

        int adjacentMines = cells[row][col].getAdjacentMines();
        if (adjacentMines > 0) {
            cellButton.setText(Integer.toString(adjacentMines));
        }

        cellButton.setDisable(true);
    }
    public GridPane getBoardPane() {
        return boardPane;
    }
    public int getRemainingMines() {
        int remainingMines = mines;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].isFlagged()) {
                    remainingMines--;
                }
            }
        }

        return remainingMines;
    }
}