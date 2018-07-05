import java.util.ArrayList;
import java.util.List;

public class Grammar {
    private List<State> states;
    private List<Character> terminals;
    private State startState;

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<Character> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<Character> terminals) {
        this.terminals = terminals;
    }

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public State getStateWithName(char stateName) {
        for (State state : states)
            if (state.getNonTerminal() == stateName) return state;
        return null;
    }

    public void removeEmptyStates() {
        if (!states.isEmpty()) for (int indexOfState = 0; indexOfState < states.size(); indexOfState++) {
            if (states.get(indexOfState).getDerivations().isEmpty()) states.remove(indexOfState);
        }
        else System.out.println("There are no states in this grammar, so no empty ones were removed.");
    }

    public String toString() {
        String objectString = "";
        objectString += "States: ";
        for (State state : states) {
            objectString += state.getNonTerminal();
            if (!(states.indexOf(state) == (states.size() - 1))) objectString += ", ";
        }

        objectString += "\n" + "Terminals: ";
        for (char terminal : terminals) {
            objectString += terminal;
            if (!(terminals.indexOf(terminal) == (terminals.size() - 1))) objectString += ", ";
        }

        objectString += "\n" + "Start State: " + startState.getNonTerminal() + "\n" + "Rules: " + "\n";
        for (State state : states) {
            objectString += state.getNonTerminal() + ": ";
            ArrayList<String> derivations = (ArrayList<String>) state.getDerivations();
            for (String derivation : derivations) {
                objectString += derivation;

                if (!(derivations.indexOf(derivation) == (derivations.size() - 1))) objectString += "|";
            }

            objectString += "\n";
        }
        return objectString;
    }
}
