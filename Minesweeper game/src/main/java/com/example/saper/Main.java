package com.example.saper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

public class Main extends Application implements Board.GameOverListener {
    private static final int ROWS = 9;
    private static final int COLUMNS = 9;
    private static final int MINES = 10;

    private Board board;
    private VBox rootLayout;
    private Label minesLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        board = new Board(ROWS, COLUMNS, MINES);
        board.setGameOverListener(this);

        minesLabel = new Label("Ilość min w grze: " + MINES);

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> resetGame());

        HBox controls = new HBox();
        controls.setSpacing(10);
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(minesLabel, resetButton);

        rootLayout = new VBox();
        rootLayout.setSpacing(10);
        rootLayout.getChildren().addAll(controls, board.getBoardPane());

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Saper");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void onGameOver() {
        resetGame();
    }

    private void resetGame() {
        board = new Board(ROWS, COLUMNS, MINES);
        board.setGameOverListener(this);
        rootLayout.getChildren().set(1, board.getBoardPane());
    }
}
