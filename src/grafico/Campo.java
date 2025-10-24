package grafico;

public class Campo {
	
	private String simbolo;
	
	
	public Campo() {
		this.simbolo = " ";
	}
	
	public String getsimbolo() {
		return simbolo;
	}
	
	public void setSimbolo(String simbolo) {
		if(this.simbolo == " ")
			this.simbolo = simbolo;
	}

}
