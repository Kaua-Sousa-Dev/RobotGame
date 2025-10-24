package principal;
import java.util.Scanner;
import excepts.MovimentoInvalidoException;
import robo.Robo;
import robo.RoboInteligente;
import grafico.Campo;
import java.util.Random;
public class Main3 {

	public static void main(String[] args) {
		
			Scanner sc = new Scanner(System.in);
			Random r = new Random();
			
			Campo [][] plano = new Campo [4][4];
			for (int i = 0; i < plano.length; i++) {
			    for (int j = 0; j < plano[i].length; j++) {
			        plano[i][j] = new Campo();
			    }
			}

			Robo roboNormal = new Robo("normal");
			RoboInteligente roboInteligente = new RoboInteligente("inteligente");
			
			System.out.println("ROBÔ NORMAL: 🦾");
			System.out.println("ROBÔ INTELIGENTE: 🤖");
			
			
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
				if(xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3)
					System.out.println("valores não permitidos, digite-os novamente.");
			} while (xAlimento < 0 || yAlimento < 0 || xAlimento > 3 || yAlimento > 3);

			
			plano[xAlimento][yAlimento].setSimbolo("🍇");
			System.out.println("Posição do alimento: (" + xAlimento + "," + yAlimento + ")");
			
			

			System.out.println("\nPosição do Robô normal: (" + roboNormal.getX() + "," + roboNormal.getY() + ")");
			
			 //ROBO NORMAL////////////////////////////////////////////////////////////////////////////////
			boolean encontrouAlimento = false;
			int movimentosValidos = 0;
			int movimentosInvalidos = 0;
			
			System.out.println("\n========================= Vez de robô normal:====================================");
			
			while(!encontrouAlimento) {
				desenharPlano(plano, roboNormal, 1);
				pausar(2000);
				 try {
						int movimentoRoboNormal = r.nextInt(4) + 1; // aleatoriamente gera um movimento
						System.out.println("\n Movimento " + movimentoRoboNormal);
						roboNormal.mover(movimentoRoboNormal);
						movimentosValidos++;
						encontrouAlimento = roboNormal.encontrarAlimento(xAlimento, yAlimento);
						pausar(1000);
						if(encontrouAlimento) {
							System.out.println("O ROBÔ NORMAL ENCONTROU O ALIMENTO ");
							break;
						}
					} catch(MovimentoInvalidoException e) {
						pausar(1000);
						System.out.println(e.getMessage());
						movimentosInvalidos ++;
					}
			}
			
			//ROBO INTELIGENTE//////////////////////////////////////////////////////////////////////
			pausar(3000);
			System.out.println("\n=========================Vez de robô inteligente:===============================");
			System.out.println("Posição do Robô inteligente: (" + roboInteligente.getX() + "," + roboInteligente.getY() + ")\n");
			boolean encontrouAlimento2 = false;
			int movimentos = 0;
			
			while(!encontrouAlimento2) {
				desenharPlano(plano, roboInteligente, 2);
				pausar(2000);
				int movimentoRoboInteligente = r.nextInt(4) + 1;
				System.out.println("\n Movimento " + movimentoRoboInteligente);
				roboInteligente.mover(movimentoRoboInteligente);
				movimentos ++;
				encontrouAlimento2 = roboInteligente.encontrarAlimento(xAlimento, yAlimento);
				pausar(1000);
				if(encontrouAlimento2) {
					System.out.println("O ROBÔ INTELIGENTE ENCONTROU O ALIMENTO ");
					break;
				}
			}
			
			System.out.println("\nRobô normal:");
			System.out.println("Movimentos válidos: " + movimentosValidos);
			System.out.println("Movimentos inválidos: " + movimentosInvalidos);
			System.out.println("TOTAL DE MOVIMENTOS: " + (movimentosValidos + movimentosInvalidos));
			
			System.out.println("\nRobô inteligente:");
			System.out.println("TOTAL DE MOVIMENTOS:" + movimentos);
			
			
			sc.close();
			
	}

	public static void desenharPlano(Campo[][] plano, Robo robo, int tipo) {
	    for (int y = 3; y >= 0; y--) {
	        System.out.printf(" %d|", y);
	        for (int x = 0; x < 4; x++) {
	            if (x == robo.getX() && y == robo.getY()) {
	                if(tipo == 1)
	            		System.out.print(" 🦾 " );
	                else 
	                	System.out.print(" 🤖 " );
	            } else {
	                System.out.print(" " + plano[x][y].getsimbolo() + " ");
	            }
	        }
	        System.out.println();
	    }
	    System.out.println("    0  1  2  3");
	}
	
	public static void pausar(int tempo) {
		try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
	}
	
}
