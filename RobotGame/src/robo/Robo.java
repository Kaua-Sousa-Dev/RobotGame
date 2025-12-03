package robo;

import config.GameConfig; // Se tiver criado a config, senão pode remover
import excepts.MovimentoInvalidoException;
import model.Coordenada;
import model.Direcao;

public class Robo {

    // A única fonte de verdade sobre a posição
    protected Coordenada posicao;
    protected String cor;

    public Robo(String cor) {
        this.cor = cor;
        // Garante que inicia sempre em 0,0
        this.posicao = new Coordenada(0, 0);
    }

    // --- MÉTODOS DE MOVIMENTO ---

    public void mover(String movimento) throws MovimentoInvalidoException {
        // Converte texto/número para Direção
        Direcao direcao = Direcao.aPartirDeInput(movimento);
        if (direcao == null) {
            throw new MovimentoInvalidoException("Direção inválida: " + movimento);
        }

        // Calcula nova posição
        Coordenada novaPosicao = direcao.aplicarEm(this.posicao);

        // Validação de Limites (0 a 3)
        if (!GameConfig.isPosicaoValida(novaPosicao)) {
            throw new MovimentoInvalidoException("Movimento inválido (Fora dos limites)");
        }

        // Atualiza a posição oficial
        this.posicao = novaPosicao;
    }

    // Sobrecarga para manter compatibilidade com códigos que usam int
    public void mover(int movimento) throws MovimentoInvalidoException {
        this.mover(String.valueOf(movimento));
    }

    // --- SETTERS ---

    // Opção 1: Recebe objeto Coordenada (usado pelas Rochas)
    public void setPosicao(Coordenada novaPosicao) {
        this.posicao = novaPosicao;
    }

    // Opção 2: Recebe x, y e cria a Coordenada (usado na inicialização)
    public void setPosicao(int x, int y) {
        this.posicao = new Coordenada(x, y);
    }

    // --- GETTERS (Delegam para a Coordenada) ---

    public Coordenada getPosicao() {
        return posicao;
    }

    public int getX() {
        return posicao.x();
    }

    public int getY() {
        return posicao.y();
    }

    public String getCor() {
        return cor;
    }

    public String getEmoji() {
        return switch (cor.toLowerCase()) {
            case "vermelho" -> "👾";
            case "azul" -> "🎮";
            default -> "🤖";
        };
    }

    public boolean encontrarAlimento(int x, int y) {
        return this.posicao.x() == x && this.posicao.y() == y;
    }
}