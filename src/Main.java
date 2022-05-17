import classes.Consumer;
import classes.CycleQueue;
import classes.Producer;

public class Main {

    public static void main(String[] args) {
        CycleQueue queue = new CycleQueue();

        Producer producer = new Producer(queue);
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);
    }
}
