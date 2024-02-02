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

import java.io.*;
import java.util.*;

/**
 *
 * It reads a file named transitionTable.txt and
 * create transition tables in the memory.
 * 
 * It has record of all the table. Other classes should use it
 * to get the transition tables.
 * 
 * @author Faisal Aslam
 */
public class TransitionTableController {

    private HashSet<Alphabet> complexAlphabet = new HashSet<Alphabet>();
    private static final String fileName = "transitionTables.txt";
    private ArrayList<TransitionTable> listTT = new ArrayList<TransitionTable>();
    private static final TransitionTableController ttContr = new TransitionTableController();

    /**
     * No one is allowed to create my constructor.
     * I am singleton. 
     */
    private TransitionTableController() {    	
    }

    /**
     * Use this method to get the object of this class.
     * @return 
     */
    public static TransitionTableController getInstanceOf() {
        return ttContr;
    }

    public void read() {

        try {
            RandomAccessFile rm = new RandomAccessFile(fileName, "rw");

            TransitionTable table = new TransitionTable();
            while (rm.getFilePointer() < rm.length()) {
                /**
                 * get a line from the file and trim the spaces in the start/end
                 */
                String line = rm.readLine().trim();
                /**
                 * skip the comments and empty lines
                 */
                if (line.length() == 0 || line.startsWith(Constants.COMMENT_ID)) {
                    continue;
                }
                //System.out.println(line);
                if (line.startsWith(Constants.DEF_ID)) {
                    line = line.substring(Constants.DEF_ID.length() + 1).trim();
                    complexAlphabet.add(createComplexAlphabet(line));
                } else if (line.startsWith(Constants.FINAL_STATE_ID)) {
                    line = line.substring(Constants.FINAL_STATE_ID.length() + 1).trim();
                    ArrayList<State> finalStates = getStates(line, table, true);
                    table.addFinalStates(finalStates);
                } else if (line.startsWith(Constants.START_STATE_ID)) {
                    line = line.substring(Constants.START_STATE_ID.length() + 1).trim();
                    ArrayList<State> startStates = getStates(line, table, true);
                    /**
                     * only one start state
                     */
                    table.addStartState(startStates.iterator().next());
                } else if (line.startsWith(Constants.STATES_ID)) {
                    line = line.substring(Constants.STATES_ID.length() + 1).trim();
                    ArrayList<State> allStates = getStates(line, table, true);
                    table.addStates(allStates);
                } else if (line.startsWith(Constants.ALPHABET_ID)) {
                    /**
                     * Either an alphabet is complex or simple.
                     * It it is complex then a definition exist for it that list its data.
                     * In case it is simple then its name and value will be same.
                     */
                    line = line.substring(Constants.ALPHABET_ID.length() + 1).trim();
                    ArrayList<Alphabet> alphabets = createAlphabetsFromTT(line);
                    table.addAlphabets(alphabets);
                } else if (line.startsWith(Constants.TRANSITION_TABLE_ID)) {
                    line = line.substring(Constants.TRANSITION_TABLE_ID.length() + 1).trim();
                    readTransitionTable(table, line);
                    listTT.add(table);
                    table = new TransitionTable();
                } else {
                    throw new UnsupportedOperationException("Unknown symbol in transtionTables file =" + line);
                }
            }
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }

    }

    public Collection<TransitionTable> getTransitionTables() {
        return this.listTT;
    }

    /**
     * The number of values in the given line should be states*alphabets
     * 
     * Example: 3 states and 2 alphabets means exactly 3*2 = 6 values
     * The first value will corresponds to first alphabet and first state
     * The second value will corresponds to second alphabet and first state
     * The third value will corresponds to first alphabet and second state
     * so on ...
     * 
     * @param tt
     * @param line 
     */
    private void readTransitionTable(TransitionTable tt, String line) {
        try {
            String[] values = line.split(Constants.SPACE);
            int currentValueIndex = 0;
            Iterator<State> stateIt = tt.getAllStates().iterator();
            while (stateIt.hasNext()) {
                Iterator<Alphabet> alphaIt = tt.getAllAphabets().iterator();
                State state = stateIt.next();
                while (alphaIt.hasNext()) {
                    Alphabet alphabetToNextState = alphaIt.next();
                    String nextStateName = values[currentValueIndex].trim();
                    State nextState = getStates(nextStateName, tt, false).iterator().next();
                    tt.addTransition(new Transition(state, alphabetToNextState, nextState));
                    currentValueIndex++;
                    if (values.length < currentValueIndex && values[currentValueIndex].trim().length() == 0) {
                        currentValueIndex++; //skip the extra spaces.
                    }
                }
            }
        } catch (Exception d) {
            System.out.println("ERROR! invalid transition table. Number of entries should be alphabet*states");
            System.exit(1);
        }
    }

    private ArrayList<Alphabet> createAlphabetsFromTT(String line) {
        String alphabetNames[] = line.split(Constants.SPACE);
        ArrayList<Alphabet> ret = new ArrayList<Alphabet>();
        for (int loop = 0; loop < alphabetNames.length; loop++) {
            String name = alphabetNames[loop].trim();
            Alphabet alpha = findComplexAlphabet(name);
            if (alpha == null) {
                alpha = new Alphabet(name);
            }
            ret.add(alpha);
        }
        return ret;
    }

    private Alphabet findComplexAlphabet(String name) {
        Iterator<Alphabet> it = complexAlphabet.iterator();
        while (it.hasNext()) {
            Alphabet alphabet = it.next();
            if (alphabet.getName().equals(name)) {
                return alphabet;
            }
        }
        return null;
    }

    /**
     * This function make sure that if a state already exist the same state
     * is return instead of creating new state with the given line. The new 
     * state is made only if it does not exist already in the Transition Table.
     * 
     * @param line should only have states separated by commas.
     * @param tt
     * @param createIfDoNotExist
     * @return 
     */
    private ArrayList<State> getStates(String line, TransitionTable tt, boolean createIfDoNotExist) {
        ArrayList<State> ret = new ArrayList<State>();
        String stateNames[] = line.split(Constants.SPACE);
        for (int loop = 0; loop < stateNames.length; loop++) {
            String name = stateNames[loop].trim();
            if (name.length() == 0) {
                continue;
            }
            State state = tt.getFromAllStates(name);
            if (state == null && createIfDoNotExist) {
                state = new State(name);
            } else if (state == null) {
                throw new UnsupportedOperationException("state does not exist");
            }
            ret.add(state);
        }
        return ret;
    }

    private Alphabet createComplexAlphabet(String line) {
        String[] tokens = line.split(" ");
        String name = "";
        HashSet<String> values = new HashSet<String>();
        for (int loop = 0; loop < tokens.length; loop++) {
            String token = tokens[loop].trim();
            if (token.length() == 0) {
                continue;
            }
            if (loop == 0) {
                name += token;
            } else {
                values.add(token);
            }
        }
        return new Alphabet(name, values);
    }

    public static void main(String args[]) {
        TransitionTableController reader = TransitionTableController.getInstanceOf();
        reader.read();
        Collection<TransitionTable> ttList = reader.getTransitionTables();
        Iterator<TransitionTable> it = ttList.iterator();
        while (it.hasNext()) {
            TransitionTable tt = it.next();
            System.out.println(tt + "\n\n\n");
        }
    }
}
