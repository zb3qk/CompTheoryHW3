
public interface Decider{
// A decider object has a decide method which takes a string as input and gives a boolean as output.
// returning true means the decider accepted the string, false means it rejected.
    public boolean decide(String s);  
}
