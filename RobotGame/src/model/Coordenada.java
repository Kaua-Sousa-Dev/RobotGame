package model;

import java.util.Objects;

public class Coordenada {
    private final int x;
    private final int y;

    public Coordenada(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Lógica de movimento encapsulada na coordenada
    public Coordenada mover(int deltaX, int deltaY) {
        return new Coordenada(this.x + deltaX, this.y + deltaY);
    }

    public int x() { return x; }
    public int y() { return y; }

    // Facilita comparações (substitui if (r.x == x && r.y == y))
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordenada that = (Coordenada) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}