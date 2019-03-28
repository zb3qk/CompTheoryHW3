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
        /**
         * This is used to convert the provided DFA into the Web format
         */
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
        if(deltas == "") return start;
        return getState(deltas, this.web.findNode(start));
    }

    /**
     *
     * This process is meant to imagined as literally following instructions along a road. The Node indicates the location
     * and the delta indicates where to go. This is made possible via the Web data structure.
     * @param deltas a string of Characters from an alphabet as the decider input
     * @param n This is the current node that the path is on
     * @return returns the final node in the path once all the deltas have been used up
     */
    public String getState(String deltas, Web.Node<String> n){
        if(deltas.length() <= 0) return (web.getNodeData(n));
        Character delta = deltas.charAt(0);
        deltas = deltas.substring(1); //"abc" -> "bc"
        //System.out.println("delta: " + delta);
        //System.out.println("NODE: " + web.toString(n));
        //System.out.println("next NODE: " + web.toString(web.nextNode(n,delta)));
        //System.out.println();
        return getState(deltas, web.nextNode(n,delta));
    }

    public boolean isFinal(String state){
        //System.out.println("Interior Finals: " + finals);
        //System.out.println("Check State: " + state);
        //System.out.println("Final check " + finals.contains(state));
        return finals.contains(state);
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

    /**
     *
     * This function operates under the assumption that nodes that cycle to itself is not considered
     * within the given definition of a cycle
     *
     * @param nodePath A HashSet of nodes which have already been traversed
     * @param curNode The node that the method will currently process
     * @param prevIgaulNext Check if the next node is the same as the current Node to stop counting
     *                      This is done so that a false positive for a cycle is not reached
     * @param counts A very stupid way to do this, but makes 'n' positions in a HashSet
     *               and the total number of elements of determined by the length of this Set becuase the variables
     *               inside of a .forEach loop must be final or effectively final
     * @return returns the size of the counts HashSet.
     */
    public int langPath(HashSet<Web.Node> nodePath, Web.Node curNode, boolean prevIgaulNext, HashSet<Double> counts){
        if(prevIgaulNext) return 0;
        else if(nodePath.contains(curNode)) return -Integer.MAX_VALUE;
        nodePath.add(curNode);
        HashSet<Web.Node> newNodePath = (HashSet) nodePath.clone();
        Set<Web.delta> deltas = web.getDeltas(curNode);
        deltas.forEach((Web.delta d) -> {
            System.out.println("delta: " + web.toString(d));
            Web.Node nextNode = web.nextNode(curNode,web.getDeltaData(d));
            double ran = Math.random(); while(counts.contains(ran)) ran = Math.random(); counts.add(ran);
            langPath(newNodePath,nextNode,curNode == nextNode, counts);
        });
        return counts.size();
    }

    public int langSize(){
    /*
    *  EXTRA CREDIT:
    * Implement this method which will give the number of strings 
    * in the language of the automaton.
    * If it is infinite return -1.
    * As a hint, an automaton will have an infinite language
    * if and only if it has a cycle.
    * A cycle does not include a node looping back into itself
    */
        Web.Node startNode = web.findNode(start);
        //System.out.println("Node: " + startNode);
        HashSet<Web.Node> nodePath = new HashSet<>();
        int count = langPath(nodePath,startNode,false,new HashSet<>());
        if(count<0) return -1;
        else return count;
    }
}
