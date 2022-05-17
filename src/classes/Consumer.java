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
        for (int i = 0; i < queue.getSIZE(); i++) {
            try {
                //Ненадолго останавливаем текущий поток
                Thread.sleep(3000);
                //Берём товар из очереди
                queue.get();
                System.out.println(Thread.currentThread().getName() + "take product.");
            } catch (InterruptedException e) {
                System.out.println("Consumer is end.");
            }
        }
    }
}
