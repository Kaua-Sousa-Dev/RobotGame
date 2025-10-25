package principal.engines;

import excepts.MovimentoInvalidoException;
import obstaculo.Bomba;
import obstaculo.Obstaculo;
import obstaculo.Rocha;
import principal.GameScreen;
import robo.Robo;
import robo.RoboInteligente;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameEngine4 extends GameEngineBase {

    private static final int MAX = 3;

    private Robo roboNormal;
    private RoboInteligente roboInteligente;
    private Map<String, Obstaculo> obstaculos;
    private Random r = new Random();

    private boolean explodiuNormal = false;
    private boolean explodiuInteligente = false;
    private int movesNormal = 0;
    private int movesInteligente = 0;
    private boolean turnoRoboNormal = true; // Controla o turno

    public GameEngine4(GameScreen screen) {
        super(screen);
    }

    @Override
    public void initialize() {
        setupFood(); // Comida em (3,3)

        // Cria robôs
        roboNormal = new Robo("vermelho");
        roboInteligente = new RoboInteligente("azul");
        screen.log("Robô normal: " + roboNormal.getEmoji() + " | Robô inteligente: " + roboInteligente.getEmoji());

        obstaculos = new HashMap<>();
        int qBombas = 2;
        int qRochas = 2;
        screen.log("Obstáculos criados!");

        for (int i = 1; i <= qBombas; i++) {
            int x, y;
            // Continua gerando (x, y) aleatório ATÉ encontrar uma posição livre
            do {
                x = r.nextInt(MAX + 1); // Gera um número de 0 a 3
                y = r.nextInt(MAX + 1); // Gera um número de 0 a 3
            } while (!validaPosicaoLivre(x, y));

            // Posição válida encontrada! Adiciona a bomba.
            obstaculos.put(key(x, y), new Bomba(i));
            screen.log("Bomba " + i + " criada em (" + x + "," + y + ")");
        }
        screen.log("Gerando " + qRochas + " rochas aleatórias...");
        for (int i = 1; i <= qRochas; i++) {
            int x, y;
            do {
                x = r.nextInt(MAX + 1);
                y = r.nextInt(MAX + 1);
            } while (!validaPosicaoLivre(x, y));

            // Posição válida encontrada! Adiciona a rocha.
            obstaculos.put(key(x, y), new Rocha(i));
            screen.log("Rocha " + i + " criada em (" + x + "," + y + ")");
        }
    }

    private boolean validaPosicaoLivre(int x, int y) {
        // 1. Verifica se está dentro dos limites (0 a 3)
        boolean estaNosLimites = (x >= 0 && y >= 0 && x <= MAX && y <= MAX);

        // 2. Verifica se está na posição inicial dos robôs (0,0)
        boolean estaNoInicio = (x == 0 && y == 0);

        // 3. Verifica se está em cima da comida (foodX e foodY vêm do GameEngineBase)
        boolean estaNaComida = (x == foodX && y == foodY);

        // 4. Verifica se já existe um obstáculo nesse local
        boolean estaOcupado = obstaculos.containsKey(key(x, y));

        // A posição só é livre se estiver nos limites E NÃO estiver no início,
        // E NÃO estiver na comida E NÃO estiver ocupada.
        return estaNosLimites && !estaNoInicio && !estaNaComida && !estaOcupado;
    }

    @Override
    public void update() {
        // Roda um turno do jogo, chamado pelo AnimationTimer

        if (turnoRoboNormal && !explodiuNormal) {
            // --- Vez do Robô Normal ---
            int oldX = roboNormal.getX(), oldY = roboNormal.getY();
            int m = r.nextInt(4) + 1;
            screen.log("\nVez do " + roboNormal.getCor() + " | Movimento " + m);
            try {
                roboNormal.mover(m);
                movesNormal++;

                if (roboNormal.encontrarAlimento(foodX, foodY)) {
                    screen.log("O robô " + roboNormal.getCor() + " encontrou o alimento!");
                    gameFinished = true;
                }

                // Checa colisão
                Obstaculo o = obstaculos.get(key(roboNormal.getX(), roboNormal.getY()));
                if (o != null) {
                    Obstaculo.AcaoResultado res = o.bater(roboNormal); // Lembre-se: bater() refatorado não imprime
                    if (res == Obstaculo.AcaoResultado.EXPLODIR) {
                        screen.log("💥 Robô " + roboNormal.getCor() + " explodiu!");
                        explodiuNormal = true;
                        obstaculos.remove(key(roboNormal.getX(), roboNormal.getY()));
                    } else if (res == Obstaculo.AcaoResultado.VOLTAR) {
                        screen.log("⛰️ Robô " + roboNormal.getCor() + " bateu na rocha e voltou.");
                        roboNormal.setPosicao(oldX, oldY);
                    }
                }
            } catch (MovimentoInvalidoException e) {
                screen.log(e.getMessage());
            }

        } else if (!turnoRoboNormal && !explodiuInteligente) {
            // --- Vez do Robô Inteligente ---
            int oldX = roboInteligente.getX(), oldY = roboInteligente.getY();
            int m = r.nextInt(4) + 1;
            screen.log("\nVez do " + roboInteligente.getCor() + " | Movimento " + m);

            // Lógica do Robô Inteligente (ele trata exceções internamente)
            roboInteligente.mover(m); // Lembre-se: mover() refatorado não imprime
            movesInteligente++;

            if (roboInteligente.encontrarAlimento(foodX, foodY)) {
                screen.log("O robô " + roboInteligente.getCor() + " encontrou o alimento!");
                gameFinished = true;
            }

            Obstaculo o = obstaculos.get(key(roboInteligente.getX(), roboInteligente.getY()));
            if (o != null) {
                Obstaculo.AcaoResultado res = o.bater(roboInteligente);
                if (res == Obstaculo.AcaoResultado.EXPLODIR) {
                    screen.log("💥 Robô " + roboInteligente.getCor() + " explodiu!");
                    explodiuInteligente = true;
                    obstaculos.remove(key(roboInteligente.getX(), roboInteligente.getY()));
                } else if (res == Obstaculo.AcaoResultado.VOLTAR) {
                    screen.log("⛰️ Robô " + roboInteligente.getCor() + " bateu na rocha e voltou.");
                    roboInteligente.setPosicao(oldX, oldY);
                }
            }
        }

        turnoRoboNormal = !turnoRoboNormal; // Troca o turno
        screen.atualizarGrid(); // Redesenha

        if ((explodiuNormal && explodiuInteligente) || gameFinished) {
            gameFinished = true;
            screen.log("\n--- FIM DE JOGO ---");
            screen.log("Robô " + roboNormal.getCor() + ": " + movesNormal + " movs" + (explodiuNormal ? " (explodiu)" : ""));
            screen.log("Robô " + roboInteligente.getCor() + ": " + movesInteligente + " movs" + (explodiuInteligente ? " (explodiu)" : ""));
        }
    }

    private String key(int x, int y) { return x + "," + y; }

    @Override
    public String[][] getBoardState() {
        String[][] board = new String[4][4];
        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 4; x++) {
                boolean r1Here = !explodiuNormal && roboNormal.getX() == x && roboNormal.getY() == y;
                boolean r2Here = !explodiuInteligente && roboInteligente.getX() == x && roboInteligente.getY() == y;

                if (r1Here && r2Here) board[x][y] = "⚔";
                else if (r1Here) board[x][y] = roboNormal.getEmoji();
                else if (r2Here) board[x][y] = roboInteligente.getEmoji();
                else if (x == foodX && y == foodY) board[x][y] = "🍇";
                else {
                    Obstaculo o = obstaculos.get(key(x,y));
                    board[x][y] = (o != null) ? o.getSimbolo() : plano[x][y].getsimbolo();
                }
            }
        }
        return board;
    }
}