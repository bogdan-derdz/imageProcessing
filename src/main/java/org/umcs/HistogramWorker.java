package org.umcs;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class HistogramWorker implements Runnable{
    private Channel channel;
    private BufferedImage image;
    private int start, end;
    private AtomicArray histogram;

    public HistogramWorker(BufferedImage image, AtomicArray histogram, int start, int end, Channel channel) {
        this.image = image;
        this.start = start;
        this.end = end;
        this.histogram = histogram;
        this.channel = channel;
    }

    @Override
    public void run() {
        for(int y = start; y < end; y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int colorBrightness = 0;
                int rgb = image.getRGB(x, y);

                switch (channel) {
                    case R:
                        colorBrightness = (rgb>>16)&0xFF;
                        break;
                    case G:
                        colorBrightness = (rgb>>8)&0xFF;
                        break;
                    case B:
                        colorBrightness = rgb&0xFF;
                        break;
                }

                histogram.increment(colorBrightness);
            }
        }
    }
}
