package model;

import java.util.Arrays;

public enum Direcao {
    CIMA(0, 1),
    BAIXO(0, -1),
    DIREITA(1, 0),
    ESQUERDA(-1, 0);

    private final int dx;
    private final int dy;

    Direcao(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    // Calcula a próxima coordenada baseada na direção
    public Coordenada aplicarEm(Coordenada atual) {
        return atual.mover(dx, dy);
    }

    // Factory method para converter input do usuário
    public static Direcao aPartirDeInput(String input) {
        if (input == null) return null;
        String limpo = input.trim().toLowerCase();

        // Mapeia números ou texto
        return switch (limpo) {
            case "1", "up", "cima" -> CIMA;
            case "2", "down", "baixo" -> BAIXO;
            case "3", "right", "direita" -> DIREITA;
            case "4", "left", "esquerda" -> ESQUERDA;
            default -> null;
        };
    }
}