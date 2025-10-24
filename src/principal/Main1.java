package principal;
import robo.Robo;
import java.util.Scanner;
import excepts.MovimentoInvalidoException;
import grafico.Campo;
public class Main1 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		 
		
		Campo [][] plano = new Campo [4][4];
		// inicializa cada campo do plano 
		for (int i = 0; i < plano.length; i++) {
		    for (int j = 0; j < plano[i].length; j++) {
		        plano[i][j] = new Campo();
		    }
		}
		
		
		System.out.println("OLÁ, PARA COMEÇAR, DIGITE A COR DO SEU ROBÔ:");
		String cor;
		while(true) {
			cor = sc.nextLine();
			if(cor.matches("[a-zA-Z]+")) // checa se só tem letras maiusculas e minusculas 
				break;
			else 
				System.out.println("DIGITE NOVAMENTE, SÓ LETRAS POR FAVOR");
		}
		
		Robo robo = new Robo (cor);
		
		
		// verifica se a posição do alimento digitada foi números inteiros e que esteja dentro do plano cartesiano
		int xAlimento , yAlimento; 
		do {
			System.out.println("Digite a posição do alimento no eixo X:   (valores de 0 a 3)");
			while (!sc.hasNextInt()) {	  // para o usuario não digitar uma letra ou outro caractere 
	                System.out.println("Entrada inválida! Digite um número inteiro para X:");
	                sc.next(); // descarta o que foi digitado inválido
	         }
			xAlimento = sc.nextInt();
			System.out.println("Digite a posição do alimento no eixo Y:   (valores de 0 a 3)");
			while (!sc.hasNextInt()) {
	                System.out.println("Entrada inválida! Digite um número inteiro para y:");
	                sc.next(); 
	         }
			yAlimento = sc.nextInt();
			sc.nextLine();
			if(xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3) // coordenadas maiores que 0 e dentro do plano
				System.out.println("valores não permitidos, digite-os novamente.");
		} while (xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3);
		
		plano[xAlimento][yAlimento].setSimbolo("🍇");
		System.out.println("Posição do alimento: (" + xAlimento + "," + yAlimento + ")\n");
		
		
		System.out.println("Posição do Robô: (" + robo.getX() + "," + robo.getY() + ")\n");
		
		
		boolean encontrouAlimento = false; 
		// pede o movimento do robô até ele encontrar o alimento
		while(!encontrouAlimento) {
			try {			
				desenharPlano(plano, robo);
				System.out.println("DIGITE O MOVIMENTO DO ROBÔ:   (1-up,2-down,3-right,4-left)");
				String movimento = sc.nextLine().trim(); // remove os dois extremos espaços da string ex: " azul " = "azul"
				try {
					int codigo = Integer.parseInt(movimento); // converte a string em um numero inteiro, se não lança uma exceção
					robo.mover(codigo);
				} catch(NumberFormatException e) {
					robo.mover(movimento);
				}
				encontrouAlimento = robo.encontrarAlimento(xAlimento, yAlimento);
				if(encontrouAlimento)
					System.out.println("O ROBÔ ENCONTROU O ALIMENTO ");	 		
			} catch(MovimentoInvalidoException e) {   // se a posição escolhida for inválida, exibi a mensagem na tela 
				System.out.println(e.getMessage());
			}
		}
		
			sc.close();
	}
	
	public static void desenharPlano(Campo[][] plano, Robo robo) {
		    for (int y = 3; y >= 0; y--) {
		        System.out.printf(" %d|", y); // printa o eixo y
		        for (int x = 0; x < 4; x++) {
		            if (x == robo.getX() && y == robo.getY()) { // printa a posição do robô
		                System.out.print(" 🤖 "); 
		            } else {
		                System.out.print(" " + plano[x][y].getsimbolo() + " "); //   senão printa espaço
		            }
		        }
		        System.out.println();   // serve para pular para a próxima linha
		    }
		    System.out.println("    0  1  2  3");     // printa o eixo x
	}

	
}
