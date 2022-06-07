package interfaces;

public interface Queue {
    void put(int val) throws InterruptedException;
    int get() throws InterruptedException;
    boolean full();
    boolean empty();
}
