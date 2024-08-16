package processamento_de_imagens;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        int widthThumbImageInput = 52, widthThumbImages = 80; // max 314
        BufferedImage imageInput = ImageIO.read(new File("C:\\Users\\vinic\\OneDrive\\Imagens\\Imagens teste\\foto.jpg"));
        UploadImages openFolder = new UploadImages("C:\\Users\\vinic\\OneDrive\\Imagens\\Imagens teste\\imagens_para_miniatura");
        List<BufferedImage> images = openFolder.getImages();
        openFolder.convertTypeImages(images);

        BufferedImage thumbImageInput = ImageCustomization.createThumbnail(widthThumbImageInput, imageInput, Boolean.FALSE);
        List<BufferedImage> thumbsImages = ImageCustomization.createThumbnails(widthThumbImages, images, Boolean.TRUE);

        BufferedImage imgThumbs = ImageCustomization.imgThumbsFix(thumbImageInput, thumbsImages);
        //BufferedImage imgBackG = ImageCustomization.resizeImage(images.get(3), imageInput.getHeight(), imageInput.getWidth());
        //BufferedImage resultimgBackG = ImageCustomization.imageToBackG(imgBackG, imageInput);
        //BufferedImage resultimgBackG = ImageCustomization.imageToBackG(imageInput, imgBackG);
        //BufferedImage imgBackG = ImageCustomization.resizeImage(images.get(9),1080,1920);

        String nameOutput = Integer.toString(widthThumbImageInput) + "_" + Integer.toString(widthThumbImages);
        File arquivoModificado = new File("C:\\Users\\vinic\\OneDrive\\Imagens\\Imagens teste\\outputImages\\imgThumbs" + nameOutput + ".jpg");
        ImageIO.write(imgThumbs, "jpg", arquivoModificado);


    }
}