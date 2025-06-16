package by.belpost.qrmodule.utils;

import by.belpost.qrmodule.model.parcel.Parcel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    public static void generateQRCode(Parcel parcel) throws WriterException, IOException {
        String qrCodePath = "D:\\javaProjects\\qrmodule\\qrcodes\\";
        Files.createDirectories(Path.of(qrCodePath)); // создать директорию, если нет

        String safeName = parcel.getSenderName().replaceAll("[^a-zA-Z0-9а-яА-Я]", "_");
        String qrCodeName = qrCodePath + parcel.getId() + "_" + safeName + "-QRCode.png";

        String content = "ID: " + parcel.getId() + "\n" +
                "Senddate: " + parcel.getSendDate() + "\n" +
                "Sendername: " + parcel.getSenderName() + "\n" +
                "Status: " + parcel.getStatus() + "\n" +
                "Type: " + parcel.getType();

        // Указываем кодировку UTF-8
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);

        Path path = FileSystems.getDefault().getPath(qrCodeName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void generateCustomQRCode(String content, String format, String fileName) throws WriterException, IOException {
        String qrCodePath = "D:\\javaProjects\\qrmodule\\qrcodes\\";
        Files.createDirectories(Path.of(qrCodePath)); // создать папку, если нет

        String outputFileName = (fileName != null && !fileName.isEmpty()) ? fileName : "CustomQRCode";
        String extension = format != null ? format.toUpperCase() : "PNG";

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);

        Path path = FileSystems.getDefault().getPath(qrCodePath + outputFileName + "." + extension.toLowerCase());
        MatrixToImageWriter.writeToPath(bitMatrix, extension, path);
    }

}
