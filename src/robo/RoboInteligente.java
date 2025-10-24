package robo;
import java.util.ArrayList;
import excepts.MovimentoInvalidoException;
import java.util.Random;
public class RoboInteligente extends Robo {

	public RoboInteligente(String cor) {
		super(cor);
	}
	
	@ Override
	public void mover(int movimento) {
		ArrayList<Integer> tentativas = new ArrayList<>();
		tentativas.add(1); // adiciona todas as possibilidades de movimentos no arraylist 
		tentativas.add(2);
		tentativas.add(3);
		tentativas.add(4);	
		
		try {
            super.mover(movimento); // se o movimento for válido, move o robô e sai do metodo
            return;
        } catch (MovimentoInvalidoException e) {   // senão captura o movimento inválido
            tentativas.remove(Integer.valueOf(movimento));  // e remove esse movimento do array
        }
		
		Random r = new Random();
		// e tenta de forma aleatória uma das três possibilidades restantes de movimento 
        while (!tentativas.isEmpty()) {   // faz essas tentativas até o array não tiver mais elemento 
        	int indice = r.nextInt(tentativas.size());   // pega o índice aleatoriamente do array  
            int tentativa = tentativas.get(indice); 

            try {
                super.mover(tentativa);   // tenta outro movimento 
                return;
            } catch (MovimentoInvalidoException e) {   // caso não dê certo, avisa que é inválido e remove esse movimento de novo e repete o while
                tentativas.remove(indice);
            }
        }
		
	}
}