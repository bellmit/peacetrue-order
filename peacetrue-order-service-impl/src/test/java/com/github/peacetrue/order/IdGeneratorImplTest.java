package com.github.peacetrue.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : xiayx
 * @since : 2021-04-28 04:34
 **/
class IdGeneratorImplTest {

    @Test
    void basic() {
        System.out.println(new IdGeneratorImpl());
    }

    @Test
    void generateId() throws Exception {
        IdGeneratorImpl generator = new IdGeneratorImpl();
        List<String> serialNumbers = new LinkedList<>();
        List<Thread> threads = IntStream.range(0, 1000)
                .mapToObj(i -> new Thread(() -> serialNumbers.add(generator.generateId())))
                .collect(Collectors.toList());
        threads.forEach(Thread::start);
        for (Thread thread : threads) thread.join();
        Assertions.assertEquals(serialNumbers.stream().distinct().count(), serialNumbers.size());
    }
}
