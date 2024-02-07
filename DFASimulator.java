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
package lexicalanalyser;

import java.util.*;
import lexicalanalyser.transTables.*;

/**
 *
 * It simulates DFA on all the transition table. It return true if the given
 * stream is accepted on a single transition table it return false if it is not
 * accepted on any of the given transition table.
 * 
 
 */
public class DFASimulator {

	private static final String DUMMY_STATE = "&DUMMY$";

	/**
	 * In case the given input string is accepted then it returns the start
	 * state of the transition table that has accepted the input stream.
	 * Otherwise, it returns null
	 * 
	 * @param tt
	 *            you can get it from the TransitionTableController
	 * @param sourceFileStream
	 * @return
	 * @throws Exception
	 */
	// IS ACCEPTED

public State isAccepted(Collection<TransitionTable> tt, char[] sourceFileStream) throws Exception {
		// Initialize a dummy state
		State currentState = new State(DFASimulator.DUMMY_STATE);

		// Iterate through each transition table in the collection
		for (TransitionTable table : tt) {
			// Reset the current state to the start state of the current transition table
			currentState = table.getStartState();

			// Iterate through each character in the sourceFileStream
			for (char c : sourceFileStream) {
				// Skip whitespace characters
				if (Character.isWhitespace(c)) {
					continue;
				}

				// Find the next state based on the current state and character
				State nextState = table.move(currentState, c);

				// If no valid transition is found, break out of the loop
				if (nextState == null) {
					break;
				}

				// Update the current state
				currentState = nextState;
			}

			// Check if the current state is an accepting state in the current transition
			// table
			if (table.isFinal(currentState)) {
				return currentState;
			}
		}

		// No accepting state found, return null
		return null;
	}
}
