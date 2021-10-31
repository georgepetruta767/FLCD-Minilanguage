package domain;
import adt.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    SymbolTable symbolTable;
    ArrayList<String> tokens;
    ArrayList<Pair<String, Integer>> pif;

    private final String digitsRegex = "^([0-9]+)(?=[\\n:.+\\-*/%, ()}{\\]\\[\"]|$)";
    private final String stringConstantRegex = "^\"[0-9a-zA-Z]+\"";
    private final String operatorRegex = "^=|\\+|-|\\*|/|%|~|<|>|\\$|=";
    private final String separatorRegex = "^[\\n:,.()}{\\]\\[\"]";
    private final String identifierRegex = "^[A-Za-z][A-Za-z0-9]*";
    private final String reservedWordsRegex = "^\\b(verify|start|integer|end|declare|increase|decrease|auxiliary|pass|char|input|output|if|length|case|true|false|always|append|value|div|mod|and|or|vowel|consonant|read|show|repeat|boolean|character|max-length|input-output)\\b";

    public Scanner(SymbolTable symbolTable, String tokenFile){
        this.symbolTable = symbolTable;
        pif = new ArrayList<>();
        readTokens(tokenFile);
    }

    private void readTokens(String tokenFile) {
        tokens = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(tokenFile))){
            String line;
            while((line = br.readLine()) != null){
                tokens.add(line.strip());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void scan(String filePath) throws IOException {
        boolean correct = true;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            boolean found;
            while ((line = br.readLine()) != null && correct) {
                while (!line.isEmpty()) {
                    found = false;
                    line = line.strip();

                    Pattern pattern = Pattern.compile(reservedWordsRegex);
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find() && matcher.start() == 0) {
                        pif.add(new Pair(matcher.group(), -1));
                        line = line.substring(matcher.end());
                        found = true;
                    }

                    pattern = Pattern.compile(separatorRegex);
                    matcher = pattern.matcher(line);
                    if (matcher.find() && matcher.start() == 0) {
                        pif.add(new Pair(matcher.group(), -1));
                        line = line.substring(matcher.end());
                        found = true;
                    }

                    pattern = Pattern.compile(operatorRegex);
                    matcher = pattern.matcher(line);
                    if (matcher.find() && matcher.start() == 0) {
                        pif.add(new Pair(matcher.group(), -1));
                        line = line.substring(matcher.end());
                        found = true;
                    }

                    pattern = Pattern.compile(identifierRegex);
                    matcher = pattern.matcher(line);
                    if (matcher.find() && matcher.start() == 0) {
                        symbolTable.addSymbol(matcher.group());
                        pif.add(new Pair("id", symbolTable.getPosition(matcher.group())));
                        line = line.substring(matcher.end());
                        found = true;
                    }

                    pattern = Pattern.compile(digitsRegex);
                    matcher = pattern.matcher(line);
                    if (matcher.find() && matcher.start() == 0) {
                        symbolTable.addSymbol(matcher.group());
                        pif.add(new Pair("constant", symbolTable.getPosition(matcher.group())));
                        line = line.substring(matcher.end());
                        found = true;
                    }

                    pattern = Pattern.compile(stringConstantRegex);
                    matcher = pattern.matcher(line);
                    if (matcher.find() && matcher.start() == 0) {
                        symbolTable.addSymbol(matcher.group());
                        System.out.println("const" + matcher.group());
                        pif.add(new Pair("constant", symbolTable.getPosition(matcher.group())));
                        line = line.substring(matcher.end());
                        found = true;
                    }

                    if (!found) {
                        System.err.println("Lexical error! Undefined token on line " + lineNumber);
                        correct = false;
                        break;
                    }
                }
                lineNumber += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (correct) {
            System.out.println("All good!");
            writePif();
            writeST();
        }
    }

    private void writePif() throws IOException {
        FileWriter outputFile = new FileWriter("PIF.out");

        for(int i = 0; i < pif.size(); i++){
            outputFile.write(pif.get(i) + "\n");
        }

        outputFile.flush();
        outputFile.close();
    }

    private void writeST() throws IOException {
        FileWriter outputfile = new FileWriter("ST.out");

        outputfile.write(String.valueOf(symbolTable));

        outputfile.flush();
        outputfile.close();
    }

    private boolean isSeparator(String token){
        String[] separators = {"(", ")", "[", "]", ":", "", " ", "\n"};
        return Arrays.asList(separators).contains(token);
    }

    private ArrayList<String> generateTokens(String line){
        ArrayList<String> tokens = new ArrayList<>();
        String[] arrTokens = line.split(" ");
        for (int i = 0; i < arrTokens.length; ++i) {
            String word = arrTokens[i];
            ArrayList<String> lastTokens = new ArrayList<>();
            int startPos = 0, endPos;
            if (word.isEmpty() || word.equals(" "))
                continue;

            while (startPos < word.length()) {
                if (isSeparator(String.valueOf(word.charAt(startPos)))) {
                    tokens.add(String.valueOf(word.charAt(startPos)));
                } else {
                    break;
                }
                startPos += 1;
            }

            endPos = word.length() - 1;
            while (endPos > 0 && word.length() > 1) {
                if (isSeparator(String.valueOf(word.charAt(endPos)))) {
                    lastTokens.add(String.valueOf(word.charAt(endPos)));
                } else {
                    break;
                }
                endPos -= 1;
            }

            tokens.add(word.substring(startPos, endPos + 1));
            for (int j = lastTokens.size() - 1; j >= 0; j --) {
                tokens.add(lastTokens.get(j));
            }
        }
        System.out.println(tokens);
        return tokens;
    }
}
