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

    public static DFA tripleAndEvenA(){
        return DFAClosure.intersection(evenA(), tripleA());
    }

    
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

        String start = "start";
        states.add("start");
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

    private static DFA noRep(int n, char[] alph, char[] noRep){
        Set<Character> alphabet = new HashSet<Character>();
        for (int i = 0; i < alph.length; i++){
            alphabet.add(alph[i]);
        }
        Set<Character> noRepetitions = new HashSet<Character>();
        for (int i = 0; i < noRep.length; i++){
            noRepetitions.add(noRep[i]);
        }
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();
        String start1 = "_0";

        //remove chars in noRep from alph
        Set<Character> alphabetSub = alphabet.stream().filter((c) -> !noRepetitions.contains(c)).collect(Collectors.toCollection(HashSet::new));
        int i = 0;
        while(i<n){
            String end = "_" + Integer.toString(i+1);
            String start = "_" + Integer.toString(i);
            states.add(start); finals.add(start);
            alphabetSub.forEach((c) -> delta.put(new Tuple(start,c),"_0"));
            noRepetitions.forEach((c) -> delta.put(new Tuple(start,c),end));
            i++;
        }
        String end = "_" + Integer.toString(n);
            states.add(end);
            alphabet.forEach((c) -> delta.put(new Tuple(end,c),end));

        return new DFA(states, alphabet, delta, start1, finals);
    }

    /*
    ######################################
    #           Cross Product            #
    ######################################
     */

    /**
     *
     * This is the main mechanism for implementing the cross product of multiple DFAs. The general structure of this is
     * to physically traverse each DFA and create states based off of where each DFA is simultaneously. To make this
     * easier, the DFA's default operation for a character that is not within its alphabet, then the state loops back
     * to itself. This will be the case because the character (in most cases) will have a static effect on the DFA.
     * This will  have to be adjusted on a case by case basis, or each DFA will have to have a standardized library
     * before being ran through this method. This method passes through each possible path within Sigma* and optimizes
     * to prevent redundancy by ending the recursion on states that loop back into itself.
     *
     * A better optimization would be to check for the new delta or state in the HashSet/HashMap (since the Find method is Theta(1)). There is also
     * a piece of outside information that I have not been able to derive just from the DFAs, the length of longest
     * string in the Sigma* language that traverses through all possible states. For the password DFA, the longest string
     * will be 8 characters long. If we were to use the redundancy check described above, then this depth parameter
     * would not even be needed since the Path will end on any sort of redundancy, making the method incredibly efficient.
     * This implementation also assumes for the Intersection of DFAs since a Final state will only be added if the path
     * in each DFA is Final. This can be adjusted to be more flexible to different types of desired valid states such
     * as Union.
     *
     * @param Q is the set of DFAs taken the cross product of
     * @param s current string (set of deltas to get to the current position)
     * @param states the growing set of states for the Cross Product
     * @param alphabet the alphabet of the cross product
     * @param finals the valid states of the cross product
     * @param delta the set of deltas of the cross product
     * @param prevState the previous state that was traversed (this is used to prevent redundancies in looping states).
     * @param curPath a HashSet of all Nodes along a given path
     * @return The set of all states, although this could have been void since Sets are update by reference and not by value
     */
    public static void allPossStrings(Set<DFA> Q, String s, Set<String> states,
                                      Set<Character> alphabet, Set<String> finals, HashMap<Tuple, String> delta,
                                      String prevState, HashSet<Web.Node> curPath){
        if(curPath.contains(prevState)) return;
        //evaluate current
        //System.out.println("inputString: " + s);
        StringBuilder b = new StringBuilder();
        boolean badIn = false;
        boolean finale = true;
        for (DFA q:Q){ //in order of the states in the Set and "." as a delimiter
            String st = q.getState(s);
            //System.out.println("q Finals: " + q.finals);
            //System.out.println("curSubState: " + st);
            if(!q.isFinal(q.getState(s))) finale=false; ///check if state could be final
            if(st != null) b.append(q.getState(s));
            else { badIn = true; break; }
        }
        //System.out.println("finale value: " + finale);
        String curState = b.toString();
        //TODO make sure delta is correct
        if(s.length() != 0) delta.put(new Tuple(prevState,s.charAt(s.length()-1)),curState); //I HOPE THIS IS CORRECT
        //System.out.println("Full state: " + curState);
        //System.out.println();

        if(finale) finals.add(curState); ///add to finals if final
        if(!badIn) states.add(curState); ///if a state is null, then dont follow through
        //parse through states for each DFA
        //System.out.println("Finals(s): " + finals);
        alphabet.forEach((a) -> {
            HashSet<Web.Node> path = new HashSet<Web.Node>();
            path = (HashSet<Web.Node>) curPath.clone();
            allPossStrings(Q, s + a, states, alphabet, finals, delta, curState, path);
        });
        return;
    }

    /**
     * This is the helper method to build the cross product of a set of DFAs
     * @param Q the Set of DFAs
     * @return the DFA of the cross product of Q
     */
    public static DFA crossProduct(Set<DFA> Q){
        Set<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        HashMap<Tuple, String> delta = new HashMap<Tuple, String>();

        StringBuilder b = new StringBuilder();
        Q.forEach((qS) -> b.append(qS.start) );
        String start1 = b.toString();

        ArrayList<Character> repeatAlph = new ArrayList<Character>();
        Q.forEach((qA) ->
                qA.alphabet.forEach((q) -> repeatAlph.add(q)));
        Set<Character> alphabet = new HashSet<Character>();

        alphabet = repeatAlph.stream()
                .distinct()
                .collect(Collectors.toSet());

        /* Set up the states & finals*/
        int stringLength = 8; //longest string necessary to acquire all possible states
        allPossStrings(Q, "", states,alphabet, finals, delta, start1, new HashSet<Web.Node>());
        states = states.stream().distinct().collect(Collectors.toSet()); //remove duplicates of states
        //System.out.println("Finals: " + finals);
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
        DFA aReps = noRep(3, alph,a);

        Set<DFA> Q = new HashSet<DFA>();
        Q.add(oneNum);Q.add(onePunc);Q.add(twoChar);Q.add(sevenAll);Q.add(aReps);
        DFA result = crossProduct(Q);
        return result;
    }
    

    public static int positionOf(char[] chars, char c){
        for(int i=0;i<chars.length;i++){
            if(chars[i] == c) return i;
        }
        return -1;
    }

    public static ArrayList<Character> listClone(ArrayList<Character> chars){
        ArrayList<Character> newList = new ArrayList<>();
        for(Character c: chars){
            newList.add(c);
        }
        return newList;
    }

    //chars is initially the full alphabet
    //if hem = 0, chars.size() == AlphSubHem
    static void hammingD(String match, Set<Character> alphabet, HashSet states, Map<Tuple,String> delta, String input,
                         int nullCount, int depth, int numVa, int distance){
        if(numVa >= match.length()- distance){ //review
            alphabet.forEach((c) -> delta.put(new Tuple(input,c),"valid"));
            return;
        }
        if(nullCount <= 0) {
           boolean skipjump =  numVa + 1>= match.length()- distance;
            alphabet.forEach((c) -> {
                if(match.charAt(depth) == c && skipjump){
                    delta.put(new Tuple(input, c), "valid");
                }
                else delta.put(new Tuple(input, c), "poop");
            });
            return;
        }

        states.add(input);
        alphabet.forEach((c) -> {
            String curState = input;
            String nextState;
            if(match.charAt(depth) == c) {
                nextState = input + "Va"; //Va indicates a consistent lettering
                hammingD(match, alphabet, states, delta, nextState, nullCount, depth+1,numVa+1,distance);
            }
            else {
                nextState = "Nu" + input; //Nu indicates an inconsistent lettering
                hammingD(match,alphabet,states,delta,nextState,nullCount-1,depth+1,numVa,distance);
            }
            delta.put(new Tuple(curState,c),nextState);
        });
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
        HashSet<String> states = new HashSet<String>();
        Set<String> finals = new HashSet<String>();
        Map<Tuple, String> delta = new HashMap<Tuple, String>();
        String start;


        int length = match.length();
        char[] matchChars = match.toCharArray();
        char[] initial = new char[length];
        for (int i=0; i<length;i++){
            initial[i] = alph[0];
        }

        finals.add("valid");
        alphabet.forEach((c) -> delta.put(new Tuple("valid",c),"valid"));

        states.add("poop");
        alphabet.forEach((c) -> delta.put(new Tuple("poop",c),"poop"));

        hammingD(match,alphabet,states,delta,"start", distance,0,0,distance);
        states = states.stream().distinct().collect(Collectors.toCollection(HashSet::new));

        return new DFA(states,alphabet,delta,"start",finals);
    }
    
    public static void main(String[] args){
        //evenA().toDot(); // remove the comments from these lines separately to see the graph created.
        //tripleA().toDot(); // remove the comments from these lines separately to see the graph created.
        //tripleAndEvenA().toDot();
        //System.out.println(tripleAndEvenA().decide("ababaabababb"));
        compId().toDot();
        //password().toDot();
        //System.out.println(password().decide("aa2!bbabbbbbbbb"));
        //System.out.println(compId().langSize());
        //hammingDistance("aaaa", 2).toDot();
        //System.out.println(hammingDistance("aaaa", 2).decide("atat")); //Should be true
        //System.out.println(hammingDistance("aaaa", 2).decide("aaat")); //Should be true
        //System.out.println(hammingDistance("aaaa", 2).decide("atgt")); //Should be false
    }
}
