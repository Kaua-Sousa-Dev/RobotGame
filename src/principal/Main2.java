package principal;
import java.util.Scanner;
import robo.Robo;
import excepts.MovimentoInvalidoException;
import grafico.Campo;
import java.util.Random;
public class Main2 {

	public static void main(String[] args) {
			
		Scanner sc = new Scanner(System.in);
		Random r = new Random();
		
		Campo [][] plano = new Campo [4][4];
		// inicializa cada campo do plano
		for (int i = 0; i < plano.length; i++) {
		    for (int j = 0; j < plano[i].length; j++) {
		        plano[i][j] = new Campo();
		    }
		}
		
		
		System.out.println("OLÁ, PARA COMEÇAR, DIGITE A COR DO 1° ROBÔ:");
		String cor1;
		while(true) {
			cor1 = sc.nextLine();
			if(cor1.matches("[a-zA-Z]+"))
				break;
			else 
				System.out.println("DIGITE NOVAMENTE, SÓ LETRAS POR FAVOR.");
		}
		Robo robo1 = new Robo (cor1);
		
		System.out.println("DIGITE A COR DO 2° ROBÔ:");
		String cor2;
		while(true) {
			cor2 = sc.nextLine();
			if(cor2.matches("[a-zA-Z]+"))
				break;
			else 
				System.out.println("DIGITE NOVAMENTE, SÓ LETRAS POR FAVOR");
		}
		Robo robo2 = new Robo (cor2);
		
		
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
		System.out.println("Posição do alimento: (" + xAlimento + "," + yAlimento + ")");
		
		
		System.out.println("\nPosição do Robô " + robo1.getCor() + ": (" + robo1.getX() + "," + robo1.getY() + ")");
		System.out.println("Posição do Robô " + robo2.getCor() + ": (" + robo2.getX() + "," + robo2.getY() + ")");
		
		System.out.println("Informações salvas! aperte enter para continuar ");
		sc.nextLine();
		
		boolean encontrouAlimento = false;
		int movimentosValidos1 = 0;
		int movimentosInvalidos1 = 0;
		int movimentosValidos2 = 0;
		int movimentosInvalidos2 = 0;
		
		while(!encontrouAlimento) {
			desenharPlano(plano, robo1, robo2);
			pausar(1000);
			try {
				System.out.println("\n Vez de robô " + robo1.getCor() + ":");
				int movimentoRobo1 = r.nextInt(4) + 1;
				System.out.println(" Movimento " + movimentoRobo1);
				robo1.mover(movimentoRobo1);
				movimentosValidos1++;
				encontrouAlimento = robo1.encontrarAlimento(xAlimento, yAlimento);
				if(encontrouAlimento) {
					System.out.println("O ROBÔ " + robo1.getCor() + " ENCONTROU O ALIMENTO ");
					break;
				}
			} catch(MovimentoInvalidoException e) {
				System.out.println(e.getMessage());
				movimentosInvalidos1 ++;
			}
			pausar(2000);
			desenharPlano(plano, robo1, robo2);
			pausar(1000);
			try {
				System.out.println("\n Vez de robô " + robo2.getCor() + ":");
				int movimentoRobo2 = r.nextInt(4) + 1;
				System.out.println(" Movimento " + movimentoRobo2);
				robo2.mover(movimentoRobo2);
				movimentosValidos2++;
				encontrouAlimento = robo2.encontrarAlimento(xAlimento, yAlimento);
				if(encontrouAlimento) {
					System.out.println("O ROBÔ " + robo2.getCor() + " ENCONTROU O ALIMENTO ");
					break;
				}
			} catch(MovimentoInvalidoException e) {
				System.out.println(e.getMessage());
				movimentosInvalidos2 ++;
			}
			pausar(2000);
			
		}
		
		System.out.println("\nRobô " + robo1.getCor() + " :");
		System.out.println("Movimentos válidos: " + movimentosValidos1);
		System.out.println("Movimentos inválidos: " + movimentosInvalidos1);
		System.out.println("\nRobô " + robo2.getCor() + " :");
		System.out.println("Movimentos válidos: " + movimentosValidos2);
		System.out.println("Movimentos inválidos: " + movimentosInvalidos2);
		
		
		sc.close();
		
	}
	
	public static void desenharPlano(Campo[][] plano, Robo robo1, Robo robo2) {
	    for (int y = 3; y >= 0; y--) {
	        System.out.printf(" %d|", y);
	        for (int x = 0; x < 4; x++) {
	        	boolean fatoRobo1 = (x == robo1.getX() && y == robo1.getY());
	        	boolean fatoRobo2 = (x == robo2.getX() && y == robo2.getY());
	        	
	        	if(fatoRobo1 && fatoRobo2)
	        		 System.out.print(" ⚔️ ");
	        	else if(fatoRobo1 )
	        		System.out.print(" " + robo1.getEmoji() + " ");
	        	else if(fatoRobo2)
	        		System.out.print(" " + robo2.getEmoji() + " ");
	        	else
	        		System.out.print(" " + plano[x][y].getsimbolo() + " ");
	        }
	        System.out.println("");
	    }        
	    System.out.println("    0  1  2  3");
	}
	
	// para da um tempo pro usuário ler 
	public static void pausar(int tempo) {
		try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
	}
	
}
