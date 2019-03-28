public class Unioner implements FinalChecker{
    
    public DFA dfa1;
    public DFA dfa2;
    
    public Unioner(DFA dfa1, DFA dfa2){
        this.dfa1 = dfa1;
        this.dfa2 = dfa2;
    }
    
    public boolean isFinal(String state1, String state2){
        // The crossproduct state should be final if either source state is final.
        return dfa1.finals.contains(state1) || dfa2.finals.contains(state2);
    }

}
