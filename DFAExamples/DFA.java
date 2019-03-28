import java.util.*;

public class DFA extends FSA
{
    public String active;
    public Web web;
    
    public DFA(Set<String> states, Set<Character> alphabet, Map<Tuple,String> delta, String start, Set<String> finals){
        this.states = states;
        this.delta = delta;
        this.start = start;
        this.finals = finals;
        this.alphabet = alphabet;
        this.active = start;
        this.web = new Web();
        /* Should build the entire DFA in the Web format */
        delta.forEach((Tu,end) -> { Web.Node startNode = web.createNode(Tu.state);
                                    Web.Node endNode = web.createNode(end);
                                    web.addDelta(startNode,Tu.input_char,endNode); });


        /* Create Linked List Structure to have each state connect to another state or itself via intermediate nodes */
        /* (state) --> (transition character) --> (next state) */
    }
    
    public DFA(){
        // empty constructor used only for a placeholder
        this.states = new HashSet<String>();
        this.delta = new HashMap<Tuple, String>();
        this.start = "";
        this.finals = new HashSet<String>();
        this.alphabet = new HashSet<Character>();
        this.active = this.start;
    }

    /**
     * Makes use of the Web datatype to traverse through the DFA
     * @param deltas an array of characters (deltas)
     * @return String of the return state
     */
    public String getState(String deltas){
        return getState(deltas, this.web.findNode(start));
    }

    public String getState(String deltas, Web.Node n){
        if(deltas.length() <= 0) return (web.getNodeData(n));
        Character delta = deltas.charAt(0);
        deltas = deltas.substring(1); //"abc" -> "bc"
        return getState(deltas, web.nextNode(n,delta));
    }

    public boolean step(char c){
        // Take one transition in the machine
        Tuple t = new Tuple(active, c);
        active = delta.get(t);
        return finals.contains(active);
    }
    
    public boolean reset(){
        // resets the automaton to run on a new string
        // returns active state to be start state
        active = start;
        return finals.contains(start);
    }    
    
    public void toDot(){
        /*
        * Prints out a description of the automaton in dot,
        * which is a graph specification language.
        * I recommend going to this url to convert it into
        * an image: https://dreampuf.github.io/GraphvizOnline/
        */
        System.out.println("digraph G {");
        
        String start_shape;
        if (this.finals.contains(start)){
            start_shape = "doubleoctagon";
        }
        else {
            start_shape = "octagon";
        }
        System.out.println("node [shape = " + start_shape + "]; " + this.start + ";");
        
        String double_circled = "node [shape = doublecircle];";
        boolean additional_finals = false;
        for(String s : this.finals){
            if (s.equals(start)){
                continue;
            }
            double_circled += " " + s;
            additional_finals = true;
        }
        if(additional_finals){
            System.out.println(double_circled + ";");
        }
        
        String single_circled = "node [shape = circle];";
        boolean additional_states = false;
        for(String s : this.states){
            if (s.equals(start) || finals.contains(s)){
                continue;
            }
            single_circled += " " + s;
            additional_states = true;
        }
        if (additional_states){
            System.out.println(single_circled);
        }
        
        for (Tuple in : delta.keySet()){
            String transition = "";
            transition += in.state + " -> " + delta.get(in) + " [ label = \"" + in.input_char + "\" ];";
            System.out.println(transition);
        }
        
        System.out.println("}");
    }
    
    public int langSize(){
    /*
    *  EXTRA CREDIT:
    * Implement this method which will give the number of strings 
    * in the language of the automaton.
    * If it is infinite return -1.
    * As a hint, an automaton will have an infinite language
    * if and only if it has a cycle.
    */
        return -1;
    }
}
