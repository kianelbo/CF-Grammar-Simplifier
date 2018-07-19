import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Parser fileParser = new Parser();
        Grammar parsedGrammar = null;

        try {
            parsedGrammar = fileParser.parseGrammarFromFile("SampleInput.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("********The Original Grammar********");
        System.out.println(parsedGrammar.toString());

        Simplifier simplifier = new Simplifier();

        System.out.println("********The Simplified Grammar********");
        Grammar simplifiedGrammar = simplifier.simplify(parsedGrammar);
        System.out.println(simplifiedGrammar.toString());

        System.out.println("********The Chomsky Normal Form********");
        ChomskyConverter converter = new ChomskyConverter();
        Grammar converted = converter.convertToChomsky(simplifiedGrammar);
        System.out.println(converted.toString());
    }
}
