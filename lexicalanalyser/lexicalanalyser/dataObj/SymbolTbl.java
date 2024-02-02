/*
 * Copyright 2012 Faisal Aslam.
 * All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * only, as published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 3 for more details (a copy is
 * included in the LICENSE file that accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License
 * version 3 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Faisal Aslam (faisal.aslam AT gmail.com)
 * if you need additional information or have any questions.
 */
package lexicalanalyser.dataObj;

import java.util.*;

/**
 *
 * It has all the Symbols and it prints out the Symbol Table 
 * in a file named symbolTbl.txt
 * @author Faisal Aslam
 */
class SymbolTbl {

    private ArrayList<Symbol> symbolTable = new ArrayList<Symbol>();

    private int findSymbol(Symbol symbol) {
        return symbolTable.indexOf(symbol);
    }

    public int addSymbol(String tokenValue, int currentLineNumber) {
        Symbol candidate = new Symbol(tokenValue, currentLineNumber, currentLineNumber);
        int index = findSymbol(candidate);
        if (index != -1) {
            Symbol oldSavedValue = symbolTable.get(index);
            oldSavedValue.foundLastAtLineNum = currentLineNumber;
            return index + 1; // we want to index starting from 1
        } else {
            symbolTable.add(candidate);
            return symbolTable.size(); //we want index starting from 1
        }
    }

    public String toString() {
        String ret = "";
        Iterator<Symbol> it = symbolTable.iterator();
        while (it.hasNext()) {
            ret += it.next();
        }
        return ret;
    }
}
