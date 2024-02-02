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
 * It represents a transition table.
 * @author Faisal Aslam
 */
public class TransitionTable {

    private State startState = null;
    private HashSet<State> finalStates = new HashSet<State>();
    /**
     * We do not need the following allStates given that all states could be 
     * reached from the startState. However, the following data structure 
     * make the implementation easy.
     */
    private ArrayList<State> allStates = new ArrayList<State>();
    private ArrayList<Alphabet> alphabetsOfTT = new ArrayList<Alphabet>();
    private ArrayList<Transition> transitions = new ArrayList<Transition>();

    public TransitionTable() {
    }

    /**
     * add or replace the start state.
     * @param startState 
     */
    public void addStartState(State startState) {
        this.startState = startState;

    }

    public ArrayList<Alphabet> getAllAphabets() {
        return alphabetsOfTT;
    }

    public boolean isFinal(State s) {
        return finalStates.contains(s);
    }

    public void addAlphabets(ArrayList<Alphabet> alphabetsOfTT) {
        this.alphabetsOfTT.addAll(alphabetsOfTT);
    }

    public void addStates(ArrayList states) {
        allStates.addAll(states);
    }

    public State getFromAllStates(String stateName) {
        Iterator<State> it = allStates.iterator();
        while (it.hasNext()) {
            State state = it.next();
            if (state.getCurrentStateName().equals(stateName)) {
                return state;
            }
        }
        return null;
    }

    public Collection<State> getAllStates() {
        return allStates;
    }

    /**
     * There could be only one state state of the transition table.
     * @return 
     */
    public State getStartState() {
        return startState;
    }

    /**
     * There could be many final states in a transition table.
     * @param finalStates 
     */
    public void addFinalStates(ArrayList<State> finalStates) {
        this.finalStates.addAll(finalStates);

    }

    public void addTransition(Transition trans) {
        transitions.add(trans);
    }

    /**
     * Given  a currentState it returns nextState that can be reached 
     * consuming the giving input character.
     * 
     * That is how it works: 
     * 1) Go to all the transitions one by one, that are stored in this transition table.
     * 2) check if a transition has same state and input alphabet that 
     * is given as parameter of the function. if such a transition is
     * found then
     * --- return the next state that can be reached using that transition.
     * 3) if no transition is found then throw an exception. 
     * 
     * @param currentState
     * @param c
     * @return 
     */
    public State move(State currentState, char c) throws Exception {
        Iterator<Transition> transIt = transitions.iterator();
        while (transIt.hasNext()) {
            Transition trans = transIt.next();
            if (trans.currentState.equals(currentState)
                    && trans.alphabetForNextState.contains(c)) {
                return trans.nextState;
            }
        }
//        throw new Exception("Invalid transition. Cannot find transition"
//                + " from state=" + currentState + " using alphabet = " + c);        
        return null;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += Constants.START_STATE_ID + " " + startState.getCurrentStateName() + "\n";
        ret += Constants.FINAL_STATE_ID + " " + toString(finalStates);
        ret += Constants.STATES_ID + " " + toString(allStates);
        ret += Constants.ALPHABET_ID + " " + toString(alphabetsOfTT);
        ret += Constants.TRANSITION_TABLE_ID + " " + toString(transitions);
        return ret;
    }

    private String toString(Collection list) {
        String ret = "";
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            ret += obj;
            if (it.hasNext()) {
                ret += Constants.SPACE;
            }
        }
        return ret + "\n";
    }
}
