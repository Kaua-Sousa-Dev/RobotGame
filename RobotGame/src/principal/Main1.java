package principal;

import robo.Robo;
import grafico.Campo;
import excepts.MovimentoInvalidoException;
// [Importante] Configuração do jogo se tiver criado o arquivo no passo anterior
// import config.GameConfig;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main1 {

    // 1. Criar o Logger para substituir System.out em logs técnicos
    private static final Logger LOGGER = Logger.getLogger(Main1.class.getName());

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Inicializa o tabuleiro 4x4
        Campo[][] plano = new Campo[4][4];
        for (int i = 0; i < plano.length; i++) {
            for (int j = 0; j < plano[i].length; j++) {
                plano[i][j] = new Campo();
            }
        }

        String cor;
        while(true) {
            cor = sc.nextLine();
            if(cor.matches("[a-zA-Z]+"))
                break;
            else
                // UI: Feedback para o usuário
                imprimirMensagem("DIGITE NOVAMENTE, SÓ LETRAS POR FAVOR");
        }

        // Criando o Robô (assumindo que Robo foi refatorado para usar Coordenada)
        Robo robo = new Robo(cor);

        int xAlimento, yAlimento;
        do {
            imprimirMensagem("Digite a posição do alimento no eixo X: (valores de 0 a 3)");
            while (!sc.hasNextInt()) {
                imprimirMensagem("Entrada inválida! Digite um número inteiro para X:");
                sc.next();
            }
            xAlimento = sc.nextInt();

            imprimirMensagem("Digite a posição do alimento no eixo Y: (valores de 0 a 3)");
            while (!sc.hasNextInt()) {
                imprimirMensagem("Entrada inválida! Digite um número inteiro para y:");
                sc.next();
            }
            yAlimento = sc.nextInt();
            sc.nextLine();

            if(xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3)
                imprimirMensagem("Valores não permitidos, digite-os novamente.");

        } while (xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3);

        plano[xAlimento][yAlimento].setSimbolo("🍇");
        int finalX = xAlimento;
        int finalY = yAlimento;

        // Usa as variáveis finais dentro da lambda
        LOGGER.info(() -> "Alimento posicionado em: (" + finalX + "," + finalY + ")");

        // [CORREÇÃO DO ERRO]: Usando getPosicao().x() em vez de getX()
        // Se ainda não tiver getPosicao(), verifique sua classe Robo.
        imprimirMensagem("Posição do Robô: " + robo.getPosicao());

        boolean encontrouAlimento = false;

        while(!encontrouAlimento) {
            try {
                desenharPlano(plano, robo);
                imprimirMensagem("DIGITE O MOVIMENTO DO ROBÔ: (1-up, 2-down, 3-right, 4-left)");

                String movimento = sc.nextLine().trim();

                // O método mover(String) na classe Robo refatorada deve tratar a conversão
                robo.mover(movimento);

                // [CORREÇÃO]: Comparação usando Coordenada
                if (robo.getPosicao().x() == xAlimento && robo.getPosicao().y() == yAlimento) {
                    encontrouAlimento = true;
                    imprimirMensagem("O ROBÔ ENCONTROU O ALIMENTO 🍇");
                }

            } catch(MovimentoInvalidoException e) {
                // LOG: Erros de regra de negócio devem ir para o Logger
                // O usuario recebe feedback via mensagem da exceção, mas o registro é log
                LOGGER.log(Level.WARNING, "Tentativa de movimento inválido: {0}", e.getMessage());
                imprimirMensagem(e.getMessage()); // Feedback visual para o jogador
            }
        }

        sc.close();
    }

    // Método Wrapper para UI (Isola o System.out e suprime o alerta do Sonar)
    @SuppressWarnings("java:S106")
    private static void imprimirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    // Desenho do plano é UI pura, mantemos System.out com supressão
    @SuppressWarnings("java:S106")
    public static void desenharPlano(Campo[][] plano, Robo robo) {
        for (int y = 3; y >= 0; y--) {
            System.out.printf(" %d|", y);
            for (int x = 0; x < 4; x++) {
                // [CORREÇÃO]: Acessando coordenada via getPosicao()
                if (x == robo.getPosicao().x() && y == robo.getPosicao().y()) {
                    System.out.print(" " + robo.getEmoji() + " ");
                } else {
                    System.out.print(" " + plano[x][y].getsimbolo() + " "); //
                }
            }
            System.out.println();
        }
        System.out.println("    0  1  2  3");
    }
}