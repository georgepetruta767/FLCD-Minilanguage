package domain;

import adt.Pair;

import java.util.ArrayList;

public class SymbolTable {
    private ArrayList<ArrayList<String>> symbols;

    public SymbolTable(int size){
        this.symbols = new ArrayList<>(size);
        for(int i = 0; i < size; i++)
            this.symbols.add(new ArrayList<>());
    }

    public Pair addSymbol(String symbol){
        if(this.containsSymbol(symbol))
            return getPosition(symbol);

        symbols.get(hashFunction(symbol)).add(symbol);
        return getPosition(symbol);
    }

    private int hashFunction(String symbol){
        return symbol.codePoints().sum() % this.symbols.size();
    }

    public boolean containsSymbol(String symbol) {
        return symbols.get(this.hashFunction(symbol)).contains(symbol);
    }

    public Pair getPosition(String symbol) {
        if (!containsSymbol(symbol))
            return null;

        return new Pair(hashFunction(symbol), symbols.get(hashFunction(symbol)).indexOf(symbol));
    }

    public boolean removeSymbol(String symbol) {
        int hashValue = hashFunction(symbol);

        if (!symbols.get(hashValue).contains(symbol)) {
            return false;
        }

        symbols.get(hashValue).remove(symbol);
        return true;
    }

}
