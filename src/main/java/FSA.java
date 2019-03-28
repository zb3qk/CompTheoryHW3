import java.util.*;

public abstract class FSA implements Decider
{
    /*
     * A Finite State Automaton (FSA) is a kind of decider.
     * Its input is always a string, and it either accepts or rejects that string.
     * We're making this an abstract class for now (where not all parts are defined)
     * because there are many different kinds of finite state automata. So far we've
     * only looked at the simplest kind, but we'll be looking at more complex ones for
     * the next homework assignment.
     */

    public Set<String> states; // Q
    public Map<Tuple,String> delta; // \delta, this map's keys are state/character pairs, the values are states.
    public String start; // q_0
    public Set<String> finals; // F
    public Set<Character> alphabet; // \Sigma
    /*
    public boolean decide(String s){
        // run the machine on the string and accept/reject it.
        boolean accept = reset();
        accept = finals.contains(start);
        for(int i = 0; i < s.length(); i++){
            accept = step(s.charAt(i));
        }
        reset();
        return accept;
    }
    */

    /**
     * The decide mechanism was also changed to support the Web data structure, and take advantage of the
     * references between different nodes. If String s has bad inputs, getState has its own logic structure
     * take rectifies it for use with the Cross Product. IF BAD DELTAS ARE A POSSIBILITY, RECTIFY FOR IT OUTSIDE
     * OF THIS METHOD.
     * @param s a string of deltas (Characters)
     * @return true or false depending on whether or not the ending state is final
     */
    public boolean decide(String s){
        //String of deltas
        boolean deltas = finals.contains(getState(s));
        return deltas;
    }

    public abstract String getState(String deltas);
    public abstract boolean step(char c); // advance one transition
    public abstract boolean reset(); // return to start state to begin another input
    public abstract void toDot(); // give a dot representation of the automaton for visualization.

};
