package principal.engines;

import excepts.MovimentoInvalidoException;
import principal.GameScreen;
import robo.Robo;

// Lógica extraída do Main1.java
public class GameEngine1 extends GameEngineBase {

    private Robo robo;

    public GameEngine1(GameScreen screen) {
        super(screen);
    }

    @Override
    public void initialize() {
        // Em vez de "Scanner", definimos os valores
        this.robo = new Robo("vermelho"); // Cor fixa
        setupFood(); // Define a comida (ex: em 3,3)
        screen.log("Robô criado. Posição: (" + robo.getX() + "," + robo.getY() + ")");
        screen.log("DIGITE O MOVIMENTO: (1-up, 2-down, 3-right, 4-left ou texto)");
    }

    @Override
    public void processInput(String input) {
        if (gameFinished) return;

        try {
            // Bloco principal para tentar o movimento
            try {
                // 1. Tenta converter para int
                int codigo = Integer.parseInt(input);
                robo.mover(codigo); // Pode lançar MovimentoInvalidoException
            } catch (NumberFormatException e) {
                // 2. Se não for int, trata como string
                robo.mover(input); // Também pode lançar MovimentoInvalidoException
            }

            // 3. Se chegou aqui, o movimento foi válido (ou não reconhecido)
            screen.log("Nova posição: (" + robo.getX() + "," + robo.getY() + ")");

            if (robo.encontrarAlimento(foodX, foodY)) {
                screen.log("O ROBÔ ENCONTROU O ALIMENTO!");
                gameFinished = true;
            }

        } catch (MovimentoInvalidoException e) {
            // 4. Se QUALQUER um dos 'mover()' falhar, captura aqui
            screen.log(e.getMessage()); // Envia a exceção para o log da GUI
        }

        screen.atualizarGrid(); // Redesenha a tela
    }

    @Override
    public String[][] getBoardState() {
        String[][] board = new String[4][4];
        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 4; x++) {
                if (x == robo.getX() && y == robo.getY()) {
                    board[x][y] = robo.getEmoji();
                } else {
                    board[x][y] = plano[x][y].getsimbolo();
                }
            }
        }
        return board;
    }
}