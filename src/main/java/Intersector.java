public class Intersector implements FinalChecker{
    
    public DFA dfa1;
    public DFA dfa2;
    
    public Intersector(DFA dfa1, DFA dfa2){
        this.dfa1 = dfa1;
        this.dfa2 = dfa2;
    }
    
    public boolean isFinal(String state1, String state2){
        // the crossproduct state is final if both source states are final
        return dfa1.finals.contains(state1) && dfa2.finals.contains(state2);
    }

}
