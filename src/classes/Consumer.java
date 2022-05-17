package classes;

public class Consumer implements Runnable {

    private CycleQueue queue;

    public Consumer(CycleQueue queue) {
        this.queue = queue;
        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int i = 0; i < queue.getSIZE(); i++) {
            try {
                Thread.sleep(3000);
                queue.get();
                System.out.println(Thread.currentThread().getName() + "take product.");
            } catch (InterruptedException e) {
                System.out.println("Consumer is end.");
            }
        }
    }
}
