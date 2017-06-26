package com.javaquasar.util.image;

import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javax.xml.bind.DatatypeConverter;

import org.imgscalr.Scalr;
import static org.imgscalr.Scalr.Method;
import static org.imgscalr.Scalr.OP_ANTIALIAS;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Java Quasar
 */
public class ImageProcessor {

    // parameters for convert String to QR Base64
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private static final Charset OUTPUT_ENCODING = StandardCharsets.UTF_8;
    private static final ErrorCorrectionLevel EC_LEVEL = ErrorCorrectionLevel.L;
    private static final int MARGIN = 4;
    private static final String IMAGE_EXTENSION = "PNG";

    public static final int SMALL_SIZE = 128;
    public static final int MEDIUM_SIZE = 256;

    public static void saveBytesToFile(String filePath, byte[] fileBytes) throws FileNotFoundException, IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(fileBytes);
        }
    }

    public static void main(String[] args) throws IOException {
        File clientQrFolder = new File("/home/artur/Desktop/" + 2020 + new Date().getTime());
        if (!clientQrFolder.exists()) {
            clientQrFolder.mkdir();
        }
        String qr = "Hello";
        String pathToQrImage = clientQrFolder.getCanonicalPath() + "/QR_CODE.png";
        ImageProcessor.createQrImage(qr, pathToQrImage);
    }

    /**
     *
     * @param text - plain text
     * @return Base64 QrCode
     */
    public static String getBase64QrCode(String text) {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, MARGIN);
        hints.put(EncodeHintType.CHARACTER_SET, OUTPUT_ENCODING.name());
        hints.put(EncodeHintType.ERROR_CORRECTION, EC_LEVEL);

        BitMatrix matrix = null;
        try {
            matrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        } catch (WriterException e) {
            System.out.println(e);
        }

        String base64QrCode = null;
        try (ByteArrayOutputStream pngOut = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, IMAGE_EXTENSION, pngOut);
            base64QrCode = Base64.getMimeEncoder().encodeToString(pngOut.toByteArray());
        } catch (IOException e) {
            System.out.println(e);
        }
        return base64QrCode;
    }

    public static boolean createQrImage(String textQr, String pathToFile) {
        try {
            String base64QrCode = getBase64QrCode(textQr);
            BufferedImage qrImage = convertBase64ToBufferedImage(base64QrCode);
            File outputfile = new File(pathToFile);
            ImageIO.write(qrImage, "png", outputfile);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public static BufferedImage scaleImage(BufferedImage bufferedImage, double percent) {
        int scaledWidth = (int) (bufferedImage.getWidth() * percent);
        int scaledHeight = (int) (bufferedImage.getHeight() * percent);
        return Scalr.resize(bufferedImage, Method.ULTRA_QUALITY, scaledWidth, scaledHeight, OP_ANTIALIAS);
    }

    /**
     *
     * @param base64Image
     * @param a - size square
     * @return base64Image
     */
    public static String scaleSquareBase64Image(String base64Image, int a) {
        BufferedImage image = scaleSquareImage(convertBase64ToBufferedImage(base64Image), a);
        return convertBufferedImageToBase64(image);
    }

    public static BufferedImage scaleSquareImage(String base64Image, int a) {
        return scaleSquareImage(convertBase64ToBufferedImage(base64Image), a);
    }

    public static BufferedImage scaleSquareImage(BufferedImage bufferedImage, int a) {
        return scaleImage(bufferedImage, a, a);
    }

    public static BufferedImage scaleImage(BufferedImage bufferedImage, int height, int width) {
        return org.imgscalr.Scalr.resize(bufferedImage, Method.BALANCED, width, height, OP_ANTIALIAS);
    }

    public static BufferedImage convertBase64ToBufferedImage(String imageBase64) {
        BufferedImage image = null;
        byte[] imageByte;
        ByteArrayInputStream bis = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageBase64);
            bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
        } catch (Exception e) {
            System.out.println("convertBase64ToBufferedImage = " + e.toString());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                System.out.println("convertBase64ToBufferedImage = " + e.toString());
            }
        }
        return image;
    }

    public static String convertBufferedImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            System.out.println("convertBufferedImageToBase64 = " + e.toString());
        }
        return DatatypeConverter.printBase64Binary(baos.toByteArray());
    }

    public static String imageToString(BufferedImage image, String type) {
        String imageString = null;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static String encodeToBase64String(String source) {
        return isEmptyString(source) ? null
                : Base64.getEncoder().encodeToString(source.getBytes(Charset.forName("UTF-8")));
    }

    public static String decodeFromBase64String(String base64String) {
        return isEmptyString(base64String) ? null
                : new String(Base64.getDecoder().decode(base64String), Charset.forName("UTF-8"));
    }

    public static boolean isEmptyString(Object str) {
        return ((str == null) || str.toString().trim().isEmpty());
    }

}
