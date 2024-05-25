package org.umcs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ImageProcesor {
    public BufferedImage image;

    public void read(String src) throws IOException {
        image = ImageIO.read(new File(src));
    }

    public void write(String src) throws IOException {
        String format = src.substring(src.lastIndexOf('.') + 1);

        ImageIO.write(image, format,  new File(src));
    }

    public void brighten(int value) {
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0x000000FF;
                int green = (rgb >>8 ) & 0x000000FF;
                int blue = (rgb) & 0x000000FF;

                red = Clamp.clamp(red + value, 0, 255);
                green = Clamp.clamp(green + value, 0, 255);
                blue = Clamp.clamp(blue + value, 0, 255);

                int newRgb = (red << 16) | (green << 8) | blue;

                image.setRGB(x, y, newRgb);
            }
        }
    }

    public void brightenMulti(int value) throws InterruptedException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        int height = image.getHeight();
        Thread[] threads = new Thread[numThreads];
        int chunkSize = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == numThreads - 1) ? height : (i + 1) * chunkSize;

            threads[i] = new Thread(new BrightenWorker(image, startIndex, endIndex, value));
            threads[i].start();
        }

        for (Thread thread : threads) thread.join();
    }

    public int[] getChannelHistogram(Channel channel) throws InterruptedException {
        AtomicArray histogram = new AtomicArray(256);

        int numThreads = Runtime.getRuntime().availableProcessors();
        int height = image.getHeight();
        Thread[] threads = new Thread[numThreads];
        int chunkSize = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == numThreads - 1) ? height : (i + 1) * chunkSize;

            threads[i] = new Thread(new HistogramWorker(image,histogram,startIndex,endIndex,channel));
            threads[i].start();
        }

        for (Thread thread : threads) thread.join();

        for(int i = 0; i < 256; i++) {
            System.out.println(histogram.get(i));
        }

        return histogram.toArray();
    }

    public void generateImageHistogram(int[] histogram, Channel channel) throws IOException {
        int height = 256;
        int width = 256;
        int maxValue = 0;
        double scale;
        Color color;

        switch (channel) {
            case B:
                color = Color.BLUE;
                break;
            case R:
                color = Color.RED;
                break;
            case G:
                color = Color.GREEN;
                break;
            default:
                color = Color.BLACK;
                break;
        }

        for (int value : histogram) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        scale = (double) height / maxValue;

        BufferedImage histogramImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D gHistogram = histogramImage.createGraphics();

        gHistogram.setColor(Color.WHITE);
        gHistogram.fillRect(0, 0, width, height);

        gHistogram.setColor(color);
        gHistogram.setStroke(new BasicStroke(1));

        for(int i = 0; i < 256; i++) {
            gHistogram.draw(new Line2D.Double(i, height - 1, i, (height - 1) - histogram[i] * scale));
        }
        System.out.println(channel);
        ImageIO.write(histogramImage,"png", new File("./histogram" + channel + ".png"));
    }
}
