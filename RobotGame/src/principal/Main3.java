package principal;

import robo.Robo;
import robo.RoboInteligente;
import grafico.Campo;
import excepts.MovimentoInvalidoException;
// import model.Coordenada; // Se necessário

import java.util.Scanner;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main3 {

    private static final Logger LOGGER = Logger.getLogger(Main3.class.getName());
    private static final Random random = new Random();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Inicializa Tabuleiro
        Campo[][] plano = new Campo[4][4];
        for (int i = 0; i < plano.length; i++) {
            for (int j = 0; j < plano[i].length; j++) {
                plano[i][j] = new Campo();
            }
        }

        Robo roboNormal = new Robo("normal");
        RoboInteligente roboInteligente = new RoboInteligente("inteligente");

        imprimirMensagem("ROBÔ NORMAL: 👾");
        imprimirMensagem("ROBÔ INTELIGENTE: 🤖");

        // Input do Alimento
        int xAlimento;
        int yAlimento;
        do {
            xAlimento = lerInteiro(sc, "Digite a posição do alimento no eixo X (0 a 3):");
            yAlimento = lerInteiro(sc, "Digite a posição do alimento no eixo Y (0 a 3):");

            if(xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3)
                imprimirMensagem("Valores não permitidos, digite-os novamente.");
        } while (xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3);

        plano[xAlimento][yAlimento].setSimbolo("🍇");

        // Logs técnicos
        int finalX = xAlimento;
        int finalY = yAlimento;
        LOGGER.info(() -> "Alimento em (" + finalX + "," + finalY + ")");

        // [CORREÇÃO]: getPosicao() em vez de getX()/getY()
        imprimirMensagem("Posição do alimento: (" + xAlimento + "," + yAlimento + ")");
        imprimirMensagem("\nPosição do Robô normal: " + roboNormal.getPosicao());

        // --- Robô Normal ---
        boolean encontrouAlimento = false;
        int movimentosValidos = 0;
        int movimentosInvalidos = 0;

        imprimirMensagem("\n========================= Vez de robô normal:====================================");

        while(!encontrouAlimento) {
            desenharPlano(plano, roboNormal, 1);
            pausar(2000);
            try {
                int movimento = random.nextInt(4) + 1;
                imprimirMensagem("\n Movimento " + movimento);

                // [CORREÇÃO]: mover(String)
                roboNormal.mover(String.valueOf(movimento));

                movimentosValidos++;
                // [CORREÇÃO]: validação com getPosicao()
                if (roboNormal.getPosicao().x() == xAlimento && roboNormal.getPosicao().y() == yAlimento) {
                    encontrouAlimento = true;
                    imprimirMensagem("O ROBÔ NORMAL ENCONTROU O ALIMENTO ");
                    break;
                }
            } catch(MovimentoInvalidoException e) {
                pausar(1000);
                imprimirMensagem(e.getMessage());
                movimentosInvalidos++;
            }
        }

        // --- Robô Inteligente ---
        pausar(3000);
        imprimirMensagem("\n=========================Vez de robô inteligente:===============================");
        // [CORREÇÃO]: getPosicao()
        imprimirMensagem("Posição do Robô inteligente: " + roboInteligente.getPosicao() + "\n");

        boolean encontrouAlimento2 = false;
        int movimentos = 0;

        while(!encontrouAlimento2) {
            desenharPlano(plano, roboInteligente, 2);
            pausar(2000);

            int movimento = random.nextInt(4) + 1;
            imprimirMensagem("\n Movimento " + movimento);

            // [CORREÇÃO]: Adicionado try-catch, pois agora mover() lança exceção
            try {
                roboInteligente.mover(String.valueOf(movimento));
            } catch (Exception e) {
                // Mesmo sendo inteligente, o método mover(String) do pai lança exceção.
                // A menos que RoboInteligente tenha sido refatorado para tratar isso internamente.
                imprimirMensagem("Movimento inválido: " + e.getMessage());
            }

            movimentos++;

            // [CORREÇÃO]: validação com getPosicao()
            if (roboInteligente.getPosicao().x() == xAlimento && roboInteligente.getPosicao().y() == yAlimento) {
                encontrouAlimento2 = true;
                imprimirMensagem("O ROBÔ INTELIGENTE ENCONTROU O ALIMENTO ");
                break;
            }
        }

        imprimirMensagem("\nRobô normal:");
        imprimirMensagem("Movimentos válidos: " + movimentosValidos);
        imprimirMensagem("Movimentos inválidos: " + movimentosInvalidos);
        imprimirMensagem("TOTAL DE MOVIMENTOS: " + (movimentosValidos + movimentosInvalidos));

        imprimirMensagem("\nRobô inteligente:");
        imprimirMensagem("TOTAL DE MOVIMENTOS:" + movimentos);

        sc.close();
    }

    // Método auxiliar para leitura de inteiros
    private static int lerInteiro(Scanner sc, String msg) {
        imprimirMensagem(msg);
        while (!sc.hasNextInt()) {
            imprimirMensagem("Entrada inválida! Digite um número inteiro:");
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }

    @SuppressWarnings("java:S106")
    public static void desenharPlano(Campo[][] plano, Robo robo, int tipo) {
        for (int y = 3; y >= 0; y--) {
            System.out.printf(" %d|", y);
            for (int x = 0; x < 4; x++) {
                // [CORREÇÃO]: getPosicao()
                if (x == robo.getPosicao().x() && y == robo.getPosicao().y()) {
                    if(tipo == 1)
                        System.out.print(" 👾 " );
                    else
                        System.out.print(" 🤖 " );
                } else {
                    System.out.print(" " + plano[x][y].getsimbolo() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("    0  1  2  3");
    }

    @SuppressWarnings("java:S106")
    private static void imprimirMensagem(String msg) {
        System.out.println(msg);
    }

    public static void pausar(int tempo) {
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}