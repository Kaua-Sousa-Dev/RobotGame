package principal;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import principal.engines.*;
public class GameScreen {

    private static final int MAX = 3; // Limite do tabuleiro

    private GameLauncher launcher; // Para poder voltar ao menu
    private GridPane gridPane;
    private TextArea logTextArea;
    private HBox userInputControls; // Controles para o Main1
    private TextField moveInput;
    private Button moveButton;

    private GameEngineBase gameEngine; // O motor de jogo atual
    private AnimationTimer gameLoop;

    public GameScreen(GameLauncher launcher) {
        this.launcher = launcher;
    }

    public BorderPane createLayout(int gameMode) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // --- Centro: O Tabuleiro (GridPane) ---
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        root.setCenter(gridPane);

        // --- Fundo: Log e Controles ---
        VBox bottomLayout = new VBox(10);
        logTextArea = new TextArea();
        logTextArea.setEditable(false);
        logTextArea.setPrefHeight(200);
        logTextArea.setFont(new Font(14));

        userInputControls = new HBox(10);
        userInputControls.setAlignment(Pos.CENTER);
        moveInput = new TextField();
        moveInput.setPromptText("Digite o movimento (1-4 ou up/down/left/right)");
        moveButton = new Button("Mover");
        userInputControls.getChildren().addAll(new Label("Movimento:"), moveInput, moveButton);

        bottomLayout.getChildren().addAll(logTextArea, userInputControls);
        root.setBottom(bottomLayout);

        // --- Topo: Voltar ao Menu ---
        Button backButton = new Button("Voltar ao Menu");
        backButton.setOnAction(e -> {
            if (gameLoop != null) gameLoop.stop();
            launcher.showMenu();
        });
        root.setTop(backButton);

        setupGameMode(gameMode);

        return root;
    }

    private void setupGameMode(int gameMode) {
        switch (gameMode) {
            case 1:
                gameEngine = new GameEngine1(this); // Baseado no Main1
                userInputControls.setVisible(true); // Mostra controles do Main1
                moveButton.setOnAction(e -> {
                    gameEngine.processInput(moveInput.getText());
                    moveInput.clear();
                });
                break;
            case 2:
                gameEngine = new GameEngine2(this); // Baseado no Main2
                userInputControls.setVisible(false);
                startGameLoop();
                break;
            case 3:
                gameEngine = new GameEngine3(this); // Baseado no Main3
                userInputControls.setVisible(false);
                startGameLoop();
                break;
            case 4:
                gameEngine = new GameEngine4(this); // Baseado no Main4
                userInputControls.setVisible(false);
                startGameLoop();
                break;
        }
        gameEngine.initialize(); // Prepara o jogo
        atualizarGrid(); // Desenha o estado inicial
    }

    // Inicia o loop para jogos automáticos (Modos 2, 3, 4)
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                // Pausa de 1 segundo (1_000_000_000 nanossegundos)
                if (now - lastUpdate < 1_000_000_000) {
                    return;
                }
                lastUpdate = now;

                if (!gameEngine.isGameFinished()) {
                    gameEngine.update(); // Roda um turno do jogo
                } else {
                    this.stop(); // Para o loop
                }
            }
        };
        gameLoop.start();
    }

    // Método público para o Engine adicionar logs
    public void log(String message) {
        logTextArea.appendText(message + "\n");
    }

    // Método público para o Engine redesenhar o tabuleiro
    public void atualizarGrid() {
        gridPane.getChildren().clear(); // Limpa o tabuleiro anterior

        // Pede ao Engine o estado atual do tabuleiro
        String[][] board = gameEngine.getBoardState();

        for (int y = MAX; y >= 0; y--) {
            // Adiciona labels do eixo Y
            gridPane.add(new Label(" " + y + "|"), 0, (MAX - y));

            for (int x = 0; x <= MAX; x++) {
                String emoji = board[x][y]; // Pega o emoji do engine
                Label cell = new Label(emoji);
                cell.setPrefSize(80, 80);
                cell.setAlignment(Pos.CENTER);
                cell.setFont(new Font(30));
                cell.setStyle("-fx-border-color: lightgray;");
                gridPane.add(cell, x + 1, (MAX - y)); // (col, lin) - +1 por causa do eixo Y
            }
        }

        // Adiciona labels do eixo X
        for (int x = 0; x <= MAX; x++) {
            gridPane.add(new Label(" " + x + " "), x + 1, MAX + 1);
        }
    }
}