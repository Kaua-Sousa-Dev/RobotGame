package principal;

import excepts.MovimentoInvalidoException;
import robo.Robo;
import robo.RoboInteligente;
import obstaculo.Obstaculo;
import obstaculo.Bomba;
import obstaculo.Rocha;
import model.Coordenada;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main4 {

    static final int MAX = 3;

    private static String key(int x, int y) {
        return x + "," + y;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random r = new Random();

        imprimirMensagem("Tabuleiro 4x4 (coordenadas de 0 a 3).");
        imprimirMensagem("Robô normal: vermelho " + new Robo("vermelho").getEmoji());
        imprimirMensagem("Robô inteligente: azul " + new RoboInteligente("azul").getEmoji());
        imprimirMensagem("");

        int[] food = lerPosicao(sc, "alimento");
        // Correção: Variáveis declaradas em linhas separadas
        int foodX = food[0];
        int foodY = food[1];

        Map<String, Obstaculo> obstaculos = new HashMap<>();

        imprimirMensagem("Quantas bombas deseja inserir? ");
        int qBombas = lerInteiro(sc, 0, 16);

        for (int i = 1; i <= qBombas; i++) {
            int[] p;
            while (true) {
                imprimirMensagem("Posição da bomba " + i + " (x y):");
                p = lerPar(sc);
                if (validaPosicaoLivre(p[0], p[1], foodX, foodY, obstaculos)) break;
                imprimirMensagem("Posição inválida ou ocupada. Tente novamente.");
            }
            obstaculos.put(key(p[0], p[1]), new Bomba(i));
        }

        imprimirMensagem("Quantas rochas deseja inserir? ");
        int qRochas = lerInteiro(sc, 0, 16 - qBombas);

        for (int i = 1; i <= qRochas; i++) {
            int[] p;
            while (true) {
                imprimirMensagem("Posição da rocha " + i + " (x y):");
                p = lerPar(sc);
                if (validaPosicaoLivre(p[0], p[1], foodX, foodY, obstaculos)) break;
                imprimirMensagem("Posição inválida ou ocupada. Tente novamente.");
            }
            obstaculos.put(key(p[0], p[1]), new Rocha(i));
        }

        Robo roboNormal = new Robo("vermelho");
        RoboInteligente roboInteligente = new RoboInteligente("azul");

        // Correção: Variáveis declaradas em linhas separadas
        boolean explodiuNormal = false;
        boolean explodiuInteligente = false;
        int movesNormal = 0;
        int movesInteligente = 0;

        desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);

        while (true) {
            // --- Turno Robô Normal ---
            if (!explodiuNormal) {
                int oldX = roboNormal.getPosicao().x();
                int oldY = roboNormal.getPosicao().y();

                int m = r.nextInt(4) + 1;
                imprimirMensagem("\nVez do robô " + roboNormal.getCor() + " " + roboNormal.getEmoji() + " | Movimento " + m);

                try {
                    roboNormal.mover(String.valueOf(m));
                    movesNormal++;

                    if (alimento(roboNormal, foodX, foodY)) {
                        imprimirMensagem("O robô " + roboNormal.getCor() + " encontrou o alimento!");
                        desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);
                        break;
                    }

                    String keyPos = key(roboNormal.getPosicao().x(), roboNormal.getPosicao().y());
                    Obstaculo o = obstaculos.get(keyPos);

                    if (o != null) {
                        Obstaculo.AcaoResultado res = o.bater(roboNormal);
                        if (res == Obstaculo.AcaoResultado.EXPLODIR) {
                            explodiuNormal = true;
                            obstaculos.remove(keyPos);
                            imprimirMensagem("A bomba desapareceu do tabuleiro.");
                        } else if (res == Obstaculo.AcaoResultado.VOLTAR) {
                            roboNormal.setPosicao(new Coordenada(oldX, oldY));
                        }
                    }
                } catch (excepts.MovimentoInvalidoException e) {
                    imprimirMensagem(e.getMessage());
                }

                desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);
            }

            // Condição de término
            if (verificarFimDeJogo(explodiuNormal, explodiuInteligente, roboNormal, roboInteligente, foodX, foodY)) {
                break;
            }

            // --- Turno Robô Inteligente ---
            if (!explodiuInteligente) {
                int oldX = roboInteligente.getPosicao().x();
                int oldY = roboInteligente.getPosicao().y();

                int m = r.nextInt(4) + 1;
                imprimirMensagem("\nVez do robô " + roboInteligente.getCor() + " " + roboInteligente.getEmoji() + " | Movimento " + m);

                try{
                    roboNormal.mover(String.valueOf(m));
                } catch (MovimentoInvalidoException e){
                    System.out.println(e.getMessage());
                }
                movesInteligente++;

                if (alimento(roboInteligente, foodX, foodY)) {
                    imprimirMensagem("O robô " + roboInteligente.getCor() + " encontrou o alimento!");
                    desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);
                    break;
                }

                String keyPos = key(roboInteligente.getPosicao().x(), roboInteligente.getPosicao().y());
                Obstaculo o = obstaculos.get(keyPos);

                if (o != null) {
                    Obstaculo.AcaoResultado res = o.bater(roboInteligente);
                    if (res == Obstaculo.AcaoResultado.EXPLODIR) {
                        explodiuInteligente = true;
                        obstaculos.remove(keyPos);
                        imprimirMensagem("A bomba desapareceu do tabuleiro.");
                    } else if (res == Obstaculo.AcaoResultado.VOLTAR) {
                        roboInteligente.setPosicao(new Coordenada(oldX, oldY));
                    }
                }
                desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);
            }

            if (verificarFimDeJogo(explodiuNormal, explodiuInteligente, roboNormal, roboInteligente, foodX, foodY)) {
                break;
            }

            try {
                Thread.sleep(600);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

        imprimirMensagem("\nResumo:");
        imprimirMensagem("Robô " + roboNormal.getCor() + " movimentos: " + movesNormal + (explodiuNormal ? " (explodiu)" : ""));
        imprimirMensagem("Robô " + roboInteligente.getCor() + " movimentos: " + movesInteligente + (explodiuInteligente ? " (explodiu)" : ""));

        sc.close();
    }

    // Método Wrapper para isolar o System.out
    @SuppressWarnings("java:S106")
    private static void imprimirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    private static boolean verificarFimDeJogo(boolean exp1, boolean exp2, Robo r1, Robo r2, int fx, int fy) {
        if (exp1 && exp2) {
            imprimirMensagem("Ambos os robôs explodiram. Fim de jogo.");
            return true;
        }
        return alimento(r1, fx, fy) || alimento(r2, fx, fy);
    }

    private static boolean alimento(Robo r, int fx, int fy) {
        return r.getPosicao().x() == fx && r.getPosicao().y() == fy;
    }

    private static int[] lerPosicao(Scanner sc, String nome) {
        int x;
        int y;
        do {
            imprimirMensagem("Digite a posição do " + nome + " (x y) com valores de 0 a 3:");
            int[] par = lerPar(sc);
            x = par[0];
            y = par[1];
            if (x < 0 || y < 0 || x > MAX || y > MAX) {
                imprimirMensagem("Coordenadas fora dos limites. Tente novamente.");
            }
        } while (x < 0 || y < 0 || x > MAX || y > MAX);
        return new int[]{x, y};
    }

    private static int[] lerPar(Scanner sc) {
        while (true) {
            String linha = sc.nextLine().trim();
            String[] toks = linha.split("\\s+");
            if (toks.length == 2) {
                try {
                    int x = Integer.parseInt(toks[0]);
                    int y = Integer.parseInt(toks[1]);
                    return new int[]{x, y};
                } catch (NumberFormatException ignored) {
                    // Ignora erro de parsing
                }
            }
            imprimirMensagem("Entrada inválida. Informe dois inteiros separados por espaço.");
        }
    }

    private static int lerInteiro(Scanner sc, int min, int max) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {
                // Ignora erro
            }
            imprimirMensagem("Informe um inteiro entre " + min + " e " + max + ".");
        }
    }

    private static boolean validaPosicaoLivre(int x, int y, int fx, int fy, Map<String, Obstaculo> mapa) {
        return x >= 0 && y >= 0 && x <= MAX && y <= MAX
                && !(x == 0 && y == 0)
                && !(x == fx && y == fy)
                && !mapa.containsKey(key(x, y));
    }

    // Método desenhar também é UI, então anotamos para suprimir o aviso
    @SuppressWarnings("java:S106")
    private static void desenhar(Map<String, Obstaculo> obstaculos, Robo r1, Robo r2, int fx, int fy, boolean exp1, boolean exp2) {
        for (int y = MAX; y >= 0; y--) {
            System.out.printf(" %d|", y);
            for (int x = 0; x <= MAX; x++) {
                String cell = "  ";
                boolean r1Here = !exp1 && r1.getPosicao().x() == x && r1.getPosicao().y() == y;
                boolean r2Here = !exp2 && r2.getPosicao().x() == x && r2.getPosicao().y() == y;

                if (r1Here && r2Here) {
                    cell = "⚔️ ";
                } else if (r1Here) {
                    cell = r1.getEmoji() + " ";
                } else if (r2Here) {
                    cell = r2.getEmoji() + " ";
                } else if (x == fx && y == fy) {
                    cell = "🍇 ";
                } else {
                    Obstaculo o = obstaculos.get(key(x, y));
                    if (o != null) cell = o.getSimbolo() + " ";
                }
                System.out.print(" " + cell);
            }
            System.out.println();
        }
        System.out.println("    0   1   2   3");
    }
}