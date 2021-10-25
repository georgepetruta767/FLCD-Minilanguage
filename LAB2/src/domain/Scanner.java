package domain;
import adt.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Scanner {
    SymbolTable symbolTable;
    ArrayList<String> tokens;
    ArrayList<Pair<String, Integer>> pif;

    public Scanner(SymbolTable symbolTable, String tokenFile){
        this.symbolTable = symbolTable;
        pif = new ArrayList<>();

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

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            int lineNumber = 0;
            while((line = br.readLine()) != null && correct){
                ArrayList<String> receivedTokens = generateTokens(line.strip());
                for (String token : receivedTokens) {
                    if(token.equals(" ") || token.isEmpty())
                        continue;

                    if(isOperator(token) || isSeparator(token) || isReservedWord(token)){
                        pif.add(new Pair(token, lineNumber));
                    }else if(tokens.contains(token)){
                        
                    }
                }
            }
        }
    }

    private boolean isOperator(String token){
        String[] operators = {"+", "-", "*", "/", "="};
        return Arrays.asList(operators).contains(token);
    }

    private boolean isSeparator(String token){
        String[] separators = {"(", ")", "[", "]", "{", "}", ":", ",", "."};
        return Arrays.asList(separators).contains(token);
    }

    private boolean isReservedWord(String token){
        String[] words = {"start", "end", "declare", "increase", "decrease", "auxiliary", "input",
                            "output", "input", "if", "verify", "if", "length", "case", "true",
                            "false", "always", "append", "value", "delete"};

        return Arrays.asList(words).contains(token);
    }

    private ArrayList<String> generateTokens(String line){
        ArrayList<String> tokens = new ArrayList<>();
        int i = 0, j = 0;
        while(j < line.length()){
            if(line.charAt(j) == '"'){
                tokens.add(line.substring(i, j).strip());
                i = j;
                j += 1;
                if(line.substring(i + 1).indexOf('"') == -1){
                    j = line.length() - 1;
                } else {
                    while(line.charAt(j) != '"'){
                        j += 1;
                    }
                    j += 1;
                }
            }

            if(isOperator(String.valueOf(line.charAt(j))) || isSeparator(String.valueOf(line.charAt(j)))){
                tokens.add(line.substring(i, j).strip());
                tokens.add(line.substring(j, i).strip());
                i = j + 1;
            }
            j += 1;
        }
        return tokens;
    }
}
