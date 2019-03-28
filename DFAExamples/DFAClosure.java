import java.util.*;

public class DFAClosure{
    
    public static DFA complement(DFA dfa){
        /*
        * returns a DFA whose language is the complement of the given
        */
        Set<String> states = dfa.states;
        Set<String> old_finals = dfa.finals;
        Set<String> new_finals = new HashSet<String>();
        for(String state : states){
            if(!old_finals.contains(state)){
                new_finals.add(state);
            }
        }
        return new DFA(dfa.states, dfa.alphabet, dfa.delta, dfa.start, new_finals);
    }
    

    private static String cross_label(String state1, String state2){
        /*
        * This function is what determines what state labels look like in cross-product
        * You're welcome to change it if you'd like, but I don't know why you'd want to.
        */
        return state1 + "_" + state2;
    }


    public static DFA crossProduct(DFA dfa1, DFA dfa2, FinalChecker check){
        /*
        * returns a machine that is the cross product of dfa1 and dfa2.
        * the FinalChecker has a method called isFinal that looks at a
        * pair of states to determine whether that combined state should
        * be a final state.
        */
        HashSet<String> cross_states = new HashSet<String>();
        HashMap<Tuple,String> cross_delta = new HashMap<Tuple,String>();
        HashSet<String> cross_finals = new HashSet<String>();
        String cross_start = cross_label(dfa1.start, dfa2.start);
        for(String state1 : dfa1.states){
            for(String state2 : dfa2.states){
                String label = cross_label(state1, state2);
                cross_states.add(label);
                if (check.isFinal(state1, state2)){
                    cross_finals.add(label);
                }
                for(char c : dfa1.alphabet){
                    String dest1 = dfa1.delta.get(new Tuple(state1, c));
                    String dest2 = dfa2.delta.get(new Tuple(state2, c));
                    String cross_dest = cross_label(dest1,dest2); 
                    cross_delta.put(new Tuple(label, c), cross_dest);
                }
            }
        }
        
        return new DFA(cross_states, dfa1.alphabet, cross_delta, cross_start, cross_finals);
        
    }
    
    public static DFA intersection(DFA dfa1, DFA dfa2){
        // Gives a DFA for the intersection of dfa1 and dfa2
        return crossProduct(dfa1, dfa2, new Intersector(dfa1, dfa2));
    }
    
    public static DFA union(DFA dfa1, DFA dfa2){
        // Gives a DFA for the union of dfa1 and dfa2
        return crossProduct(dfa1, dfa2, new Unioner(dfa1, dfa2));
    }
    
    public static DFA difference(DFA dfa1, DFA dfa2){
        // Gives a DFA for the difference of dfa1 and dfa2
        return intersection(dfa1, complement(dfa2));
    }
    
    
    
    

}
