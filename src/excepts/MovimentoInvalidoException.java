package excepts;

public class MovimentoInvalidoException extends Exception {
	
	 private static final long serialVersionUID = 1L;
	 
	 public MovimentoInvalidoException(String movimento) {
		 super("O movimento " + movimento + " foi inválido! \n");
	}
	
}
