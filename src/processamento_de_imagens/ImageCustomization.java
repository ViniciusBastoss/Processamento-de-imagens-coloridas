package processamento_de_imagens;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageCustomization {

    public static BufferedImage imgThumbs(BufferedImage imageInput, List<BufferedImage> thumbs) {
        BufferedImage newImage = new BufferedImage(imageInput.getWidth() * thumbs.get(0).getWidth(),
                imageInput.getHeight() * thumbs.get(0).getHeight(), imageInput.getType());
        BufferedImage auxImage;
        Random random = new Random();
        for (int i = 0; i < imageInput.getHeight(); i++)
            for (int j = 0; j < imageInput.getWidth(); j++) {
                int pixel = imageInput.getRGB(j, i);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
                int brightnessLevel = (red + green + blue) / 3;
                int sortedImage = random.nextInt(thumbs.size());
                int rate = brightnessLevel;
                if (brightnessLevel <= 255 / 2) {
                    rate = -1 * rate;
                    rate += 30;
                } else
                    rate += -70;

                auxImage = lightenOrDarkenImage(thumbs.get(sortedImage), rate);
                fillImage(newImage, auxImage, i * auxImage.getHeight(), j * auxImage.getWidth());
            }
        return newImage;
    }

    public static BufferedImage imgThumbsFix(BufferedImage imageInput, List<BufferedImage> thumbs) {
        BufferedImage newImage = new BufferedImage(imageInput.getWidth() * thumbs.get(0).getWidth(),
                imageInput.getHeight() * thumbs.get(0).getHeight(), imageInput.getType());
        BufferedImage auxImage;
        double[] positionImages = new double[thumbs.size()];
        double valueInterval = 255.0/ thumbs.size();
        for(int i = 0; i < thumbs.size(); i++){
            positionImages[i] = valueInterval * (i + 1);

        }
//        positionImages[0] = 135;
//        positionImages[1] = 200;
//        positionImages[2] = 255;

        for (int i = 0; i < imageInput.getHeight(); i++)
            for (int j = 0; j < imageInput.getWidth(); j++) {
                int pixel = imageInput.getRGB(j, i);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
                int brightnessLevel = (red + green + blue) / 3;
                int image = 0;
                int rate = brightnessLevel;
                if (brightnessLevel <= 255/2) {
                    rate = -1 * rate;
                    rate += 30;
                } else
                    rate += -100;
                for(int m = 0; m < thumbs.size(); m++){
                    if(brightnessLevel <= positionImages[m]){
                        image = m;
                        break;
                    }
                }

                auxImage = lightenOrDarkenImage(thumbs.get(image), rate);
                fillImage(newImage, auxImage, i * auxImage.getHeight(), j * auxImage.getWidth());
            }
        return newImage;
    }

    public static void fillImage(BufferedImage image, BufferedImage tumb, int line, int column) {
        int columnImage = column;
        for (int i = 0; i < tumb.getHeight(); i++) {
            for (int j = 0; j < tumb.getWidth(); j++) {
                image.setRGB(columnImage, line, tumb.getRGB(j, i));
                columnImage++;
            }
            columnImage = column;
            line++;
        }
    }

    public static BufferedImage lightenOrDarkenImage(BufferedImage image, int rate) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int y = 0; y < image.getWidth(); y++) {
            for (int x = 0; x < image.getHeight(); x++) {
                int pixel = image.getRGB(x, y);

                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                red = clamp(red + rate, 0, 255);
                green = clamp(green + rate, 0, 255);
                blue = clamp(blue + rate, 0, 255);

                int novoPixel = (red << 16) | (green << 8) | blue;
                newImage.setRGB(x, y, novoPixel);
            }

        }
        return newImage;
    }

    public static int lightenOrDarkenPixel(int pixel, int rate){
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;

        red = clamp(red + rate, 0, 255);
        green = clamp(green + rate, 0, 255);
        blue = clamp(blue + rate, 0, 255);

        int novoPixel = (red << 16) | (green << 8) | blue;
        return novoPixel;
    }

    private static int clamp(int valor, int min, int max) {
        return Math.max(min, Math.min(valor, max));
    }

    public static BufferedImage createThumbnail(int widthThumb, BufferedImage image, Boolean equalDimensions) {
        int heightThumb;
        double ind = (double) widthThumb / image.getWidth();

        if (equalDimensions)
            heightThumb = widthThumb;
        else
            heightThumb = (int) (image.getHeight() * ind);
        //heightThumb = (image.getHeight() * widthThumb) / image.getWidth();
        double pixelsToOnePixelH = image.getHeight() / (double) heightThumb;
        double pixelsToOnePixelW = image.getWidth() / (double) widthThumb;
        BufferedImage thumbImage = new BufferedImage(widthThumb, heightThumb, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < heightThumb; i++)
            for (int j = 0; j < widthThumb; j++) {
                int line, column;
                line = (int) (pixelsToOnePixelH * i);
                column = (int) (pixelsToOnePixelW * j);
                thumbImage.setRGB(j, i, image.getRGB(column, line));
            }
        return thumbImage;
    }

    public static List<BufferedImage> createThumbnails(int widthThumb, List<BufferedImage> images, Boolean equalDimensions) {
        List<BufferedImage> thumbnails = new ArrayList<>();
        for (BufferedImage image : images) {
            thumbnails.add(createThumbnail(widthThumb, image, equalDimensions));
        }
        return thumbnails;
    }

    public static BufferedImage resizeImage(BufferedImage image, int height, int width) {
        double pixelsToOnePixelH = image.getHeight() / (double) height;
        double pixelsToOnePixelW = image.getWidth() / (double) width;
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int line, column;
                line = (int) (pixelsToOnePixelH * i);
                column = (int) (pixelsToOnePixelW * j);
                newImage.setRGB(j, i, image.getRGB(column, line));
            }
        return newImage;
    }

    public static BufferedImage imageToBackG(BufferedImage imageInput, BufferedImage imagebackG) {
        BufferedImage newImage = new BufferedImage(imageInput.getWidth(), imageInput.getHeight(),imageInput.getType());
        for (int i = 0; i < imageInput.getHeight(); i++)
            for (int j = 0; j < imageInput.getWidth(); j++) {
                int pixel = imageInput.getRGB(j, i);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
                int brightnessLevel = (red + green + blue) / 3;
                int rate = brightnessLevel;
                if (brightnessLevel <= 255/2) {
                    rate = -1 * rate;
                    rate += 30;
                } else
                    rate += -70;
               newImage.setRGB(j, i, lightenOrDarkenPixel(imagebackG.getRGB(j, i), rate));
            }
        return newImage;
    }
}
