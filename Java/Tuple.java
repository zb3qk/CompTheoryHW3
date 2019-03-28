public class Tuple {
// This class is used to represent the input to the delta function
// It contains a String for the name of the current state, and
// a character for the input which that transition matches.


    public final String state;
    public final char input_char;
    
    public Tuple(String s, char c) {
        this.state = s;
        this.input_char = c;
    }
    
    public boolean equals(Object o){
        if (this == o) return true;
        if(!(o instanceof Tuple)) return false;
        Tuple t = (Tuple) o;
        return (this.state).equals(t.state) && this.input_char == t.input_char;
    }
    
    public int hashCode(){
        return this.state.hashCode();
    }

}
