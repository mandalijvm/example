package io.mandali.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

public class StabilityUnitTest {

    @RepeatedTest(100)
    public void shouldIndicateStringBuilderIsNotThreadSafe() throws InterruptedException {
        StringBuilder sharedBuilder = new StringBuilder();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    sharedBuilder.append("A");
                } catch (Exception ignored) {
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    sharedBuilder.append("B");
                } catch (Exception ignored) {
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    sharedBuilder.append("B");
                } catch (Exception ignored) {
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("sharedBuilder.length():" + sharedBuilder.length() + (sharedBuilder.length() != 3000 ? "❌" : ""));
    }

    @RepeatedTest(100)
    public void shouldVerifyStringBufferIsThreadSafe() throws InterruptedException {
        StringBuffer sharedBuffer = new StringBuffer();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    sharedBuffer.append("A");
                } catch (Exception ignored) {
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    sharedBuffer.append("B");
                } catch (Exception ignored) {
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    sharedBuffer.append("B");
                } catch (Exception ignored) {
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
        System.out.println("sharedBuffer.length():" + sharedBuffer.length() + (sharedBuffer.length() != 3000 ? "❌" : ""));
        Assertions.assertEquals(3000, sharedBuffer.length());
    }
}
