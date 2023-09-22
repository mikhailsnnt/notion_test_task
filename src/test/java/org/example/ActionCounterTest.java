package org.example;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActionCounterTest {

    @RepeatedTest(value = 5)
    void testOnRandomVals(){
        Random random = new Random();
        CounterStupidProxy counter = new CounterStupidProxy();
        callCounterRandom(counter, 100,1000);
        int timestampToQuery = random.nextInt(1001,1300);
        int expected = counter.getStupidSolution(timestampToQuery);
        assertEquals(expected, counter.getActions(timestampToQuery));
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
    void testOneCallMade(){
        CounterStupidProxy counter = new CounterStupidProxy();
        counter.call(500);
        assertEquals(1, counter.getActions(500));
    }

    @Test
    void shouldThrowBadInput(){
        ActionCounter counter = new ActionCounter();
        assertThrows(IllegalArgumentException.class, ()-> counter.call(-1));
        assertThrows(IllegalArgumentException.class, ()-> counter.getActions(-9));
    }

    private void callCounterRandom(ActionCounter counter, int minCalls, int maxCalls){
        Random random = new Random();
        Stream.generate(() -> random.nextInt(0,1000))
                .limit(random.nextInt(minCalls , maxCalls))
                .sorted()
                .forEach(counter::call);
    }

    private void callCounter(ActionCounter counter, Integer ... timestamps){
        for (Integer timestamp : timestamps)
            counter.call(timestamp);
    }
}