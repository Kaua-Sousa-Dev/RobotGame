package principal.engines;

import excepts.MovimentoInvalidoException;
import principal.GameScreen;
import robo.Robo;
// Importe as classes necessárias se tiver criado pacotes separados

import java.util.logging.Level;
import java.util.logging.Logger;

// Lógica extraída do Main1.java e adaptada
public class GameEngine1 extends GameEngineBase {

    private static final Logger LOGGER = Logger.getLogger(GameEngine1.class.getName());
    private Robo robo;

    public GameEngine1(GameScreen screen) {
        super(screen);
    }

    @Override
    public void initialize() {
        // [REFATORAÇÃO]: Criação do robô
        this.robo = new Robo("vermelho"); // Cor fixa

        setupFood(); // Define a comida (ex: em 3,3)

        // [CORREÇÃO]: Acesso à posição via getPosicao()
        screen.log("Robô criado. Posição: " + robo.getPosicao());
        screen.log("DIGITE O MOVIMENTO: (1-up, 2-down, 3-right, 4-left ou texto)");
    }

    @Override
    public void processInput(String input) {
        if (gameFinished) return;

        try {
            // [REFATORAÇÃO]: Simplificação drástica da lógica
            // O novo método mover(String) do Robo já lida com números ("1") ou texto ("up")
            robo.mover(input);

            // [CORREÇÃO]: Acesso à posição
            screen.log("Nova posição: " + robo.getPosicao());

            // Verifica colisão com alimento
            // Assumindo que foodX e foodY vêm da classe pai (GameEngineBase)
            if (robo.getPosicao().x() == foodX && robo.getPosicao().y() == foodY) {
                screen.log("O ROBÔ ENCONTROU O ALIMENTO! 🍇");
                gameFinished = true;
            }

        } catch (MovimentoInvalidoException e) {
            // Log técnico
            LOGGER.log(Level.WARNING, "Input inválido: {0}", e.getMessage());
            // Feedback visual para o usuário
            screen.log(e.getMessage());
        }

        screen.atualizarGrid(); // Redesenha a tela
    }

    @Override
    public String[][] getBoardState() {
        String[][] board = new String[4][4];
        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 4; x++) {
                // [CORREÇÃO]: Comparação usando getPosicao()
                // Verifica se o robô está nesta célula (x,y)
                if (x == robo.getPosicao().x() && y == robo.getPosicao().y()) {
                    board[x][y] = robo.getEmoji();
                } else {
                    board[x][y] = plano[x][y].getsimbolo();
                }
            }
        }
        return board;
    }
}