package com.example.tictactoe;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Random;

public class ReversedTicTacToe extends Application {
    private final char[][] board;
    private char currentPlayer;
    private boolean gameEnd;
    private final Difficulty difficulty;

    enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public ReversedTicTacToe() {
        this(Difficulty.EASY); // Set default difficulty
    }

    public ReversedTicTacToe(Difficulty difficulty) {
        board = new char[3][3];
        currentPlayer = 'X';
        gameEnd = false;
        this.difficulty = difficulty;
        initializeBoard();
    }


    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }
    }

    private void printBoard(GridPane boardPane) {
        boardPane.getChildren().clear();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button(Character.toString(board[row][col]));
                button.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                button.setMinSize(80, 80);
                button.setOnAction(e -> {
                    if (!gameEnd && currentPlayer == 'X') {
                        int buttonRow = GridPane.getRowIndex(button);
                        int buttonCol = GridPane.getColumnIndex(button);
                        makeMove(buttonRow, buttonCol);
                        checkGameEnd(boardPane);
                        if (!gameEnd)
                            makeAIMove(boardPane);
                    }
                });

                boardPane.add(button, col, row);
            }
        }
    }

    private boolean isMoveValid(int row, int col) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            return board[row][col] == ' ';
        }
        return false;
    }

    private void makeMove(int row, int col) {
        if (isMoveValid(row, col)) {
            board[row][col] = currentPlayer;
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        } else {
            System.out.println("Invalid move! Please try again.");
        }
    }

    private boolean checkWin(char player) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkGameEnd(GridPane boardPane) {
        if (checkWin('O')) {
            showAlert("Player X wins!");
            gameEnd = true;
        } else if (checkWin('X')) {
            showAlert("Player O wins!");
            gameEnd = true;
        } else if (isBoardFull()) {
            showAlert("It's a draw!");
            gameEnd = true;
        }

        if (gameEnd) {
            disableBoardButtons(boardPane);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void disableBoardButtons(GridPane boardPane) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = (Button) boardPane.getChildren().get(row * 3 + col);
                button.setDisable(true);
            }
        }
    }

    private void makeAIMove(GridPane boardPane) {
        if (difficulty == Difficulty.EASY) {
            makeRandomMove();
        } else if (difficulty == Difficulty.MEDIUM) {
            makeSlightlyOptimalMove();
        } else if (difficulty == Difficulty.HARD) {
            makeMostlyOptimalMove();
        }

        printBoard(boardPane);
        checkGameEnd(boardPane);
    }

    private void makeRandomMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!isMoveValid(row, col));
        makeMove(row, col);
    }

    private void makeSlightlyOptimalMove() {
        // Check if AI can win in the next move
        if (makeOptimalMove(currentPlayer)) {
            return;
        }

        // Check if player can win in the next move and block
        if (makeOptimalMove((currentPlayer == 'X') ? 'O' : 'X')) {
            return;
        }

        // If no optimal move is possible, make a random move
        makeRandomMove();
    }

    private void makeMostlyOptimalMove() {
        // Check if AI can win in the next move
        if (makeOptimalMove(currentPlayer)) {
            return;
        }

        // Check if player can win in the next move and block
        if (makeOptimalMove((currentPlayer == 'X') ? 'O' : 'X')) {
            return;
        }

        // Make a move to prioritize corners, then center, then edges
        if (isMoveValid(0, 0)) {
            makeMove(0, 0);
            return;
        }
        if (isMoveValid(2, 2)) {
            makeMove(2, 2);
            return;
        }
        if (isMoveValid(0, 2)) {
            makeMove(0, 2);
            return;
        }
        if (isMoveValid(2, 0)) {
            makeMove(2, 0);
            return;
        }
        if (isMoveValid(1, 1)) {
            makeMove(1, 1);
            return;
        }
        if (isMoveValid(0, 1)) {
            makeMove(0, 1);
            return;
        }
        if (isMoveValid(1, 0)) {
            makeMove(1, 0);
            return;
        }
        if (isMoveValid(1, 2)) {
            makeMove(1, 2);
            return;
        }
        if (isMoveValid(2, 1)) {
            makeMove(2, 1);
        }
    }

    private boolean makeOptimalMove(char player) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            int emptyCount = 0;
            int playerCount = 0;
            int emptyRow = -1;
            int emptyCol = -1;
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    emptyCount++;
                    emptyRow = row;
                    emptyCol = col;
                } else if (board[row][col] == player) {
                    playerCount++;
                }
            }
            if (emptyCount == 1 && playerCount == 2) {
                makeMove(emptyRow, emptyCol);
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            int emptyCount = 0;
            int playerCount = 0;
            int emptyRow = -1;
            int emptyCol = -1;
            for (int row = 0; row < 3; row++) {
                if (board[row][col] == ' ') {
                    emptyCount++;
                    emptyRow = row;
                    emptyCol = col;
                } else if (board[row][col] == player) {
                    playerCount++;
                }
            }
            if (emptyCount == 1 && playerCount == 2) {
                makeMove(emptyRow, emptyCol);
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == ' ' && board[1][1] == player && board[2][2] == player) {
            makeMove(0, 0);
            return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == ' ') {
            makeMove(2, 2);
            return true;
        }
        if (board[0][2] == ' ' && board[1][1] == player && board[2][0] == player) {
            makeMove(0, 2);
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == ' ') {
            makeMove(2, 0);
            return true;
        }

        return false;
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane boardPane = new GridPane();
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setHgap(50);
        boardPane.setVgap(50);

        Image background = new Image("C:\\Users\\ANISMUNIRAH\\Downloads\\suzume.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(background,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        boardPane.setBackground(new Background(backgroundImage));

        printBoard(boardPane);

        VBox root = new VBox(10);
        Text title = new Text("Reversed Tic-Tac-Toe");
        title.setFont(Font.font("Bahnschrift", FontWeight.BOLD, 24));
        title.getStyleClass().add("title"); // Add this line to apply the "title" CSS class

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
            ReversedTicTacToe newGame = new ReversedTicTacToe(difficulty);
            newGame.start(primaryStage);
        });

        root.getChildren().addAll(title, boardPane, newGameButton);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 450);
       // scene.getStylesheets().add("style.css");

        primaryStage.setTitle("Reversed Tic-Tac-Toe");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
