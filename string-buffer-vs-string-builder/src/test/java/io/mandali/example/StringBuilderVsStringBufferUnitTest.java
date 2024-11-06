package io.mandali.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.RepeatedTest;

import io.mandali.Mandali;
import io.mandali.RunMandali;

@RunMandali(showDate = true, detectDeadlock = true)
public class StringBuilderVsStringBufferUnitTest {

    private static final int ITERATIONS = 1000000;

    private long stringBuilderDuration() {
        long startTime = System.nanoTime();

        StringBuilder sb = new StringBuilder();
        sb.append("Hello".repeat(ITERATIONS));

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private long stringBufferDuration() {
        long startTime = System.nanoTime();

        StringBuffer sb = new StringBuffer();
        sb.append("Hello".repeat(ITERATIONS));

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static int stringBuilderScore = 0;
    private static int stringBufferScore = 0;

    @RepeatedTest(20)
    public void givenTwoMethodThenShowResult() {
        long strBuilder = stringBuilderDuration();
        long strBuffer = stringBufferDuration();

        if (strBuilder > strBuffer) {
            stringBufferScore++;
        } else {
            stringBuilderScore++;
        }
    }

    @AfterAll
    public static void recapResults() {
        System.out.println("> StringBuilder score: " + stringBuilderScore);
        System.out.println("> StringBuffer score: " + stringBufferScore);

        if (stringBuilderScore > stringBufferScore) {
            System.out.println("Overall Winner: StringBuilder");
        } else if (stringBufferScore > stringBuilderScore) {
            System.out.println("Overall Winner: StringBuffer");
        } else {
            System.out.println("It's a Tie!");
        }

        new Mandali(StringBuilderVsStringBufferUnitTest.class).start();
    }
}