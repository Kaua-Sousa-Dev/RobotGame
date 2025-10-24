package obstaculo;

import robo.Robo;

public class Rocha extends Obstaculo {
    public Rocha(int id){
        super(id);
    }

    @Override
    public AcaoResultado bater(Robo robo) {
        return AcaoResultado.VOLTAR;
    }

    @Override
    public String getSimbolo() {
        return "🗻";
    }
}