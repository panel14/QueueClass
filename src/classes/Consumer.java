package classes;

//Класс потребителя
public class Consumer implements Runnable {
    //Очередь, с которой производится работа
    private CycleQueue queue;

    //Конструктор класса
    public Consumer(CycleQueue queue) {
        this.queue = queue;
        //Запуск текущего потока
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            int count = (int)(Math.random() * (10 - 1)) + 1;
            try {
                for (int i = 0; i < count; i++) {
                    //Берём товар из очереди
                    queue.get();
                    System.out.println(Thread.currentThread().getName() + "take product.");
                }
                //Ненадолго останавливаем текущий поток
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Consumer is end.");
            }
        }
    }
}
