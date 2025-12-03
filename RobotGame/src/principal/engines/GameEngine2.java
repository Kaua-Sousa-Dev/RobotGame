package principal.engines;

import excepts.MovimentoInvalidoException;
import principal.GameScreen;
import robo.Robo;
import java.util.Random;

public class GameEngine2 extends GameEngineBase {

    private Robo robo1;
    private Robo robo2;
    private final Random r = new Random();

    private int movimentosValidos1 = 0;
    private int movimentosInvalidos1 = 0;
    private int movimentosValidos2 = 0;
    private int movimentosInvalidos2 = 0;

    private boolean turnoRobo1 = true; // Controla de quem é a vez

    public GameEngine2(GameScreen screen) {
        super(screen);
    }

    @Override
    public void initialize() {
        screen.log("Modo 2: 2 Robôs Aleatórios");
        this.robo1 = new Robo("vermelho");
        this.robo2 = new Robo("azul");

        setupFood(); // Define a comida (ex: em 3,3)

        screen.log("Robô " + robo1.getCor() + " criado: " + robo1.getEmoji());
        screen.log("Robô " + robo2.getCor() + " criado: " + robo2.getEmoji());
    }

    @Override
    public void update() {
        if (gameFinished) return;

        if (turnoRobo1) {
            processarTurno(robo1, true);
        } else {
            processarTurno(robo2, false);
        }

        if (!gameFinished) {
            turnoRobo1 = !turnoRobo1; // Troca o turno apenas se o jogo não acabou
            screen.atualizarGrid();   // Redesenha a tela
        }
    }

    // Método auxiliar para evitar duplicação de código entre os robôs
    private void processarTurno(Robo robo, boolean isRobo1) {
        screen.log("\nVez de robô " + robo.getCor() + ":");

        int movimento = r.nextInt(4) + 1;
        screen.log(" Movimento " + movimento);

        try {
            // [CORREÇÃO]: Convertendo int para String para o novo método mover()
            robo.mover(String.valueOf(movimento));

            // Atualiza contadores corretos
            if (isRobo1) movimentosValidos1++;
            else movimentosValidos2++;

            // [CORREÇÃO]: Usando getPosicao() para verificar a comida
            // (Assumindo que encontrarAlimento também foi atualizado ou usamos a comparação manual)
            // Se o método encontrarAlimento(int, int) ainda existir no Robo, ele funcionará se usar this.posicao internamente.
            // Por segurança, usamos a comparação direta com getPosicao() aqui também:
            if (robo.getPosicao().x() == foodX && robo.getPosicao().y() == foodY) {
                screen.log("O ROBÔ " + robo.getCor().toUpperCase() + " ENCONTROU O ALIMENTO! 🍇");
                finalizarJogo();
            }

        } catch (MovimentoInvalidoException e) {
            screen.log(e.getMessage());

            if (isRobo1) movimentosInvalidos1++;
            else movimentosInvalidos2++;
        }
    }

    private void finalizarJogo() {
        gameFinished = true;
        screen.log("\n--- FIM DE JOGO ---");
        screen.log("\nRobô " + robo1.getCor() + " :");
        screen.log("Movimentos válidos: " + movimentosValidos1);
        screen.log("Movimentos inválidos: " + movimentosInvalidos1);

        screen.log("\nRobô " + robo2.getCor() + " :");
        screen.log("Movimentos válidos: " + movimentosValidos2);
        screen.log("Movimentos inválidos: " + movimentosInvalidos2);
    }

    @Override
    public String[][] getBoardState() {
        String[][] board = new String[4][4];
        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 4; x++) {
                // [CORREÇÃO]: Usando getPosicao().x() e y()
                boolean r1Aqui = (x == robo1.getPosicao().x() && y == robo1.getPosicao().y());
                boolean r2Aqui = (x == robo2.getPosicao().x() && y == robo2.getPosicao().y());

                if (x == foodX && y == foodY) {
                    board[x][y] = plano[x][y].getsimbolo(); // "🍇"
                } else if(r1Aqui && r2Aqui) {
                    board[x][y] = "⚔";
                } else if(r1Aqui) {
                    board[x][y] = robo1.getEmoji();
                } else if(r2Aqui) {
                    board[x][y] = robo2.getEmoji();
                } else {
                    board[x][y] = plano[x][y].getsimbolo();
                }
            }
        }
        return board;
    }
}