import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CircularBuffer<T> {
    private int head;
    private int tail;
    private int size;
    private final int capacity;
    private final T[] buffer;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition nonEmpty = lock.newCondition();

    @SuppressWarnings("unchecked")
    public CircularBuffer (int capacity) {
        this.capacity = capacity;
        this.buffer = (T[]) new Object[capacity];
        this.head = 0;
        this.tail = 0;
        size = 0;
    }

    public T poll() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                nonEmpty.await();
            }
            T res = buffer[head];
            buffer[head] = null;
            head = (head + 1) % capacity;
            size--;
            notFull.signal();
            return res;
        } finally {
            lock.unlock();
        }
    }

    public void offer(T item) throws InterruptedException {
        lock.lock();
        try {
            while (size == capacity) {
                notFull.await();
            }
            buffer[tail] = item;
            tail = (tail + 1) % capacity;
            size++;
            nonEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
}
