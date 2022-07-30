package controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class HelloController {
    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    @PostMapping("/image")
    public ResponseEntity<byte[]> processImage(@RequestParam("file") MultipartFile file) throws IOException {
        Path fileNameAndPath = uploadFile(file);
        //  convert the image to grayscale
        BufferedImage image = rgb2GrayScale(fileNameAndPath);
        byte[] bytes = getBytes(fileNameAndPath, image);
        // add more info into the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private static byte[] getBytes(Path fileNameAndPath, BufferedImage image) throws IOException {
        File output = new File(fileNameAndPath.toUri());
        ByteArrayOutputStream byteArrayOutputStream;
        ImageIO.write(image, "jpg", output);
        image = ImageIO.read(output);
        //convert the buffered image to bytes array
        byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    private static Path uploadFile(MultipartFile file) throws IOException {
        Path fileNameAndPath;
        fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        //save the file to locally
        Files.write(fileNameAndPath, file.getBytes());
        return fileNameAndPath;
    }

    private static BufferedImage rgb2GrayScale(Path fileNameAndPath) {
        BufferedImage image;
        // bgr to grayscale
        File input = new File(fileNameAndPath.toUri());
        try {
            image = ImageIO.read(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(image.getRGB(j, i));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);
                Color newColor = new Color(red + green + blue,
                        red + green + blue, red + green + blue);
                image.setRGB(j, i, newColor.getRGB());
            }
        }
        return image;
    }
}
