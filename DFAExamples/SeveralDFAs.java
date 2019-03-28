import java.util.*;
import java.util.stream.Collectors;

public class SeveralDFAs{

    public static DFA evenA(){
        /*
        * This is the automaton for the language evenA from class.
        */
        char[] alph = {'a','b'};
        Set<Character> alphabet = new HashSet<Character>();
        for (int i = 0; i < alph.length; i++){
            alphabet.add(alph[i]);
        }
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();
        
        states.add("E"); // We have 2 states: E and O
        states.add("O");
        
        String start = "E"; // The start state is state E
        
        finals.add("E"); // The only final state is state E
        
        delta.put(new Tuple("E",'a'), "O"); // when in state E, transition to state O on input a
        delta.put(new Tuple("E",'b'), "E");
        delta.put(new Tuple("O",'a'), "E");
        delta.put(new Tuple("O",'b'), "O");
        
        return new DFA(states, alphabet, delta, start, finals);
    }
    
    public static DFA tripleA(){
        /*
        * This is the automaton for the language tripleA from class.
        */
        char[] alph = {'a','b'};
        Set<Character> alphabet = new HashSet<Character>();
        for (int i = 0; i < alph.length; i++){
            alphabet.add(alph[i]);
        }
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();
        
        states.add("zero");
        states.add("one");
        states.add("two");
        String start = "zero";
        finals.add("zero");
        delta.put(new Tuple("zero",'a'), "one");
        delta.put(new Tuple("zero",'b'), "zero");
        delta.put(new Tuple("one",'a'), "two");
        delta.put(new Tuple("one",'b'), "one");
        delta.put(new Tuple("two",'a'), "zero");
        delta.put(new Tuple("two",'b'), "two");
        
        return new DFA(states, alphabet, delta, start, finals);
    }

    private static Map<Tuple, String> insertToDelta(Map<Tuple,String> delta, String start, char[] chars, String end){
        for(char c: chars) //trash
            delta.put(new Tuple(start,c), end);
        return delta;
    }

    /*
    ##########
    UNCOMMENT
    ##########
     */
    /*
    public static DFA tripleAndEvenA(){
        return DFAClosure.intersection(evenA(), tripleA());
    }
    */
    
    public static DFA compId(){
        /* HOMEWORK
        * This function should return a finite state automaton
        * which accepts all strings that are valid UVA computing ids.
        * A UVA computing ID is formatted as:
        * between 2-3 lowercase letters a-z
        * one digit between 2-9
        * between 1-3 lowercase letters a-z
        *
        * To keep your automata from appearing too cluttered,
        * we will restrict letters to be a-c and numbers 2-3
        */
        char[] alph = {'a','b','c','2', '3'};
        Set<Character> alphabet = new HashSet<Character>();
        for (int i = 0; i < alph.length; i++){
            alphabet.add(alph[i]);
        }
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();

        String start = "";
        states.add("char1_1");
        states.add("char1_2");
        states.add("char1_3");
        states.add("poop");
        states.add("num");
        states.add("char2_1");
        states.add("char2_2");
        states.add("char2_3");

        finals.add("char2_1");
        finals.add("char2_2");
        finals.add("char2_3");

        char[] chars = {'a', 'b', 'c'};
        char[] nums = {'2', '3'};

        //shrink it?
        delta = insertToDelta(delta,"poop",alph,"poop");

        delta = insertToDelta(delta,"start",chars,"char1_1");
        delta = insertToDelta(delta,"start",nums,"poop");

        delta = insertToDelta(delta,"char1_1",chars,"char1_2");
        delta = insertToDelta(delta,"char1_1",nums,"poop");

        delta = insertToDelta(delta,"char1_2",chars,"char1_3");
        delta = insertToDelta(delta,"char1_2",nums,"num");

        delta = insertToDelta(delta,"char1_3",chars,"poop");
        delta = insertToDelta(delta,"char1_3",nums,"num");

        delta = insertToDelta(delta,"num",chars,"char2_1");
        delta = insertToDelta(delta,"num",nums,"poop");

        delta = insertToDelta(delta,"char2_1",chars,"char2_2");
        delta = insertToDelta(delta,"char2_1",nums,"poop");

        delta = insertToDelta(delta,"char2_2",chars,"char2_3");
        delta = insertToDelta(delta,"char2_2",nums,"poop");

        delta = insertToDelta(delta,"char2_3",alph,"poop");

        return new DFA(states, alphabet, delta, start, finals);
    }


    /*
    BEGIN DFAs for the password DFA
     */
    private static DFA nElements(int n, char[] alph){ //count number of elements
        Set<Character> alphabet = new HashSet<Character>();
        for (int i = 0; i < alph.length; i++){
            alphabet.add(alph[i]);
        }
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();
        String start1 = "_0";

        int i = n;
            states.add("_0");
            delta = insertToDelta(delta,"_0",alph,"_0");
        while(i>0){
            String end = "_" + Integer.toString(i);
            String start = "_" + Integer.toString(i-1);
            states.add(end);
            delta = insertToDelta(delta,start,alph,end);
            i--;
        }
        finals.add("_" + Integer.toString(n));

        return new DFA(states, alphabet, delta, start1, finals);
    }

