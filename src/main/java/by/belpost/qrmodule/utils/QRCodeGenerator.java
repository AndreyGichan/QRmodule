package by.belpost.qrmodule.utils;

import by.belpost.qrmodule.model.parcel.Parcel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    public static void generateQRCode(Parcel parcel) throws WriterException, IOException {
        String qrCodePath = "D:\\javaProjects\\qrmodule\\qrcodes\\";
        Files.createDirectories(Path.of(qrCodePath));

        String safeName = parcel.getSenderName().replaceAll("[^a-zA-Z0-9а-яА-Я]", "_");
        String qrCodeName = qrCodePath + parcel.getId() + "_" + safeName + "-QRCode.png";

        String content = "ID: " + parcel.getId() + "\n" +
                "Senddate: " + parcel.getSendDate() + "\n" +
                "Sendername: " + parcel.getSenderName() + "\n" +
                "Status: " + parcel.getStatus() + "\n" +
                "Type: " + parcel.getType();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);

        Path path = FileSystems.getDefault().getPath(qrCodeName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    private static void writeSVG(BitMatrix matrix, Path path) throws IOException {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(String.format(
                    "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"%d\" height=\"%d\" shape-rendering=\"crispEdges\">",
                    width, height));
            writer.write("<rect width=\"100%\" height=\"100%\" fill=\"white\"/>");

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        writer.write(String.format(
                                "<rect x=\"%d\" y=\"%d\" width=\"1\" height=\"1\" fill=\"black\"/>", x, y));
                    }
                }
            }

            writer.write("</svg>");
        }
    }
    public static void generateCustomQRCode(String content, String format, String fileName)
            throws WriterException, IOException {

        String qrCodePath = "D:/javaProjects/qrmodule/qrcodes/";
        Files.createDirectories(Path.of(qrCodePath));
        String ext = (format == null || format.isEmpty()) ? "png" : format.toLowerCase();
        String fullName = (fileName == null || fileName.isEmpty() ? "CustomQRCode" : fileName) + "." + ext;
        Path outPath = Path.of(qrCodePath, fullName);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 400, 400,
                Map.of(EncodeHintType.CHARACTER_SET, "UTF-8"));

        switch (ext) {
            case "png":
                MatrixToImageWriter.writeToPath(matrix, "PNG", outPath);
                break;
            case "svg":
                writeSVG(matrix, outPath);
                break;
            case "pdf":
                BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
                try (PDDocument doc = new PDDocument()) {
                    PDPage page = new PDPage();
                    doc.addPage(page);
                    PDImageXObject pdImage = LosslessFactory.createFromImage(doc, image);
                    try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                        contentStream.drawImage(pdImage, 100, 400, 300, 300);
                    }
                    doc.save(outPath.toFile());
                }
                break;
            default:
                throw new IllegalArgumentException("Формат не поддерживается: " + ext);
        }
    }

}
