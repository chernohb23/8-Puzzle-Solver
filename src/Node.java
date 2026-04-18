public class Node {
    String state; // State of the node
    int g; // g(n) value
    int h; // h(n) value
    int f; // f(n) value

    Node(String state, int g, int h){ // Constructor
        this.state = state;
        this.g = g;
        this.h = h;
        this.f = g + h; // Calculate the f(n) value
    }
}