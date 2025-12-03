package robo;

import excepts.MovimentoInvalidoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoboInteligente extends Robo {

    private final Random random = new Random();

    public RoboInteligente(String cor) {
        super(cor);
    }

    // Sobrescreve o mover(String) para adicionar a inteligência
    @Override
    public void mover(String movimento) {
        try {
            super.mover(movimento); // Tenta o movimento principal
        } catch (MovimentoInvalidoException e) {
            // Se bater, chama a estratégia de tentar outro caminho
            tentarOutroMovimento(movimento);
        }
    }

    // Mantemos o mover(int) para compatibilidade, delegando para a versão String
    @Override
    public void mover(int movimento) {
        this.mover(String.valueOf(movimento));
    }

    private void tentarOutroMovimento(String movimentoFalho) {
        // Lista de movimentos possíveis (1=up, 2=down, 3=right, 4=left)
        List<String> tentativas = new ArrayList<>();
        tentativas.add("1");
        tentativas.add("2");
        tentativas.add("3");
        tentativas.add("4");

        // Tenta remover o movimento que já sabemos que falhou
        tentativas.remove(movimentoFalho);

        // Se o input foi "up", "down", etc., removemos o correspondente numérico para evitar retentativa inútil
        if ("up".equalsIgnoreCase(movimentoFalho)) tentativas.remove("1");
        if ("down".equalsIgnoreCase(movimentoFalho)) tentativas.remove("2");
        if ("right".equalsIgnoreCase(movimentoFalho)) tentativas.remove("3");
        if ("left".equalsIgnoreCase(movimentoFalho)) tentativas.remove("4");

        while (!tentativas.isEmpty()) {
            int indice = random.nextInt(tentativas.size());
            String tentativa = tentativas.get(indice);

            try {
                // Tenta mover com a nova direção
                super.mover(tentativa);
                return; // Sucesso! Sai do método.
            } catch (MovimentoInvalidoException e) {
                // Se falhar também, remove da lista e o loop tenta o próximo
                tentativas.remove(indice);
            }
        }
        // Se a lista ficar vazia, o robô está cercado e perde o turno.
    }
}