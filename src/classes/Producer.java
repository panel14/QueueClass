package classes;

//Класс производителя
public class Producer implements Runnable {

    //Очередь, с которой производится работа
    private CycleQueue queue;
    //Значение добавляемого элемента
    private int product;

    //Конструктор класса
    public Producer(CycleQueue queue) {
        this.queue = queue;
        product = 1;
        //Запуск текущего потока
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            //Если из очереди был взят товар -> увеличиваем его значение на 1
            if (queue.getIsTaken()) product++;
            //Кладём товар в очередь
            try {
                queue.put(product);
                System.out.println("Producer put a product.");
            } catch (InterruptedException e) {
                System.out.println("Producer is end.");
            }
        }
    }
}
