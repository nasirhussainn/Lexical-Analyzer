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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * This class stores all the tokens. It also print token in the token.txt
 * file.
 *
 * @author Faisal Aslam
 */
public class TokenContainer {

    private ArrayList<Token> tokenList = new ArrayList<Token>();
    private SymbolTbl symTable = new SymbolTbl();
    private static final TokenContainer tokenContainer = new TokenContainer();
    private static final String TOKEN_FILE_NAME = "token.txt";
    private static final String SYMBOL_TABLE_FILE_NAME = "symboltbl.txt";

    /**
     * No one else is allowed to create an object of this class
     */
    private TokenContainer() {
    }

    /**
     * Use this to get the object of this class
     * The class is singleton and its only one object exist
     * @return 
     */
    public static TokenContainer getInstanceOf() {
        return tokenContainer;
    }

    /**
     * Add token and symbols
     * 
     * @param tokenName
     * @param tokenValue
     * @param currentLineNumber 
     */
    public void addToken(String tokenName, String tokenValue, int currentLineNumber) {
        Token tokeToAdd = null;
        int symIndex = symTable.addSymbol(tokenValue, currentLineNumber);
        tokeToAdd = new Token(tokenName, symIndex);
        tokenList.add(tokeToAdd);
    }

    @Override
    public String toString() {
        String ret = "";
        Iterator<Token> it = tokenList.iterator();
        while (it.hasNext()) {
            ret += it.next();
        }
        return ret;
    }

    /**
     * dump symbol table and tokens in files.
     */
    public void writeInFile() {
        try {
            String toWrite = toString();
            writeInFile(toWrite, TOKEN_FILE_NAME);
            toWrite = symTable.toString();
            writeInFile(toWrite, SYMBOL_TABLE_FILE_NAME);
        } catch (Exception d) {
            System.out.println(d);
            System.exit(1);
        }
    }

    private void writeInFile(String str, String fileName) throws Exception {
        File file = new File(fileName);
        file.delete();
        file.createNewFile();
        RandomAccessFile rm = new RandomAccessFile(file, "rw");
        rm.writeBytes(str);
        rm.close();
    }
    
}
