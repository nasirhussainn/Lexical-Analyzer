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
 * This class represents a single token. Tokens are stored in TokenContainer.
 * 
 * @author Faisal Aslam
 */
public class Token {

    private String name = null;
    private int symbolTableIndex = -1;

    public Token(String name, int symbolTableIndex) {
        this.name = name;
        this.symbolTableIndex = symbolTableIndex;
    }

    public Token(String name) {
        this.name = name;
    }

    public String toString() {
        if (symbolTableIndex != -1) {
            return "<" + name + ", " + symbolTableIndex + ">\n";
        } else {
            return "<" + name + ">\n";
        }
    }
}
