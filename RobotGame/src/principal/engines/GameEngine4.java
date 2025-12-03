package principal.engines;

import excepts.MovimentoInvalidoException;
import model.Coordenada;
import obstaculo.Bomba;
import obstaculo.Obstaculo;
import obstaculo.Rocha;
import principal.GameScreen;
import robo.Robo;
import robo.RoboInteligente;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameEngine4 extends GameEngineBase {

    private static final int MAX = 3;
    private Robo roboNormal;
    private RoboInteligente roboInteligente;
    private Map<String, Obstaculo> obstaculos;
    private final Random r = new Random();

    private boolean explodiuNormal = false;
    private boolean explodiuInteligente = false;
    private int movesNormal = 0;
    private int movesInteligente = 0;
    private boolean turnoRoboNormal = true;

    public GameEngine4(GameScreen screen) {
        super(screen);
    }

    @Override
    public void initialize() {
        // 1. Instancia Robôs
        roboNormal = new Robo("vermelho");
        roboInteligente = new RoboInteligente("azul");

        // 2. FORÇA a posição inicial (0,0) para garantir
        roboNormal.setPosicao(0, 0);
        roboInteligente.setPosicao(0, 0);

        screen.log("Robôs iniciados na posição (0,0).");
        screen.log("Normal: " + roboNormal.getEmoji() + " | Inteligente: " + roboInteligente.getEmoji());

        // 3. Configura Alimento
        Coordenada posAlimento = solicitarCoordenada("🍇 ALIMENTO", "Digite a posição do Alimento (x,y):");
        this.foodX = posAlimento.x();
        this.foodY = posAlimento.y();
        screen.log("Alimento definido em " + posAlimento);

        // 4. Configura Obstáculos
        obstaculos = new HashMap<>();

        // Bombas (Manual)
        for (int i = 1; i <= 2; i++) {
            adicionarObstaculoManual(i, new Bomba(i), "💣 BOMBA " + i);
        }

        // Rochas (Manual)
        for (int i = 1; i <= 2; i++) {
            adicionarObstaculoManual(i, new Rocha(i), "🗻 ROCHA " + i);
        }

        screen.log("Configuração concluída! O jogo vai começar.");
        screen.atualizarGrid(); // Atualiza a tela imediatamente para mostrar o estado inicial
    }

    private void adicionarObstaculoManual(int id, Obstaculo obstaculo, String titulo) {
        Coordenada coord;
        do {
            coord = solicitarCoordenada(titulo, "Digite a posição (x,y) livre:");
            if (!validaPosicaoLivre(coord.x(), coord.y())) {
                System.out.println("Posição inválida! (Ocupada, 0,0 ou Alimento)");
            } else {
                break;
            }
        } while (true);
        obstaculos.put(key(coord.x(), coord.y()), obstaculo);
        screen.log(obstaculo.getClass().getSimpleName() + " " + id + " em " + coord);
    }

    private Coordenada solicitarCoordenada(String titulo, String mensagem) {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Configuração");
            dialog.setHeaderText(titulo);
            dialog.setContentText(mensagem);
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                try {
                    String[] partes = result.get().trim().split("[,\\s]+");
                    if (partes.length == 2) {
                        int x = Integer.parseInt(partes[0]);
                        int y = Integer.parseInt(partes[1]);
                        if (x >= 0 && x <= MAX && y >= 0 && y <= MAX) {
                            return new Coordenada(x, y);
                        }
                    }
                } catch (Exception ignored) { }
            }
        }
    }

    private boolean validaPosicaoLivre(int x, int y) {
        // Impede colocar obstáculos no 0,0 (início), na comida ou onde já tem obstáculo
        return !(x == 0 && y == 0) && !(x == foodX && y == foodY) && !obstaculos.containsKey(key(x, y));
    }

    @Override
    public void update() {
        if (gameFinished) return;

        if (turnoRoboNormal) {
            if (!explodiuNormal) processarTurnoNormal();
        } else {
            if (!explodiuInteligente) processarTurnoInteligente();
        }

        verificarFimDeJogo();
        turnoRoboNormal = !turnoRoboNormal;
        screen.atualizarGrid();
    }

    private void processarTurnoNormal() {
        int oldX = roboNormal.getPosicao().x();
        int oldY = roboNormal.getPosicao().y();

        // Loop para garantir que o robô normal SEMPRE realize um movimento válido nesta rodada
        // Se ele sortear uma parede, ele tenta de novo imediatamente (senão ele fica "parado" perdendo o turno)
        boolean moveu = false;
        while (!moveu) {
            String m = String.valueOf(r.nextInt(4) + 1);
            try {
                roboNormal.mover(m);
                moveu = true; // Sucesso
                screen.log("\nVez do " + roboNormal.getCor() + " | Movimento " + m);
            } catch (MovimentoInvalidoException e) {
                // Falhou (parede), loop repete e tenta outro número
            }
        }

        movesNormal++;
        if (verificarAlimento(roboNormal)) return;
        processarColisao(roboNormal, oldX, oldY, true);
    }

    private void processarTurnoInteligente() {
        int oldX = roboInteligente.getPosicao().x();
        int oldY = roboInteligente.getPosicao().y();

        int m = r.nextInt(4) + 1;
        screen.log("\nVez do " + roboInteligente.getCor() + " | Movimento " + m);

        // O RoboInteligente já sabe retentar internamente
        try {
            roboInteligente.mover(String.valueOf(m));
        } catch (Exception e) { }

        movesInteligente++;
        if (verificarAlimento(roboInteligente)) return;
        processarColisao(roboInteligente, oldX, oldY, false);
    }

    // ... (Métodos verificarAlimento, processarColisao, verificarFimDeJogo, key e getBoardState permanecem iguais ao anterior) ...
    // Vou incluir aqui só o getBoardState para garantir que ele usa getPosicao()

    @Override
    public String[][] getBoardState() {
        String[][] board = new String[4][4];
        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 4; x++) {
                boolean r1Here = !explodiuNormal && roboNormal.getPosicao().x() == x && roboNormal.getPosicao().y() == y;
                boolean r2Here = !explodiuInteligente && roboInteligente.getPosicao().x() == x && roboInteligente.getPosicao().y() == y;

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

    // ... Métodos auxiliares de colisão e fim de jogo (ver código anterior) ...
    private boolean verificarAlimento(Robo robo) {
        if (robo.getPosicao().x() == foodX && robo.getPosicao().y() == foodY) {
            screen.log("O robô " + robo.getCor() + " encontrou o alimento!");
            gameFinished = true;
            return true;
        }
        return false;
    }

    private void processarColisao(Robo robo, int oldX, int oldY, boolean isNormal) {
        String keyPos = key(robo.getPosicao().x(), robo.getPosicao().y());
        Obstaculo o = obstaculos.get(keyPos);
        if (o != null) {
            Obstaculo.AcaoResultado res = o.bater(robo);
            if (res == Obstaculo.AcaoResultado.EXPLODIR) {
                screen.log("💥 Robô " + robo.getCor() + " explodiu!");
                obstaculos.remove(keyPos);
                if (isNormal) explodiuNormal = true; else explodiuInteligente = true;
            } else if (res == Obstaculo.AcaoResultado.VOLTAR) {
                screen.log("⛰️ Robô " + robo.getCor() + " bateu na rocha e voltou.");
                robo.setPosicao(new Coordenada(oldX, oldY));
            }
        }
    }

    private void verificarFimDeJogo() {
        if ((explodiuNormal && explodiuInteligente) || gameFinished) {
            gameFinished = true;
            screen.log("\n--- FIM DE JOGO ---");
            screen.log("Robô Normal: " + movesNormal + (explodiuNormal ? " (MORT)" : ""));
            screen.log("Robô Inteligente: " + movesInteligente + (explodiuInteligente ? " (MORT)" : ""));
        }
    }
    private String key(int x, int y) { return x + "," + y; }
}