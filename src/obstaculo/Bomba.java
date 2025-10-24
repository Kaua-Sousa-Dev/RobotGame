package obstaculo;

import robo.Robo;

public class Bomba extends Obstaculo {
    public Bomba(int id){
        super(id);
    }

    @Override
    public AcaoResultado bater(Robo robo) {
        return AcaoResultado.EXPLODIR;
    }

    @Override
    public String getSimbolo() {
        return "💣";
    }
}