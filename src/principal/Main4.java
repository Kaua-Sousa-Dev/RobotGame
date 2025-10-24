package principal;

import robo.Robo;
import robo.RoboInteligente;
import obstaculo.Obstaculo;
import obstaculo.Bomba;
import obstaculo.Rocha;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main4 {

    static final int MAX = 3; // limites 0..3 conforme Robo

    private static String key(int x, int y) {
        return x + "," + y;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random r = new Random();

        System.out.println("Tabuleiro 4x4 (coordenadas de 0 a 3).");
        System.out.println("Robô normal: vermelho " + new Robo("vermelho").getEmoji());
        System.out.println("Robô inteligente: azul " + new RoboInteligente("azul").getEmoji());
        System.out.println();

        int[] food = lerPosicao(sc, "alimento");
        int foodX = food[0], foodY = food[1];

        // Inserção de obstáculos
        Map<String, Obstaculo> obstaculos = new HashMap<>();

        System.out.println("Quantas bombas deseja inserir? ");
        int qBombas = lerInteiro(sc, 0, 16);
        for (int i = 1; i <= qBombas; i++) {
            int[] p;
            while (true) {
                System.out.println("Posição da bomba " + i + " (x y):");
                p = lerPar(sc);
                if (validaPosicaoLivre(p[0], p[1], foodX, foodY, obstaculos)) break;
                System.out.println("Posição inválida ou ocupada. Tente novamente.");
            }
            obstaculos.put(key(p[0], p[1]), new Bomba(i));
        }

        System.out.println("Quantas rochas deseja inserir? ");
        int qRochas = lerInteiro(sc, 0, 16 - qBombas);
        for (int i = 1; i <= qRochas; i++) {
            int[] p;
            while (true) {
                System.out.println("Posição da rocha " + i + " (x y):");
                p = lerPar(sc);
                if (validaPosicaoLivre(p[0], p[1], foodX, foodY, obstaculos)) break;
                System.out.println("Posição inválida ou ocupada. Tente novamente.");
            }
            obstaculos.put(key(p[0], p[1]), new Rocha(i));
        }

        Robo roboNormal = new Robo("vermelho");
        RoboInteligente roboInteligente = new RoboInteligente("azul");

        boolean explodiuNormal = false, explodiuInteligente = false;
        int movesNormal = 0, movesInteligente = 0;

        desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);

        while (true) {
            // Robô normal
            if (!explodiuNormal) {
                int oldX = roboNormal.getX(), oldY = roboNormal.getY();
                int m = r.nextInt(4) + 1;
                System.out.println("\nVez do robô " + roboNormal.getCor() + " " + roboNormal.getEmoji() + " | Movimento " + m);
                try {
                    roboNormal.mover(m);
                    movesNormal++;
                    if (alimento(roboNormal, foodX, foodY)) {
                        System.out.println("O robô " + roboNormal.getCor() + " encontrou o alimento!");
                        desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);
                        break;
                    }
                    Obstaculo o = obstaculos.get(key(roboNormal.getX(), roboNormal.getY()));
                    if (o != null) {
                        Obstaculo.AcaoResultado res = o.bater(roboNormal);
                        if (res == Obstaculo.AcaoResultado.EXPLODIR) {
                            explodiuNormal = true;
                            obstaculos.remove(key(roboNormal.getX(), roboNormal.getY())); // bomba desaparece
                            System.out.println("A bomba desapareceu do tabuleiro.");
                        } else if (res == Obstaculo.AcaoResultado.VOLTAR) {
                            roboNormal.setPosicao(oldX, oldY);
                        }
                    }
                } catch (excepts.MovimentoInvalidoException e) {
                    System.out.println(e.getMessage());
                }
                desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);
            }

            // Condição de término
            if (alimento(roboInteligente, foodX, foodY) || alimento(roboNormal, foodX, foodY) || (explodiuNormal && explodiuInteligente)) {
                if (explodiuNormal && explodiuInteligente) {
                    System.out.println("Ambos os robôs explodiram. Fim de jogo.");
                }
                break;
            }

            // Robô inteligente
            if (!explodiuInteligente) {
                int oldX = roboInteligente.getX(), oldY = roboInteligente.getY();
                int m = r.nextInt(4) + 1;
                System.out.println("\nVez do robô " + roboInteligente.getCor() + " " + roboInteligente.getEmoji() + " | Movimento " + m);
                roboInteligente.mover(m); // tenta outro se inválido internamente
                movesInteligente++;
                if (alimento(roboInteligente, foodX, foodY)) {
                    System.out.println("O robô " + roboInteligente.getCor() + " encontrou o alimento!");
                    desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);
                    break;
                }
                Obstaculo o = obstaculos.get(key(roboInteligente.getX(), roboInteligente.getY()));
                if (o != null) {
                    Obstaculo.AcaoResultado res = o.bater(roboInteligente);
                    if (res == Obstaculo.AcaoResultado.EXPLODIR) {
                        explodiuInteligente = true;
                        obstaculos.remove(key(roboInteligente.getX(), roboInteligente.getY())); // bomba desaparece
                        System.out.println("A bomba desapareceu do tabuleiro.");
                    } else if (res == Obstaculo.AcaoResultado.VOLTAR) {
                        roboInteligente.setPosicao(oldX, oldY);
                    }
                }
                desenhar(obstaculos, roboNormal, roboInteligente, foodX, foodY, explodiuNormal, explodiuInteligente);
            }

            if (alimento(roboInteligente, foodX, foodY) || alimento(roboNormal, foodX, foodY) || (explodiuNormal && explodiuInteligente)) {
                if (explodiuNormal && explodiuInteligente) {
                    System.out.println("Ambos os robôs explodiram. Fim de jogo.");
                }
                break;
            }
            // Delay
            try {
                Thread.sleep(600); // 0,6s
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\nResumo:");
        System.out.println("Robô " + roboNormal.getCor() + " movimentos: " + movesNormal + (explodiuNormal ? " (explodiu)" : ""));
        System.out.println("Robô " + roboInteligente.getCor() + " movimentos: " + movesInteligente + (explodiuInteligente ? " (explodiu)" : ""));

        sc.close();
    }

    private static boolean alimento(Robo r, int fx, int fy) {
        return r.getX() == fx && r.getY() == fy;
    }

    private static int[] lerPosicao(Scanner sc, String nome) {
        int x, y;
        do {
            System.out.println("Digite a posição do " + nome + " (x y) com valores de 0 a 3:");
            int[] par = lerPar(sc);
            x = par[0];
            y = par[1];
            if (x < 0 || y < 0 || x > MAX || y > MAX) {
                System.out.println("Coordenadas fora dos limites. Tente novamente.");
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
                }
            }
            System.out.println("Entrada inválida. Informe dois inteiros separados por espaço.");
        }
    }

    private static int lerInteiro(Scanner sc, int min, int max) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Informe um inteiro entre " + min + " e " + max + ".");
        }
    }

    private static boolean validaPosicaoLivre(int x, int y, int fx, int fy, Map<String, Obstaculo> mapa) {
        return x >= 0 && y >= 0 && x <= MAX && y <= MAX
                && !(x == 0 && y == 0) // não sobre o início dos robôs
                && !(x == fx && y == fy)
                && !mapa.containsKey(key(x, y));
    }

    private static void desenhar(Map<String, Obstaculo> obstaculos, Robo r1, Robo r2, int fx, int fy, boolean exp1, boolean exp2) {
        for (int y = MAX; y >= 0; y--) {
            System.out.printf(" %d|", y);
            for (int x = 0; x <= MAX; x++) {
                String cell = "  ";
                boolean r1Here = !exp1 && r1.getX() == x && r1.getY() == y;
                boolean r2Here = !exp2 && r2.getX() == x && r2.getY() == y;
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
