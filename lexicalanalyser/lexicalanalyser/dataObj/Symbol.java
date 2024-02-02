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

/**
 *
 * This is a Symbol used to store in the SymbolTable class
 * You do not need to change this class.
 * 
 * @author Faisal Aslam
 */
class Symbol {

    String symbolName = null;
    /**
     * This is the first line of the SOURCE CODE where
     * that symbol was found.
     */
    int foundFirstAtLineNum = -1;
    /**
     * This is the last line of the SOURCE CODE where
     * that symbol was found.
     */
     int foundLastAtLineNum = -1;

    public Symbol(String symbolName, int foundFirstAtLineNum, int foundLastAtLineNum) {
        this.symbolName = symbolName;
        this.foundFirstAtLineNum = foundFirstAtLineNum;
        this.foundLastAtLineNum = foundLastAtLineNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Symbol)) {
            return false;
        }
        Symbol input = (Symbol)obj;
        if (input.symbolName.equals(symbolName)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.symbolName != null ? this.symbolName.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return symbolName + " " + foundFirstAtLineNum + " " + foundLastAtLineNum+"\n";
    }

    
}
