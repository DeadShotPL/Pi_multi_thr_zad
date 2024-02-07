package smula.pi;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        int allPoints = 10000000;
        AtomicInteger pointsInCircle = new AtomicInteger(0);
        int numThreads = Runtime.getRuntime().availableProcessors(); // Get the number of available processors

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new PointGenerator(allPoints / numThreads, pointsInCircle));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double pi = ((double) pointsInCircle.get() * 4) / ((double) allPoints);
        System.out.println(pi);
    }

    static class PointGenerator implements Runnable {
        private final int numPoints;
        private final AtomicInteger pointsInCircle;
        private final Random random = new Random();

        PointGenerator(int numPoints, AtomicInteger pointsInCircle) {
            this.numPoints = numPoints;
            this.pointsInCircle = pointsInCircle;
        }

        @Override
        public void run() {
            int localPointsInCircle = 0;
            for (int i = 0; i < numPoints; i++) {
                double x = random.nextDouble();
                double y = random.nextDouble();

                double distanceFromCenter = Math.sqrt(x * x + y * y);

                if (distanceFromCenter < 1) {
                    localPointsInCircle++;
                }
            }
            pointsInCircle.addAndGet(localPointsInCircle);
        }
    }
}