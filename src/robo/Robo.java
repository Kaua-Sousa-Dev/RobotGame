package robo;

import excepts.MovimentoInvalidoException;

public class Robo {
	
	protected static final int limiteX = 3; // tamanho do plano cartesiano,   0 <= x <= 3 , 0 <= y <= 3
	protected static final int limiteY = 3;
	protected int x;
	protected int y;
	protected String cor;
	protected String emoji;
	
	public Robo(String cor) {
		this.cor = cor;
		this.x = 0;
		this.y = 0;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getCor() {
		return cor;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setPosicao(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String getEmoji() {
		switch (cor.toLowerCase()) {
			case "vermelho": return "👾";
	        case "azul": return "🎮";
	        default: return "🤖"; 
		}
	}
	
	// mover passando como parâmetro o comando em texto
	public void mover(String movimento) throws MovimentoInvalidoException {
		int novoX = x;
		int novoY = y;
		switch (movimento.toLowerCase()) {
			case "up":
				novoY += 1;
				break;
			case "down":
				novoY -= 1;
				break;
			case "right":
				novoX += 1;
				break;
			case "left":
				novoX -= 1;
				break;
			default:
				return;
		}
		
		if(novoX < 0 || novoY < 0 || novoX > limiteX || novoY > limiteY)
			throw new MovimentoInvalidoException(movimento);
			
		x = novoX;
		y = novoY;
	}
	
	
	// mover passando como parâmetro com comando em código
	public void mover(int movimento) throws MovimentoInvalidoException {
		int novoX = x;
		int novoY = y;
		switch (movimento) {
			case 1: // up
				novoY += 1;
				break;
			case 2: // down
				novoY -= 1;
				break;
			case 3: // right
				novoX += 1;
				break;
			case 4: // left
				novoX -= 1;
				break;
			default:
				return;
		}
		
		if(novoX < 0 || novoY < 0 || novoX > limiteX || novoY > limiteY )
			throw new MovimentoInvalidoException("" + movimento);
			
		x = novoX;
		y = novoY;
	}
	
	public boolean encontrarAlimento(int posicaoAlimentoX, int posicaoAlimentoY) {
		if(this.x == posicaoAlimentoX && this.y == posicaoAlimentoY)
			return true;
			
		return false;
	}
}		
	
	
		
	
	
