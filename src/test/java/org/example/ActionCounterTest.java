package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActionCounterTest {

    @Test
    void testOneThreadUniques(){
        ActionCounter counter = new ActionCounter();
        callCounter(counter,11,22,33,34,329);
        assertEquals(3, counter.getActions(330));
    }

    @Test
    void testOneThreadWithEquals(){
        ActionCounter counter = new ActionCounter();
        callCounter(counter,11,11,33,33,33,34,35,35,330,330);
        assertEquals(8, counter.getActions(330));
    }


    @Test
    void testMultiThreadedWithUniques() throws InterruptedException {
        ActionCounter counter = new ActionCounter();
        List<Thread> threads = List.of(
                new Thread(() -> callCounter(counter, 100, 101, 102, 103, 104)),
                new Thread(() -> callCounter(counter, 106, 107, 108, 109, 110)),
                new Thread(() -> callCounter(counter, 111, 112, 113, 114, 115))
        );
        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(15, counter.getActions(400));
    }


    @Test
    void testMultiThreadedWithEquals() throws InterruptedException {
        ActionCounter counter = new ActionCounter();

        List<Thread> threads = List.of(
                new Thread(() -> callCounter(counter, 100, 100, 100, 100, 101)),
                new Thread(() -> callCounter(counter, 101, 100, 101, 100, 101)),
                new Thread(() -> callCounter(counter, 100, 101, 101, 100, 101))
        );

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(15, counter.getActions(400));
    }

    @Test
    void testLessCloseToMinSeconds(){
        ActionCounter counter = new ActionCounter();
        callCounter(counter, 1,1,1,2,2,2);
        assertEquals(6, counter.getActions(2));
    }

    @Test
    void testOnNoCallsMade(){
        ActionCounter counter = new ActionCounter();
        assertEquals(0, counter.getActions(500));
    }

    @Test
    void shouldThrowBadInput(){
        ActionCounter counter = new ActionCounter();
        assertThrows(IllegalArgumentException.class, ()-> counter.call(-1));
        assertThrows(IllegalArgumentException.class, ()-> counter.getActions(-9));
    }




    private void callCounter(ActionCounter counter, Integer ... timestamps){
        for (Integer timestamp : timestamps)
            counter.call(timestamp);
    }

}