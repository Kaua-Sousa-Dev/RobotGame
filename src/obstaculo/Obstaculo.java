package obstaculo;

import robo.Robo;

public abstract class Obstaculo {
    protected int id;

    public enum AcaoResultado {
        EXPLODIR, VOLTAR, NENHUM
    }

    public Obstaculo(int id){
        this.id = id;
    }

    public int getId(){ return this.id; }

    public abstract AcaoResultado bater(Robo robo);

    public abstract String getSimbolo();
}