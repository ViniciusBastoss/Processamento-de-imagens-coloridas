package processamento_de_imagens;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadImages {
    private String folderName;
    private List<String> imagesPath = new ArrayList<>();

    public UploadImages(String folder) {
        this.folderName = folder;
    }

    public List<String> getPathImages(){
        File folder = new File(folderName);
        File[] files = folder.listFiles();


        for(File file : files){
            imagesPath.add(folderName + "\\" + file.getName());
        }
        return imagesPath;
    }

    public List<BufferedImage> getImages() throws IOException {
        getPathImages();
        List<BufferedImage> images = new ArrayList<>();
        for(String path:imagesPath)
            images.add(ImageIO.read(new File(path)));
        return images;
    }

    public BufferedImage convertToRGB(BufferedImage image) {
        if(image.getType() != BufferedImage.TYPE_INT_RGB && image.getType() != BufferedImage.TYPE_INT_ARGB){
            BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixelBGR = image.getRGB(x, y);
                    int blue = pixelBGR & 0xFF;
                    int green = (pixelBGR >> 8) & 0xFF;
                    int red = (pixelBGR >> 16) & 0xFF;
                    int pixelRGB = (red << 16) | (green << 8) | blue;
                    convertedImage.setRGB(x, y, pixelRGB);
                }
            }
            return convertedImage;
        }
        else
            return image;
    }

    public void convertTypeImages(List<BufferedImage> images){
        BufferedImage newImage;
       for(int i = 0; i < images.size(); i++){
           newImage = convertToRGB(images.get(i));
           images.set(i, newImage);
       }

    }
}

