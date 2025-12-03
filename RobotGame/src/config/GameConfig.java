package config;

import model.Coordenada;

public class GameConfig {
    public static final int TAMANHO_GRID = 4; // 0 a 3
    public static final int MAX_COORD = TAMANHO_GRID - 1;

    public static boolean isPosicaoValida(Coordenada c) {
        return c.x() >= 0 && c.x() <= MAX_COORD &&
                c.y() >= 0 && c.y() <= MAX_COORD;
    }
}