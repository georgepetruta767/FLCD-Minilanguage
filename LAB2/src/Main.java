

public class Main {
    public static void main(String[] args){
        SymbolTable symbolTable = new SymbolTable(10);

        String[] symbols = {"a1", "b2", "c3", "d4", "a1"};

        for (String symbol : symbols) {
            System.out.println(symbolTable.addSymbol(symbol));
        }
    }

}
