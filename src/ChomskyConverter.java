import java.util.ArrayList;

public class ChomskyConverter {


   public Grammar convertToChomsky(Grammar cfg) {
        removeMixed(cfg);
        removeLong(cfg);
        return cfg;
    }

    /**
     * @param cfg the grammar to remove the mixed derivations from
     * @author Matt
     * Splits all derivations of a grammar that are mixed (Mixed derivations contain one or more terminals AND nonterminals)
     */
    private void removeMixed(Grammar cfg) {
        //initialize list containing the current non-terminal characters
        ArrayList<Character> nonTerminals = new ArrayList<>();

        //initialize a list for which the updated states will be added
        ArrayList<State> updatedStates = (ArrayList<State>) cfg.getStates();
        int count = 0;

        //iterate through each state in the current grammar, adding it's non-terminal character
        //to the current list of non-terminals
        for (State s : cfg.getStates()) {
            nonTerminals.add(s.getNonTerminal());

        }

        //step through the list of cfg's terminals, and if a state's derivation of length > 1 contain's this
        //terminal character, we must create a new state that points to this terminal, and then replace the terminal
        //with it's new corresponding non-terminal within the derivation
        for (char toNonTerminal : cfg.getTerminals()) {
            //find a character that is not currently being used as a non-terminal for this grammar
            while (nonTerminals.contains((char) (count % 26 + 65)) && count < 91) {
                count++;
            }
            //create a new state using this new non-terminal
            State newState = new State((char) (count % 26 + 65));
            nonTerminals.add((char) (count % 26 + 65));
            ArrayList<String> term = new ArrayList<>();
            String charToString = "" + toNonTerminal;
            term.add(charToString);

            //walk through the list of updated states, and replace every instance of 'toNonTerminal' within a derivation
            //of length > 1
            for (State toModify : updatedStates) {

                //initialize a list of strings to add derivations to, manipulated or not, depending on size
                ArrayList<String> newDerivations = new ArrayList<>();
                for (String deriv : toModify.getDerivations()) {
                    if (deriv.length() > 1 && !(deriv.length() == 2 && deriv.contains(" "))) {//need to do a special check for ' '  because of the replacement in removeEpsilon


                        char[] alreadyDerived = deriv.toCharArray();

                        //do the replacement here
                        for (char c : alreadyDerived) {
                            if (c == toNonTerminal) {
                                deriv = deriv.replace(c, newState.getNonTerminal());
                            }
                        }

                    }
                    //add the new derivation to the list of [new] derivations that we will add to the updated state
                    newDerivations.add(deriv);
                }

                //update the derivations for this state
                toModify.setDerivations(newDerivations);
            }

            //update the derivations and add state for one that does not meet the conditions above
            newState.setDerivations(term);
            updatedStates.add(newState);

        }
        //set the states to the updated list that we have created
        cfg.setStates(updatedStates);
    }


    /**
     * Removes all long derivations from a grammar (long derivations have length greater than 2, i.e S -> aAb)
     *
     * @param cfg the grammar from which the long derivations need to be removed
     */
    private void removeLong(Grammar cfg) {

        //initialize a list that will hold the new set of states
        ArrayList<State> newStates = new ArrayList<>();

        //initialize a list that contains all of the current non-terminals being used by this grammar
        ArrayList<Character> nonTerminals = new ArrayList<>();

        //initialize a list that we can add new non-terminals to
        ArrayList<Character> newNonTerminals = new ArrayList<>();

        //add all of the currently used non-terminals to this list created above
        for (State s : cfg.getStates()) {
            nonTerminals.add(s.getNonTerminal());

        }

        //add those characters that are remaining in the alphabet that are not being used by this grammar
        for (int alpha = 0; alpha < 26; alpha++) {

            if (!nonTerminals.contains((char) ((alpha) % 26 + 65))) {
                newNonTerminals.add((char) (alpha % 26 + 65));
            }

        }
        int next = 0;
        //step into a state and iterate through it's derivations
        for (State old : cfg.getStates()) {

            //initialize a list of new non-terminals that can be used for each state
            ArrayList<Character> newNonTermList = new ArrayList<>();

            //initialize empty set of derivations for the new original derivation
            ArrayList<String> forNewOrig = new ArrayList<>();

            //initialize new original state for states containing derivations of length 2 or less
            State lengthLess3 = new State(old.getNonTerminal());

            //initialize a list for the derivations of a state containing derivations of length 2 or less (corresponding to the state lengthLess3)
            ArrayList<String> forlengthLess3 = new ArrayList<>();

            //iterate through each derivation of each of the current grammar's states
            for (String checkLength : old.getDerivations()) {
                if (checkLength.length() > 2 && !checkLength.contains(" ")) {

                    //for derivations of length n > 2, create n-2 new non-terminals
                    int iterator = 2;
                    while (iterator < checkLength.length()) {
                        newNonTermList.add(newNonTerminals.get(next % 26));
                        next++;
                        iterator++;
                    }

                    //split the derivation to a character array, create new state for each non terminal and add its derivation
                    char[] derivation = checkLength.toCharArray();
                    char[] newDeriv = {derivation[0], newNonTermList.get(0)};
                    String newOriginalDerivation = new String(newDeriv);
                    State newOrig = new State(old.getNonTerminal());
                    newOrig.setDerivations(forNewOrig); //initialize derivations
                    newOrig.addDerivation(newOriginalDerivation); //add derivations
                    newStates.add(newOrig);//new original derivation
                    char leftSide = newNonTermList.get(0);
                    int index = 1;

                    ArrayList<String> newList = new ArrayList<>();
                    while (index < newNonTermList.size()) {
                        //creates a new state for each of the non-terminals that were created above for derivations having length > 2
                        State toAdd = new State(leftSide);
                        toAdd.setDerivations(newList);
                        leftSide = newNonTermList.get(index);
                        char[] rightSide = {derivation[index], leftSide};
                        String rightToString = new String(rightSide);
                        toAdd.addDerivation(rightToString);
                        newList.add(rightToString);
                        index++;
                        newStates.add(toAdd);
                    }

                    //adds the new final state, whose derivation is the non-terminal for the n-1 state created,
                    //concatenated with the last character in the sequence from the original derivation
                    ArrayList<String> forFinalNewState = new ArrayList<>();
                    State finalNewState = new State(leftSide);
                    finalNewState.setDerivations(forFinalNewState);
                    char[] rightSide = {derivation[index], derivation[index + 1]};
                    String rightToString = new String(rightSide);
                    finalNewState.addDerivation(rightToString);
                    newStates.add(finalNewState);
                } else {
                    //add the derivations that do not meet above requirements to the state initialized
                    forlengthLess3.add(checkLength);
                    lengthLess3.setDerivations(forlengthLess3);
                    newStates.add(lengthLess3);
                }
            }

        }

        //combine derivations of a state whose non-terminal occurs more than once
        ArrayList<State> finalNewStates = new ArrayList<>();
        for (State toCombine : newStates) {
            boolean toAdd = true;
            if (!finalNewStates.contains(toCombine)) {
                for (State inFinalSet : finalNewStates) {
                    if (toCombine.getNonTerminal() == inFinalSet.getNonTerminal()) {
                        toAdd = false;
                        for (String derivationCheck : toCombine.getDerivations())
                            if (!inFinalSet.getDerivations().contains(derivationCheck))
                                inFinalSet.addDerivation(derivationCheck);
                    }

                }
                if (toAdd) finalNewStates.add(toCombine);
            }
        }
        cfg.setStates(finalNewStates);
    }
}
