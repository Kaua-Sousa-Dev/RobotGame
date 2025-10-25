package principal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameLauncher extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private VBox menuLayout;

    private double gameWidth = 600;
    private double gameHeight = 800;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("RobotGame - Menu Principal");

        // 1. Criar o layout do menu (como antes)
        menuLayout = new VBox(20); // Layout vertical com 20px de espaçamento
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setPadding(new Insets(25));

        Label title = new Label("Escolha o Modo de Jogo");
        title.setFont(new Font("Arial", 24));

        Button btnMain1 = new Button("Modo 1: 1 Robô (Controlado pelo Jogador)");
        btnMain1.setOnAction(e -> startGame(1));

        Button btnMain2 = new Button("Modo 2: 2 Robôs (Aleatório)");
        btnMain2.setOnAction(e -> startGame(2));

        Button btnMain3 = new Button("Modo 3: Robô Normal vs. Inteligente");
        btnMain3.setOnAction(e -> startGame(3));

        Button btnMain4 = new Button("Modo 4: Jogo Completo (Obstáculos)");
        btnMain4.setOnAction(e -> startGame(4));

        menuLayout.getChildren().addAll(title, btnMain1, btnMain2, btnMain3, btnMain4);

        // 2. Criar o layout raiz e a cena ÚNICA
        rootLayout = new BorderPane();
        rootLayout.setCenter(menuLayout);

        // 3. Cria a cena apenas UMA VEZ
        Scene menuScene = new Scene(rootLayout, 400, 300); // <-- MUDANÇA: A cena usa o rootLayout

        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void startGame(int gameMode) {
        // Criamos e configuramos a tela de jogo
        GameScreen gameScreen = new GameScreen(this);

        BorderPane gameLayout = gameScreen.createLayout(gameMode);

        rootLayout.setCenter(gameLayout);

        primaryStage.setTitle("RobotGame - Modo " + gameMode);

        if (!primaryStage.isFullScreen() && !primaryStage.isMaximized()) {
            primaryStage.setWidth(gameWidth);
            primaryStage.setHeight(gameHeight);
            primaryStage.centerOnScreen();
        }
    }

    // Método para voltar ao menu
    public void showMenu() {
        try {
            rootLayout.setCenter(menuLayout);
            primaryStage.setTitle("RobotGame - Menu Principal");

            if (!primaryStage.isFullScreen() && !primaryStage.isMaximized()) {
                primaryStage.setWidth(gameWidth);
                primaryStage.setHeight(gameHeight);
                primaryStage.centerOnScreen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}