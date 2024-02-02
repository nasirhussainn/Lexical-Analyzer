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

import java.io.*;
import lexicalanalyser.dataObj.TokenContainer;
import lexicalanalyser.transTables.TransitionTableController;

/**
 * This is from where the program starts.
 * 
 * @author Faisal Aslam
 */
public class Main {

	public static final char EOF = (char) -1;

	/**
	 * The main should not take any arguments. It should read from input.tpl
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		/**
		 * read the transition tables
		 */
		TransitionTableController.getInstanceOf().read();

		/**
		 * now read source file and pass it to the input buffer.
		 */
		InputStream in = new FileInputStream("input.tpl");
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(in, "US-ASCII"));
		int intch;
		InputBuffer ibuff = new InputBuffer();
		while ((intch = buffReader.read()) != -1) {
			char ch = (char) intch;
			ibuff.addInBuff(ch);
		}
		ibuff.addInBuff(EOF);
		buffReader.close();
		in.close();
		/**
		 * This will create symbols and token files with all the symbols and
		 * tokens in them.
		 */
		TokenContainer.getInstanceOf().writeInFile();
		System.out.println("\n\n Done! Check the token.txt and symboltbl.txt file");
	}
}
