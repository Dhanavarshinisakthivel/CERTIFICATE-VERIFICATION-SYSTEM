package com.certverify.util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * QRCodeUtil - Generates QR codes for certificates.
 * The QR code contains the verification URL with the certificate ID.
 * When scanned, it opens the verify page directly.
 */
public class QRCodeUtil {

    /**
     * Generates a QR code image as a Base64 string.
     * This can be embedded directly in HTML as: <img src="data:image/png;base64,{result}">
     */
    public static String generateQRCode(String certificateId, String frontendBaseUrl) {
        try {
            // The URL that the QR code will open when scanned
            String verifyUrl = frontendBaseUrl + "/verify?id=" + certificateId;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(verifyUrl, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            // Convert to base64 so we can store/send it as text
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (WriterException | IOException e) {
            // If QR generation fails, return null (not critical)
            System.err.println("QR Code generation failed: " + e.getMessage());
            return null;
        }
    }
}
