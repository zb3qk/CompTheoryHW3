import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * The Web Data structure is a generalizable system defined by Nodes and deltas. The way the Web is set up, the Node
 * can represent location and the delta represents a change in location. This is implemented by references, where Nodes
 * reference to a series of deltas, and deltas reference to other nodes. Deltas can also be associated with the directed
 * edges in a connection. There are various methods in this class to help traverse this Web via String inputs.
 *
 * In retrospect, it probably would have been smarter to make a different class for Node and delta so that these
 * individual data types can be manipulated via methods of their own instead of adapting Web for this purpose.
 */
public class Web<T> {
    private HashSet<Node<String>> states;

    /* Node --> delta --> Node */

    //HashSet to all the Nodes to make them accesible via the delta objects
    public Web(){
        states = new HashSet<Node<String>>();
    }

    /**
     *
     * @data A Node has a 'data' element which is used to give the Node an identity, or a name
     * @deltas is an ArrayList of possible paths from the given node
     */
    public static class Node<String> extends Object{
        private String data;
        private Set<delta> deltas;
    }

    /**
     *
     * @param n
     * @return the Node's information in a readable String format
     */
    public String toString(Node<String> n){
        String print = "Node<String>";
        String data = "[State: " + n.data;
        String deltas = ", deltas(";
        StringBuilder b = new StringBuilder();
        n.deltas.forEach( (d) -> b.append(this.toString(d)));
        String vals = b.toString();
        deltas = deltas + vals + ")]";
        print = print + data + deltas;
        return print;
    }

    /**
     *
     * @param d
     * @return the delta's information in a readable String format
     */
    public String toString(delta<Character> d){
        String print = "delta(" + d.data + ", " + d.dest.data + ") ";
        return print;
    }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        this.states.forEach((s) -> b.append(this.toString(s) + " "));
        String s = b.toString();
        return s;
    }

    //private default Consumer<Node<String>> andThen()

    public String getNodeData(Node<String> node){ if(node == null) return null; return node.data; }
    public Character getDeltaData(delta<Character> d){ if(d == null) return null; return d.data; }

    /**
     *
     * @data an identity for the delta to take on, in this case an input Character
     * @dest the Node that this delta points to
     */
    public static class delta<Character> {
        private Character data; ///character of the delta
        private Node<String> dest; ///destination of the delta
    }

    /**
     * A Node may be needed to be used via a given state name, and this method enables this behavior. This is run in
     * O(n) runtime. In reality, if the list of all Nodes were put in a HashTable sorted by Node.data, then this could
     * be executed in Theta(1) runtime.
     * @param name The 'data' parameter of the Node
     * @return the Node that is associated with the 'data' parameter
     */
    public Node<String> findNode(String name){
        Node<String> node;
        if(states == null || states.size()==0) return null;
        List<Node<String>> nodeList = states.stream()
                .filter( (n) -> n.data.equals(name))
                .collect(Collectors.toList());
        if(!nodeList.isEmpty()) node = nodeList.get(0);
        else node = null;
        return node;
    }

    /**
     * This is designed to initialize a Node
     * @param name represents the data in the Node
     * @return the initialized Node Data Type
     */
    public Node<String> createNode(String name){
        Node<String> node = findNode(name);
        if(node == null) {
            Node<String> newNode = new Node<String>();
            newNode.data = name;
            newNode.deltas = new HashSet<delta>();
            states.add(newNode);
            return newNode;
        }
        else return node;
    }

    /**
     * This is meant to complement the createNode method to add deltas to the new Node
     * @param start the source Node
     * @param d the delta.data
     * @param end the destination node
     */
    public void addDelta(Node<String> start, Character d, Node<String> end){
        delta delt = new delta();
        delt.data = d;
        delt.dest = end;
        start.deltas.add(delt);
    }

    /**
     * This method is our traversal mechanism, taking advantage of a Node's reference to a delta, and the delta's
     * reference to the next Node.
     *
     * If the delta  does not exist within the node, then the same node is returned. This choice was made to support
     * the cross product mechanism. If an unknown character were passed through the DFA, then it would break the system,
     * and thusly when conducting a Cross Product (within the scope of the problems given in this assignment), this choice
     * would not break the system. (This logic would break in an NDA system)
     * @param start Node type of the start node
     * @param d character for the delta
     * @return the ending node
     */
    public Node<String> nextNode(Node<String> start, Character d){
        delta delt;
        if(start == null) return null;
        Optional<delta> op = start.deltas.stream().filter( (de) -> de.data.equals(d) ).findFirst();
        //System.out.println(op.isPresent());
        if(op.isPresent()) delt = op.get();
        else return start; //could be null, but this breaks everything else (ALWAYS??!? WHY?!?)

        if(delt == null) return null;
        return delt.dest;
    }

    public Set<Web.delta> getDeltas(Node<String> n){
        return n.deltas;
    }

    public static void main(String[] args){
        Web web = new Web();
        Node n = web.createNode("hi");
        Node n1 = web.createNode("bye");
        web.addDelta(n,'c',n1);
        web.addDelta(n, 'a', n);

        System.out.println(web);
    }

}