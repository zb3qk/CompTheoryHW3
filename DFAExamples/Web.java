import java.util.*;
import java.util.stream.Collectors;

public class Web<T> {
    private HashSet<Node<String>> states;

    /* Node --> delta --> Node */

    //HashSet to all the Nodes to make them accesible via the delta objects

    public static class Node<String> extends Object{
        private String data;
        private Set<delta> deltas;
    }

    private void accept(Node<String> n){
        return; //Bad but ok
    }

    //private default Consumer<Node<String>> andThen()

    public String getNodeData(Node<String> node){ return node.data; }

    public static class delta<Character> {
        private Character data; ///character of the delta
        private Node<String> dest; ///destination of the delta
    }

    public Node<String> findNode(String name){
        Node<String> node;
        //System.out.println("BINGO!");
        System.out.println("BINGO2!");
        List<Node<String>> nodeList = states.stream()
                .filter( (n) -> n.data.equals(name))
                .collect(Collectors.toList());
        System.out.println("BINGO2!");
        if(!nodeList.isEmpty()) node = nodeList.get(0);
        else node = null;
        System.out.println("Node: " + node);
        return node;
    }

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

    public void addDelta(Node<String> start, Character d, String end){
        Node<String> endNode = findNode(end);
        if(endNode == null) endNode = createNode(end);
        delta delt = new delta();
        delt.data = d;
        delt.dest = endNode;

        start.deltas.add(delt);
    }

    public void addDelta(Node<String> start, Character d, Node<String> end){
        delta delt = new delta();
        delt.data = d;
        delt.dest = end;
        start.deltas.add(delt);
    }

    /**
     *
     * @param start Node type of the start node
     * @param d character for the delta
     * @return the ending node
     */
    public Node<String> nextNode(Node<String> start, Character d){
        delta delt = start.deltas.stream().filter( (de) -> de.equals(d)).findFirst().get();
        if(delt == null) return null;
        return delt.dest;
    }

    public static void main(String[] args){

    }

}