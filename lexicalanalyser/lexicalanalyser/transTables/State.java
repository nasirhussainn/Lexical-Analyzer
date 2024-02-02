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
 * It represents a state of the transition table
 * It also have the information that what will be the 
 * next states from the current state for some input values.
 * 
 * @author Faisal Aslam
 */
public class State {

    private String currentStateName = null;
  

    public State(String stateName) {
        if (stateName == null) {
            throw new UnsupportedOperationException("name of the state cannot be null");
        }
        //this.mineTT = mineTT;
        this.currentStateName = stateName;
    }

    public String getCurrentStateName() {
        return currentStateName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof State)) {
            return false;
        }
        State input = (State) obj;
        if (input.currentStateName.equals(currentStateName)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.currentStateName != null ? this.currentStateName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        String ret = currentStateName + " ";
        return ret;
    }
}
