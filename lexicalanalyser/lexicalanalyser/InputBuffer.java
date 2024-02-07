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

import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Scanner;
import lexicalanalyser.dataObj.TokenContainer;
import lexicalanalyser.transTables.State;
import lexicalanalyser.transTables.TransitionTable;
import lexicalanalyser.transTables.TransitionTableController;
import lexicalanalyser.dataObj.*;;

/**
 *
 * This is a typical input buffer. The buffer will have two pointers: forward
 * and lex-pointer.
 * 
 * The current string for the DFA simulator is characters between lex to forward
 * pointer.
 * 
 * In case both lex and forward will be pointing to a same location and DFA
 * simulator returns false then an error message will be generated.
 * 
 * In case DFA simulator return true then the forward pointer is incremented one
 * char at a time, until the DFA simulator returns false. After that we will
 * decrement forward point and extract the token and write it in files.
 * 
 * After successfully getting the token both forward and lex pointer now points
 * to forward+1 location.
 * 
 * The process repeat itself until the end of file is reached.
 * 
 * @author Faisal Aslam
 */

public class InputBuffer {

	private static int SIZE_OF_BUFFER = 1000;
	private static final int MAX_TOKEN_SIZE_ALLOWED = 100;
	private static final char COMMENT_START = '/';
	/**
	 * This is the buffer
	 */
	private char[] buffer = new char[SIZE_OF_BUFFER];
	/**
	 * This cache is used when a token span in two buffers.
	 */
	private char[] cache = new char[MAX_TOKEN_SIZE_ALLOWED];
	/**
	 * It is use to fill the buffer.
	 */
	int fillIndex = 0;
	/**
	 * lexeme pointer the value of lpoint may be negative. The non-positive
	 * value points to the cache that contains a partial token from the previous
	 * buffer
	 */
	int lpoint = 0;
	/**
	 * forward pointer
	 */
	int fpoint = 0;
	/**
	 * record the line number of the source code.
	 */
	int currentlineNumber = 1;
	boolean lastCharWasASlash = false;
	boolean insideComment = false;
    TokenContainer tokenContainer = TokenContainer.getInstanceOf();
	/**
	 * 
	 * @param dataFromSource
	 */
	public void addInBuff(char dataFromSource) {
		/**
		 * first fill the buffer.
		 */
		if (fillIndex < SIZE_OF_BUFFER) {
			if (dataFromSource == COMMENT_START) {
				if (lastCharWasASlash) {
					/**
					 * we are in comments skip until new line character is found
					 */
					insideComment = true;
					fillIndex--; // to overwrite single slash already in the
									// buffer
				}
				lastCharWasASlash = true;
			} else {
				lastCharWasASlash = false;
			}
			if (insideComment && dataFromSource != '\n') {
				// skip
			} else {
				buffer[fillIndex++] = dataFromSource;
				insideComment = false;
			}
		} else {
			/**
			 * when the buffer is full then use it to find tokens.
			 */
			// printBuffer();
			while (fpoint < SIZE_OF_BUFFER && buffer[lpoint] != Main.EOF) {
				findToken();
			}
			// System.out.println("\n\n *** reset the buffer\n");
			buffer = new char[SIZE_OF_BUFFER];
			fillIndex = 0;
		}
		if (dataFromSource == Main.EOF) {

			// printBuffer();
			while (fpoint < SIZE_OF_BUFFER && buffer[lpoint] != Main.EOF) {
				findToken();
			}

		}

	}

	private void printError() {
			String buffer1 = String.valueOf(buffer[fpoint]);
			buffer1 = buffer1.trim();
			if (buffer[fpoint] != '\u0000' && buffer[fpoint] != ' ' && buffer1.length() != 0)
				System.err.println("Invalid character '" + buffer[fpoint] + "' at line number =" + (currentlineNumber+1));
	}

	// FIND TOKEN


    private void findToken() {
	    try {
	        if (buffer[fpoint] == '\n') {
	            currentlineNumber++;
	            fpoint++;
	            return;
	        }

	        if (buffer[fpoint] == '\r'){
	            fpoint++;
	            return;
	        }

	        // Skip whitespace characters
	        if (Character.isWhitespace(buffer[fpoint])) {
	            fpoint++;
	            return;
	        }

	        // Check for comments (lines starting with "//")
	        if (buffer[fpoint] == '/' && buffer[fpoint + 1] == '/') {
	            while (buffer[fpoint] != '\n' && buffer[fpoint] != Main.EOF) {
	                fpoint++;
	            }
	            return;
	        }

	        // Create a cache to store the token
	        int cacheIndex = 0;

	        // Iterate through the buffer to extract the token
	        while (fpoint < SIZE_OF_BUFFER && cacheIndex < MAX_TOKEN_SIZE_ALLOWED
	                && buffer[fpoint] != Main.EOF && !Character.isWhitespace(buffer[fpoint])) {
	            // Add the character to the cache
	            cache[cacheIndex++] = buffer[fpoint++];
	        }

	        // Check if we are at the end of the buffer and still haven't found a complete token
	        if (fpoint == SIZE_OF_BUFFER || buffer[fpoint] == Main.EOF) {
	            // Handle the case when a token spans two buffers
	            if (lpoint != 0) {
	                // Copy the cache to the beginning of the buffer
	                System.arraycopy(cache, 0, buffer, 0, cacheIndex);
	                fillIndex = cacheIndex;
	                lpoint = 0;
	            }
	            // If the buffer is exhausted, we should reset the buffer
	            buffer = new char[SIZE_OF_BUFFER];
	            fillIndex = 0;
	            return;
	        }

	        // Process the token using the DFA simulator
	        char[] tokenChars = new char[cacheIndex];
	        System.arraycopy(cache, 0, tokenChars, 0, cacheIndex);

	        DFASimulator dfaSimulator = new DFASimulator();
	        TransitionTableController transitionController = TransitionTableController.getInstanceOf();
	        Collection<TransitionTable> transitionTables = transitionController.getTransitionTables();

	        // Check if the token is accepted by the DFA simulator
	        State acceptedState = dfaSimulator.isAccepted(transitionTables, tokenChars);

	        if (acceptedState != null) {
	            // Token is accepted, so extract and store it
	            String tokenValue = new String(tokenChars, 0, cacheIndex);
	            tokenContainer.addToken(tokenValue, acceptedState.getCurrentStateName(), currentlineNumber);
	        } else {
	            // Token is not accepted, print an error
	            printError();
	        }
	    } catch (Exception exception) {
	        System.out.println("Exception Occurred: " + exception);
	    }
	}

	private void printBuffer() {
		for (int loop = 0; loop < SIZE_OF_BUFFER; loop++) {
			System.out.print(buffer[loop]);
		}
		System.out.println("\n****\n");
	}

}
