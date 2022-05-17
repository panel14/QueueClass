package classes;

public class Producer implements Runnable {

    private CycleQueue queue;
    private int product;

    public Producer(CycleQueue queue) {
        this.queue = queue;
        product = 1;
        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int i = 0; i < queue.getSIZE(); i++) {
            if (queue.getIsTaken()) product++;
            queue.put(product);
            try {
                Thread.sleep(1000);
                System.out.println("Producer put a product.");
            } catch (InterruptedException e) {
                System.out.println("Producer is end.");
            }
        }
    }
}
