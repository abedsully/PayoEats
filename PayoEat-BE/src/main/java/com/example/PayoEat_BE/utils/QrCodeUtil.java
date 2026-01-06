package com.example.PayoEat_BE.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import javax.imageio.ImageIO;

@Component
public class QrCodeUtil {

    @Value("${backend.url}")
    private String backendUrl;

    public String generateBase64Qr(String text, int width, int height) {
        try {
            String url = backendUrl + "order/confirm-redirect?orderId=" + text;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            byte[] qrBytes = baos.toByteArray();

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrBytes);
        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    public static String generateCustomerId() {
        try {
            UUID randomUUID = UUID.randomUUID();

            return "CUS-".concat(String.valueOf(randomUUID));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}