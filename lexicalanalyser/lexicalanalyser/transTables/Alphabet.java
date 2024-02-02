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
package lexicalanalyser.transTables;

import java.util.*;

/**
 *
 * Defines an alphabet.
 * An alphabet could be complex i.e. composed of other alphabets.
 * 
 * @author Faisal Aslam
 */
public class Alphabet {

    private HashSet<String> alphabetValues = new HashSet<String>();
    private String name = null;

    /**
     * Alphabet may be a single character or a complex alphabet of multiple 
     * characters.
     * In case the alphabet corresponds to a single character then 
     * the value will be only one item otherwise, it will be a set of more than
     * one items.
     * 
     * s@param name
     * @param values 
     */
    public Alphabet(String name, HashSet<String> values) {
        this.name = name;
        this.alphabetValues = values;
    }

    /**
     * true if the alphabet contains the character
     * otherwise it return false
     * 
     * @param c
     * @return 
     */
    public boolean contains(char c) {
        String str = "" + c;
        if (alphabetValues.contains(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * A simple alphabet with name and value both equal
     * to each other.
     * 
     * @param name 
     */
    public Alphabet(String name) {
        this.name = name;
        this.alphabetValues.add(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Alphabet)) {
            return false;
        }
        Alphabet input = (Alphabet) obj;
        if (input.name.equals(name)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public HashSet<String> getAlphabetValues() {
        return alphabetValues;
    }

    @Override
    public String toString() {
        String ret = "name=" + name + ", values={";
        if (alphabetValues == null) {
            return ret;
        }
        Iterator<String> it = alphabetValues.iterator();
        while (it.hasNext()) {
            ret += it.next();
            if (it.hasNext()) {
                ret += ", ";
            }
        }
        return ret + "}";
    }
}
