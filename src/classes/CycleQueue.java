package classes;

import interfaces.Queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//Класс очереди
public class CycleQueue implements Queue {
    //Размер очереди
    private final static int SIZE = 10;
    private final int[] values;
    //Указатели на начало и конец очереди
    private int start;
    private int end;

    //Блокировщик
    private ReentrantLock lock;
    //Условия блокировки
    private Condition consumer;
    private Condition producer;

    //Флаг того, что товар взяли со склада
    private boolean isTaken;

    //Конструктор
    public CycleQueue() {
        values = new int[SIZE];
        //Расстановка указателей
        start = 0;
        end = 9;

        lock = new ReentrantLock();
        consumer = lock.newCondition();
        producer = lock.newCondition();

        isTaken = false;
    }

    //Метод, получаем следующую позицию в очереди (с учётом цикла)
    private int nextPosition(int current){
        return ++current % SIZE;
    }

    @Override
    public void put(int val) throws InterruptedException {
        //Блокируем метод для других потоков
        lock.lock();
        try {
            //Если очередь полная - потоки-производители ожидают
            while (full())
                producer.await();

            //Сдвигаем позицию конца очереди вперед
            end = ++end % SIZE;
            //Кладём в очередь новый элемент
            values[end] = val;
            //Выводим состояние очереди на экран
            log();

            //Сигнализируем потокам-потребителям, что очередь не пуста и из неё можно брать товары
            consumer.signalAll();

            //Убираем флаг - товар не брали (а положили в очередь)
            isTaken = false;
        } finally {
            // В любом случае в конце снимаем блокировку (чтобы не было дедлока)
            lock.unlock();
        }
    }

    @Override
    public int get() throws InterruptedException {
        //Блокируем метод для других потоков
        lock.lock();
        try {
            //Если очередь пустая - потоки-потребители ожидают
            while (empty())
                consumer.await();

            //Сдвигаем позицию конца очереди вперед
            start = ++start % SIZE;
            //Выводим состояние очереди на экран
            log();
            //Сигнализируем потокам-производителям, что очередь не заполнена и в неё можно класть товары
            producer.signalAll();

            //Устанавливаем флаг - из очереди взят товар
            isTaken = true;
            return values[start];

        } finally {
            // В любом случае в конце снимаем блокировку (чтобы не было дедлока)
            lock.unlock();
        }
    }

    //Метод проверки очередь на заполненность
    @Override
    public boolean full() {
        return nextPosition(nextPosition(end)) == start;
    }

    //Проверка на пустоту
    @Override
    public boolean empty() {
        return nextPosition(end) == start;
    }

    //Метод, выводит заполненную часть очереди на экран
    private void log() {
        System.out.println("Queue State: ");
        int sPoint = start;
        int ePoint = end;

        if (empty()) {
            System.out.println("empty");
            return;
        }
        while (!(nextPosition(ePoint) == sPoint)) {
            System.out.print(values[sPoint] + " ");
            sPoint = nextPosition(sPoint);
        }
        System.out.println();
    }
    public int getSIZE() {
        return SIZE;
    }

    //Возвращает флаг, было ли что-то взято со склада (для производителя)
    public boolean getIsTaken() {
        return isTaken;
    }
}
