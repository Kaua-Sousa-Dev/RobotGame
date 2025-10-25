package principal.engines;

import excepts.MovimentoInvalidoException;
import principal.GameScreen;
import robo.Robo;
import robo.RoboInteligente; //
import java.util.Random;

public class GameEngine3 extends GameEngineBase {

    private Robo roboNormal;
    private RoboInteligente roboInteligente;
    private Random r = new Random();

    private int movimentosValidos = 0;
    private int movimentosInvalidos = 0;
    private int movimentosInteligente = 0;

    private boolean faseRoboNormal = true; // Controla se estamos na fase 1
    private boolean encontrouAlimentoNormal = false;
    private boolean encontrouAlimentoInteligente = false;


    public GameEngine3(GameScreen screen) {
        super(screen);
    }

    @Override
    public void initialize() {
        screen.log("Modo 3: Robô Normal vs. Inteligente");
        this.roboNormal = new Robo("normal");
        this.roboInteligente = new RoboInteligente("inteligente");

        setupFood(); // Define a comida (ex: em 3,3)

        screen.log("ROBÔ NORMAL: 👾");
        screen.log("ROBÔ INTELIGENTE: 🤖");
        screen.log("\n========================= Vez de robô normal:====================================");
    }

    @Override
    public void update() {
        if (gameFinished) return;

        if (faseRoboNormal) {
            // --- Fase 1: Robô Normal ---
            try {
                int movimentoRoboNormal = r.nextInt(4) + 1;
                screen.log("\n Movimento " + movimentoRoboNormal);
                roboNormal.mover(movimentoRoboNormal);
                movimentosValidos++;

                if (roboNormal.encontrarAlimento(foodX, foodY)) {
                    screen.log("O ROBÔ NORMAL ENCONTROU O ALIMENTO ");
                    encontrouAlimentoNormal = true;
                    faseRoboNormal = false; // Passa para a próxima fase
                    screen.log("\n=========================Vez de robô inteligente:===============================");
                }
            } catch (MovimentoInvalidoException e) {
                screen.log(e.getMessage());
                movimentosInvalidos++;
            }

        } else {
            // --- Fase 2: Robô Inteligente ---
            int movimentoRoboInteligente = r.nextInt(4) + 1;
            screen.log("\n Movimento " + movimentoRoboInteligente);
            // RoboInteligente trata exceções internamente (lembra da refatoração?)
            roboInteligente.mover(movimentoRoboInteligente);
            movimentosInteligente++;

            if (roboInteligente.encontrarAlimento(foodX, foodY)) {
                screen.log("O ROBÔ INTELIGENTE ENCONTROU O ALIMENTO ");
                encontrouAlimentoInteligente = true;
                finalizarJogo();
            }
        }

        screen.atualizarGrid(); // Redesenha a tela
    }

    private void finalizarJogo() {
        gameFinished = true;
        screen.log("\n--- FIM DE JOGO ---");
        screen.log("\nRobô normal:");
        screen.log("Movimentos válidos: " + movimentosValidos);
        screen.log("Movimentos inválidos: " + movimentosInvalidos);
        screen.log("TOTAL DE MOVIMENTOS: " + (movimentosValidos + movimentosInvalidos));
        screen.log("\nRobô inteligente:");
        screen.log("TOTAL DE MOVIMENTOS:" + movimentosInteligente);
    }

    @Override
    public String[][] getBoardState() {
        String[][] board = new String[4][4];
        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 4; x++) {
                boolean r1Here = (x == roboNormal.getX() && y == roboNormal.getY() && !encontrouAlimentoNormal);
                boolean r2Here = (x == roboInteligente.getX() && y == roboInteligente.getY() && !encontrouAlimentoInteligente);

                if (x == foodX && y == foodY) {
                    board[x][y] = plano[x][y].getsimbolo(); // "🍇"
                } else if(r1Here && r2Here) {
                    board[x][y] = "⚔";
                } else if(r1Here) {
                    board[x][y] = "👾";
                } else if(r2Here) {
                    board[x][y] = "🤖";
                } else {
                    board[x][y] = plano[x][y].getsimbolo(); // " "
                }
            }
        }
        return board;
    }
}