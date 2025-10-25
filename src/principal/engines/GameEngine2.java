package principal.engines;

import excepts.MovimentoInvalidoException;
import principal.GameScreen;
import robo.Robo;
import java.util.Random;

public class GameEngine2 extends GameEngineBase {

    private Robo robo1;
    private Robo robo2;
    private Random r = new Random();

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
        // Em vez de usar Scanner, definimos os valores fixos
        screen.log("Modo 2: 2 Robôs Aleatórios");
        this.robo1 = new Robo("vermelho");
        this.robo2 = new Robo("azul");

        setupFood(); // Define a comida (ex: em 3,3)

        screen.log("Robô " + robo1.getCor() + " criado: " + robo1.getEmoji());
        screen.log("Robô " + robo2.getCor() + " criado: " + robo2.getEmoji());
    }

    @Override
    public void update() {
        // Esta função é chamada 1x por segundo pelo AnimationTimer
        if (gameFinished) return;

        if (turnoRobo1) {
            // --- Vez do Robô 1 ---
            screen.log("\nVez de robô " + robo1.getCor() + ":");
            int movimentoRobo1 = r.nextInt(4) + 1;
            screen.log(" Movimento " + movimentoRobo1);
            try {
                robo1.mover(movimentoRobo1); // (Lembre-se: mover() refatorado não imprime)
                movimentosValidos1++;

                if (robo1.encontrarAlimento(foodX, foodY)) {
                    screen.log("O ROBÔ " + robo1.getCor() + " ENCONTROU O ALIMENTO ");
                    finalizarJogo();
                }
            } catch (MovimentoInvalidoException e) {
                screen.log(e.getMessage());
                movimentosInvalidos1++;
            }
        } else {
            // --- Vez do Robô 2 ---
            screen.log("\nVez de robô " + robo2.getCor() + ":");
            int movimentoRobo2 = r.nextInt(4) + 1;
            screen.log(" Movimento " + movimentoRobo2);
            try {
                robo2.mover(movimentoRobo2);
                movimentosValidos2++;

                if (robo2.encontrarAlimento(foodX, foodY)) {
                    screen.log("O ROBÔ " + robo2.getCor() + " ENCONTROU O ALIMENTO ");
                    finalizarJogo();
                }
            } catch (MovimentoInvalidoException e) {
                screen.log(e.getMessage());
                movimentosInvalidos2++;
            }
        }

        turnoRobo1 = !turnoRobo1; // Troca o turno
        screen.atualizarGrid(); // Redesenha a tela
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
                boolean fatoRobo1 = (x == robo1.getX() && y == robo1.getY());
                boolean fatoRobo2 = (x == robo2.getX() && y == robo2.getY());

                if (x == foodX && y == foodY) {
                    board[x][y] = plano[x][y].getsimbolo(); // "🍇"
                } else if(fatoRobo1 && fatoRobo2) {
                    board[x][y] = "⚔";
                } else if(fatoRobo1) {
                    board[x][y] = robo1.getEmoji();
                } else if(fatoRobo2) {
                    board[x][y] = robo2.getEmoji();
                } else {
                    board[x][y] = plano[x][y].getsimbolo();
                }
            }
        }
        return board;
    }
}