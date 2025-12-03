package principal;

import robo.Robo;
import grafico.Campo;
import excepts.MovimentoInvalidoException;
// Certifique-se de importar suas novas classes de modelo/config
// import config.GameConfig;
// import model.Coordenada;

import java.util.Scanner;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main2 {

    private static final Logger LOGGER = Logger.getLogger(Main2.class.getName());
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

        imprimirMensagem("OLÁ, PARA COMEÇAR, DIGITE A COR DO 1° ROBÔ:");
        Robo robo1 = criarRobo(sc);

        imprimirMensagem("DIGITE A COR DO 2° ROBÔ:");
        Robo robo2 = criarRobo(sc);

        // Posição do Alimento
        int xAlimento, yAlimento;
        do {
            xAlimento = lerInteiro(sc, "Digite a posição do alimento no eixo X (0 a 3):");
            yAlimento = lerInteiro(sc, "Digite a posição do alimento no eixo Y (0 a 3):");

            if(xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3)
                imprimirMensagem("Valores não permitidos (use 0 a 3).");

        } while (xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3);

        plano[xAlimento][yAlimento].setSimbolo("🍇");

        // Logs técnicos com variáveis efetivamente finais
        int finalX = xAlimento;
        int finalY = yAlimento;
        LOGGER.info(() -> "Alimento definido em (" + finalX + "," + finalY + ")");

        imprimirMensagem("Posição do Robô " + robo1.getCor() + ": " + robo1.getPosicao());
        imprimirMensagem("Posição do Robô " + robo2.getCor() + ": " + robo2.getPosicao());

        imprimirMensagem("Informações salvas! Aperte enter para continuar.");
        sc.nextLine(); // Consome o enter

        boolean encontrouAlimento = false;

        // Loop principal do jogo
        while(!encontrouAlimento) {
            desenharPlano(plano, robo1, robo2);
            pausar(1000);

            // Turno do Robô 1
            if (realizarTurno(robo1, xAlimento, yAlimento)) {
                break;
            }

            pausar(2000);
            desenharPlano(plano, robo1, robo2);
            pausar(1000);

            // Turno do Robô 2
            if (realizarTurno(robo2, xAlimento, yAlimento)) {
                break;
            }

            pausar(2000);
        }

        // [Melhoria]: Poderíamos ter contadores de movimento dentro da classe Robo para exibir no final
        imprimirMensagem("\nFim de jogo!");
        sc.close();
    }

    // Método extraído para evitar duplicação de lógica entre os robôs
    private static boolean realizarTurno(Robo robo, int xAlimento, int yAlimento) {
        imprimirMensagem("\nVez de robô " + robo.getCor() + ":");

        // Gera movimento aleatório de 1 a 4
        int movimentoSorteado = random.nextInt(4) + 1;
        imprimirMensagem(" Movimento sorteado: " + movimentoSorteado);

        try {
            // [CORREÇÃO]: Converte int para String, pois o novo mover() espera String ou Direcao
            robo.mover(String.valueOf(movimentoSorteado));

            // [CORREÇÃO]: Verifica colisão usando Coordenada
            if (robo.getPosicao().x() == xAlimento && robo.getPosicao().y() == yAlimento) {
                imprimirMensagem("O ROBÔ " + robo.getCor().toUpperCase() + " ENCONTROU O ALIMENTO! 🍇");
                return true; // Encontrou
            }
        } catch (MovimentoInvalidoException e) {
            // Loga o erro mas segue o jogo (comportamento original do Main2)
            LOGGER.log(Level.WARNING, "Movimento inválido do robô {0}: {1}", new Object[]{robo.getCor(), e.getMessage()});
            imprimirMensagem(e.getMessage());
        }
        return false;
    }

    // Método auxiliar para criar robô e validar entrada
    private static Robo criarRobo(Scanner sc) {
        while(true) {
            String cor = sc.nextLine();
            if(cor.matches("[a-zA-Z]+")) {
                return new Robo(cor);
            }
            imprimirMensagem("DIGITE NOVAMENTE, SÓ LETRAS POR FAVOR.");
        }
    }

    private static int lerInteiro(Scanner sc, String mensagem) {
        imprimirMensagem(mensagem);
        while (!sc.hasNextInt()) {
            imprimirMensagem("Entrada inválida! Digite um número inteiro:");
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine(); // Consome quebra de linha
        return valor;
    }

    @SuppressWarnings("java:S106")
    public static void desenharPlano(Campo[][] plano, Robo robo1, Robo robo2) {
        for (int y = 3; y >= 0; y--) {
            System.out.printf(" %d|", y);
            for (int x = 0; x < 4; x++) {
                // [CORREÇÃO]: Uso de .getPosicao() e .equals()
                boolean r1Aqui = (x == robo1.getPosicao().x() && y == robo1.getPosicao().y());
                boolean r2Aqui = (x == robo2.getPosicao().x() && y == robo2.getPosicao().y());

                if(r1Aqui && r2Aqui)
                    System.out.print(" ⚔️ ");
                else if(r1Aqui)
                    System.out.print(" " + robo1.getEmoji() + " ");
                else if(r2Aqui)
                    System.out.print(" " + robo2.getEmoji() + " ");
                else
                    System.out.print(" " + plano[x][y].getsimbolo() + " ");
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
            LOGGER.log(Level.SEVERE, "Thread interrompida", e);
        }
    }
}