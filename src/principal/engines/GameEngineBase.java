package principal.engines;

import grafico.Campo;
import principal.GameScreen;
import robo.Robo;
import java.util.Map;
import obstaculo.Obstaculo;

// Classe base para todos os motores de jogo
public abstract class GameEngineBase {

    protected GameScreen screen; // Referência à View para logar e atualizar
    protected Campo[][] plano;
    protected int foodX, foodY;
    protected boolean gameFinished = false;

    public GameEngineBase(GameScreen screen) {
        this.screen = screen;
        // Inicializa o plano 4x4
        plano = new Campo[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                plano[i][j] = new Campo();
            }
        }
    }

    // Configuração inicial (ex: pedir posição da comida)
    public abstract void initialize();

    // Para jogos automáticos (Modos 2, 3, 4)
    public void update() {}

    // Para jogos controlados (Modo 1)
    public void processInput(String input) {}

    // A View chama este método para saber o que desenhar
    public abstract String[][] getBoardState();

    public boolean isGameFinished() {
        return gameFinished;
    }

    // Lógica simples para pegar posição da comida (substitui o Scanner)
    protected void setupFood() {
        // Por simplicidade, vamos fixar a comida.
        // Em um app real, você usaria um Pop-up (Dialog) para perguntar ao usuário.
        this.foodX = 3;
        this.foodY = 3;
        plano[foodX][foodY].setSimbolo("🍇");
        screen.log("Posição do Alimento: (" + foodX + "," + foodY + ")");
    }
}