    private static DFA noRep(int n, char[] alph){
        Set<Character> alphabet = new HashSet<Character>();
        for (int i = 0; i < alph.length; i++){
            alphabet.add(alph[i]);
        }
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();
        String start1 = "_0";

        int i = 0;
        delta = insertToDelta(delta,"_0",alph,"_0");
        while(i<n){
            String end = "_" + Integer.toString(i+1);
            String start = "_" + Integer.toString(i);
            states.add(start);
            finals.add(start);
            delta = insertToDelta(delta,start,alph,end);
            i++;
        }
        states.add("_" + Integer.toString(n));

        return new DFA(states, alphabet, delta, start1, finals);
    }

    /*
    ######################################
    #           Cross Product            #
    ######################################
     */

//TODO: Must begin here
    private static Set<String> allPossStrings(Set<DFA> Q, String s, int depth, int depthMax, Set<String> states, Set<Character> alphabet){
        if (depth>=depthMax) return states;
        //evaluate current state
        String state = "";
        Q.forEach((q) -> state.concat("." + q.getState(s))); //in order of the states in the Set and "." as a delimiter
        states.add(state);
        //parse through states for each DFA
        alphabet.forEach((a) -> allPossStrings(Q, s+a, depth+1,depthMax,states,alphabet));
        return states;
    }

    private static DFA crossProduct(Set<DFA> Q){
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();

        String start1 = "";
        Q.forEach((qS) -> start1.concat(qS.start) );

        ArrayList<Character> repeatAlph = new ArrayList<Character>();
        Q.forEach((qA) ->
                qA.alphabet.forEach((q) -> repeatAlph.add(q)));
        Set<Character> alphabet = new HashSet<Character>();

        alphabet = repeatAlph.stream()
                .distinct()
                .collect(Collectors.toSet());

        /* Set up the states */
        int stringLength = 7; //longest string necessary to acquire all possible states
        states = allPossStrings(Q, "", 0, stringLength,states,alphabet);
        states = states.stream().distinct().collect(Collectors.toSet()); //remove duplicates of states

        return new DFA(states, alphabet, delta, start1, finals);
    }


    public static DFA password(){
        /*HOMEWORK
        * This function should return a finite state automaton
        * which checks whether or not the given string is a
        * "valid" password. The allowed characters for our 
        * passwords are {a,b,1,2,?,!}. To be valid, it must
        * satisfy the following rules:
        * It contains at least 2 letters, one number, and one punctuation
        * There are not 3 a's in a row
        * the total length of the string is at least 7 characters
        *
        * To approach this problem, I recommend making automata for each
        * requirement above, then combine them using closure properties.
        */
        char[] alph = {'a','b','1','2','?','!'};
        char[] nums = {'1','2'};
        char[] punc = {'?','!'};
        char[] lets = {'a','b'};
        char[] a = {'a'};

        //Create a 5-way tuple?

        DFA oneNum = nElements(1, nums);
        DFA onePunc = nElements(1,punc);
        DFA twoChar = nElements(2,lets);
        DFA sevenAll = nElements(7,alph);

        DFA aReps = noRep(3, a);


        return new DFA();
    }
    
    
    public static DFA hammingDistance(String match, int distance){
        /* HOMEWORK
        * For bioinformatics and network transmission, it's helpful 
        * to be able to measure how different various strings are
        * from one another. These metrics are often called string
        * distance. There are various methods from measuring string
        * distance, and which to use mostly depends on what is an
        * allowable change. 
        * For this problem we're asking you to write a function to
        * accept all strings that are within a certain Hamming
        * distance of another string (the match parameter).
        * The Hamming distance between two strings is the smallest
        * number of single-character substitutions that must be 
        * made to convert one string into the other.
        *
        * For example, if we invoked this function on:
        * match = "nate"
        * distance = 2
        *
        * The automaton should accept:
        * nate (distance 0, exact match)
        * gate (distance 1, substituting n->g)
        * note (distance 1, substituting a->o)
        * hath (distance 2, substituting n->h and e->h)
        * pale (distance 2, substituting n->p and t->l)
        *
        * The automaton should reject:
        * math (3 substitutions required)
        * rich (4 substitutions required)
        * naters (cannot be converted by substitution alone)
        *
        * To keep your automata from looking too cluttered,
        * we restrict our alphabet to be DNA bases (a,t,g,c).
        *
        * hint: you code will likely be simpler if you do this with
        * a cross product somewhere, but this is not required.
        */
        char[] alph = {'a','t','g','c'};
        Set<Character> alphabet = new HashSet<Character>();
        for (int i = 0; i < alph.length; i++){
            alphabet.add(alph[i]);
        }
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();
        String start;
        
        return new DFA();
    }
    
    public static void main(String[] args){
        //evenA().toDot(); // remove the comments from these lines separately to see the graph created.
        //tripleA().toDot(); // remove the comments from these lines separately to see the graph created.
        //tripleAndEvenA().toDot();
        //System.out.println(tripleAndEvenA().decide("ababaabababb"));
        compId().toDot();
        //hammingDistance("aaaa", 2).toDot();
        //System.out.println(hammingDistance("aaaa", 2).decide("atat")); //Should be true
        //System.out.println(hammingDistance("aaaa", 2).decide("aaat")); //Should be true
        //System.out.println(hammingDistance("aaaa", 2).decide("atgt")); //Should be false
    }
}
