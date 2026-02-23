# LLD - Thread-Safe Fixed-Size Circular Buffer

A thread-safe, fixed-size circular buffer implementation in Java using `ReentrantLock` and `Condition` variables for producer-consumer synchronization.

## Overview

This project implements a **circular buffer** (also known as a ring buffer) with thread-safe operations. The buffer has a fixed capacity and implements blocking operations for both producers and consumers, making it suitable for concurrent programming scenarios.

## Features

- **Fixed-Size Capacity**: Buffer has a predetermined maximum size
- **Thread-Safe Operations**: Uses `ReentrantLock` and `Condition` variables for safe concurrent access
- **Blocking Operations**:
  - `offer(T item)`: Blocks if the buffer is full until space becomes available
  - `poll()`: Blocks if the buffer is empty until data becomes available
- **Generic Implementation**: Works with any data type using Java generics
- **Efficient Memory Usage**: Circular design reuses buffer space without allocation overhead

## Project Structure

```
src/
├── CircularBuffer.java    # Core circular buffer implementation
└── Main.java              # Entry point with usage examples
```

## Class Overview

### CircularBuffer<T>

A generic circular buffer class with the following key components:

#### Fields:
- `head`: Index pointing to the first element
- `tail`: Index pointing to the next insertion position
- `size`: Current number of elements in the buffer
- `capacity`: Maximum buffer size
- `buffer`: Underlying array storage
- `lock`: ReentrantLock for synchronization
- `notFull`: Condition for producer threads waiting for space
- `nonEmpty`: Condition for consumer threads waiting for data

#### Methods:

**`CircularBuffer(int capacity)`**
- Constructor that initializes the circular buffer with a given capacity

**`void offer(T item) throws InterruptedException`**
- Adds an item to the buffer
- Blocks if the buffer is full until space becomes available
- Signals waiting consumers when item is added

**`T poll() throws InterruptedException`**
- Removes and returns an item from the buffer
- Blocks if the buffer is empty until data becomes available
- Signals waiting producers when space becomes available

## How It Works

1. **Circular Indexing**: Uses modulo arithmetic (`% capacity`) to wrap indices around
2. **Producer-Consumer Pattern**: Multiple threads can safely produce and consume items
3. **Condition Variables**:
   - `nonEmpty`: Consumers wait on this when buffer is empty
   - `notFull`: Producers wait on this when buffer is full

## Usage Example

```java
// Create a circular buffer with capacity of 10
CircularBuffer<Integer> buffer = new CircularBuffer<>(10);

// Producer thread
new Thread(() -> {
    try {
        for (int i = 0; i < 100; i++) {
            buffer.offer(i);
            System.out.println("Produced: " + i);
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}).start();

// Consumer thread
new Thread(() -> {
    try {
        for (int i = 0; i < 100; i++) {
            Integer item = buffer.poll();
            System.out.println("Consumed: " + item);
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}).start();
```

## Thread Safety

The implementation uses:
- **ReentrantLock**: Ensures mutual exclusion for all buffer operations
- **Condition Variables**: Allows threads to wait/signal based on buffer state
- **Lock/Unlock Pattern**: All critical sections are protected with try-finally blocks

## Time Complexity

- `offer(T item)`: O(1) amortized
- `poll()`: O(1) amortized

## Space Complexity

- O(capacity): Fixed buffer size regardless of actual items stored

## Building and Running

### Compile
```bash
javac src/*.java
```

### Run
```bash
java -cp src Main
```

## Use Cases

- Producer-consumer queue systems
- Thread pool task queues
- Rate limiting and buffering
- Multi-threaded applications requiring bounded queues
