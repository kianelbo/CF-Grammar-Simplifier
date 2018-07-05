import java.util.List;

public class State {
	
	private char nonTerminal;
	private List<String> derivations;
	
	public State(char nonTerminal) {
		this.nonTerminal = nonTerminal;
	}

	public char getNonTerminal() {
		return nonTerminal;
	}

	public List<String> getDerivations() {
		return derivations;
	}

	public void setDerivations(List<String> derivations) {
		this.derivations = derivations;
	}
	
	public void removeDerivation(String derivation){
		derivations.remove(derivation);
	}
	
	public void addDerivation(String derivation) {
		derivations.add(derivation);
	}
	
}
