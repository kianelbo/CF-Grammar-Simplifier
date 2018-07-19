import java.util.ArrayList;

public class State {
	
	private char nonTerminal;
	private ArrayList<String> derivations;
	
	public State(char nonTerminal) {
		this.nonTerminal = nonTerminal;
	}

	public char getNonTerminal() {
		return nonTerminal;
	}

	public ArrayList<String> getDerivations() {
		return derivations;
	}

	public void setDerivations(ArrayList<String> derivations) {
		this.derivations = derivations;
	}
	
	public void removeDerivation(String derivation){
		derivations.remove(derivation);
	}
	
	public void addDerivation(String derivation) {
		derivations.add(derivation);
	}
	
}
