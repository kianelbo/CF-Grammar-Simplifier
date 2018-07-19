import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Parser {

    private Grammar parsedGrammar;

    public Grammar parseGrammarFromFile(String filePath) throws IOException {
        parsedGrammar = new Grammar();

        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));

        boolean initialSetup = true;

        String currentLine = reader.readLine();

        while (currentLine != null && !currentLine.isEmpty() && !(currentLine.trim().equals(""))) {
            if (initialSetup) {

                if (currentLine.startsWith("V:")) parseStates((currentLine.split(":")[1]).trim());
                else if (currentLine.startsWith("T:")) parseTerminals(currentLine.split(":")[1].trim());
                else if (currentLine.startsWith("S:")) parseStartState(currentLine.split(":")[1].trim());
                else if (currentLine.startsWith("P:")) initialSetup = false;

            } else parseRules(currentLine.trim());
            currentLine = reader.readLine();
        }

        reader.close();
        return parsedGrammar;
    }

    private void parseStates(String newStates) {
        String[] splitStates = newStates.split(",");
        ArrayList<State> statesToAdd = new ArrayList<>();
        for (String state : splitStates) {
            state = state.trim();
            State newState = new State(state.charAt(0));
            statesToAdd.add(newState);
        }
        parsedGrammar.setStates(statesToAdd);
    }

    private void parseTerminals(String terminals) {
        String[] splitTerminals = terminals.split(",");
        ArrayList<Character> terminalsToAdd = new ArrayList<>();
        for (String terminal : splitTerminals) {
            terminal = terminal.trim();
            terminalsToAdd.add(terminal.charAt(0));
        }
        parsedGrammar.setTerminals(terminalsToAdd);
    }

    private void parseStartState(String startState) {
        parsedGrammar.setStartState(parsedGrammar.getStateWithName(startState.trim().charAt(0)));
    }

    private void parseRules(String rulesToParse) {
        State stateToEdit = parsedGrammar.getStateWithName(rulesToParse.charAt(0));
        ArrayList<String> derivationsToAdd = new ArrayList<>();
        String[] unparsedDerivations = rulesToParse.split("->")[1].trim().split("\\|");

        for (String derivation : unparsedDerivations) {
            derivation = derivation.trim();
            derivationsToAdd.add(derivation);
        }
        stateToEdit.setDerivations(derivationsToAdd);
    }

}
