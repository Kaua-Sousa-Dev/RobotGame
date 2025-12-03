package principal.engines;

import excepts.MovimentoInvalidoException;
import principal.GameScreen;
import robo.Robo;
import robo.RoboInteligente;
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

                // [CORREÇÃO]: Convertendo int para String
                roboNormal.mover(String.valueOf(movimentoRoboNormal));

                movimentosValidos++;

                // [CORREÇÃO]: Usando getPosicao().x() e .y()
                if (roboNormal.getPosicao().x() == foodX && roboNormal.getPosicao().y() == foodY) {
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

            // [CORREÇÃO]: Convertendo para String e adicionando try-catch
            // (Mesmo que o Robô Inteligente trate internamente, o compilador exige se a assinatura do método diz que lança)
            try {
                roboInteligente.mover(String.valueOf(movimentoRoboInteligente));
            } catch (Exception e) {
                // Se o RoboInteligente for bem implementado, ele não deve lançar erro aqui,
                // mas capturamos para garantir.
                screen.log("Erro (Inteligente): " + e.getMessage());
            }

            movimentosInteligente++;

            // [CORREÇÃO]: Usando getPosicao().x() e .y()
            if (roboInteligente.getPosicao().x() == foodX && roboInteligente.getPosicao().y() == foodY) {
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
                // [CORREÇÃO]: Usando getPosicao()
                boolean r1Here = !encontrouAlimentoNormal &&
                        (x == roboNormal.getPosicao().x() && y == roboNormal.getPosicao().y());

                boolean r2Here = !encontrouAlimentoInteligente &&
                        (x == roboInteligente.getPosicao().x() && y == roboInteligente.getPosicao().y());

                if (x == foodX && y == foodY) {
                    board[x][y] = plano[x][y].getsimbolo(); // "🍇"
                } else if(r1Here && r2Here) {
                    board[x][y] = "⚔";
                } else if(r1Here) {
                    board[x][y] = roboNormal.getEmoji(); // ou "👾"
                } else if(r2Here) {
                    board[x][y] = roboInteligente.getEmoji(); // ou "🤖"
                } else {
                    board[x][y] = plano[x][y].getsimbolo(); // " "
                }
            }
        }
        return board;
    }
}