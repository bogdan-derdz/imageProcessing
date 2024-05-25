package org.umcs;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ImageProcesor imageProcesor = new ImageProcesor();

        Long startTime;
        Long endTime;

//        try {
//            System.out.println(Runtime.getRuntime().availableProcessors());
//            imageProcesor.read("thailand.jpg");
//
//            startTime = System.currentTimeMillis();
//            imageProcesor.brighten(20);
//            endTime = System.currentTimeMillis();
//
//            imageProcesor.write("thailand2.jpg");
//
//            System.out.println(endTime - startTime);
////---
//            imageProcesor.read("thailand.jpg");
//
//            startTime = System.currentTimeMillis();
//            imageProcesor.brightenMulti(20);
//            endTime = System.currentTimeMillis();
//
//            imageProcesor.write("thailand2.jpg");
//
//            System.out.println(endTime - startTime);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        try {
            imageProcesor.read("thailand.jpg");

            int[] histogramR = imageProcesor.getChannelHistogram(Channel.R);
            int[] histogramG = imageProcesor.getChannelHistogram(Channel.G);
            int[] histogramB = imageProcesor.getChannelHistogram(Channel.B);

            imageProcesor.generateImageHistogram(histogramR, Channel.R);
            imageProcesor.generateImageHistogram(histogramG, Channel.G);
            imageProcesor.generateImageHistogram(histogramB, Channel.B);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}