package interfaces;

public interface Queue {
    void put(int val);
    int get();
    boolean full();
    boolean empty();
}
