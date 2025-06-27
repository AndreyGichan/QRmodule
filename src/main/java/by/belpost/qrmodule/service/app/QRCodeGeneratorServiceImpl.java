package by.belpost.qrmodule.service.app;

import by.belpost.qrmodule.service.model.QRCodeMetadataServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeGeneratorServiceImpl implements QRCodeGeneratorService {
    @Value("${qr.svg.xmlns}")
    private String svgXmlns;
    @Value("${qr.code.dir}")
    private String qrCodePath;
    @Autowired
    private QRCodeMetadataServiceImpl metadataService;

    public String generateAndStoreQRCode(
            String content,
            String format,
            String fileName,
            String foregroundColor,
            String backgroundColor,
            MultipartFile logoFile
    ) throws WriterException, IOException {
        if (fileName == null || fileName.isBlank()) {
            fileName = "qr_" + System.currentTimeMillis();
        } else {
            fileName = fileName.replaceAll("[^a-zA-Z0-9а-яА-Я]", "_");
        }
        Path filePath = generateCustomQRCode(content, format, fileName, foregroundColor, backgroundColor, logoFile);
        metadataService.saveMetadata(content, filePath.toString(), format, fileName);
        return filePath.getFileName().toString();
    }
    private void writeSVG(BitMatrix matrix, Path path, String foregroundHex, String backgroundHex) throws IOException {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(String.format(
                    "<svg xmlns=\"%s\" width=\"%d\" height=\"%d\" shape-rendering=\"crispEdges\">",
                    svgXmlns, width, height));
            writer.write(String.format("<rect width=\"100%%\" height=\"100%%\" fill=\"%s\"/>", backgroundHex));

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        writer.write(String.format(
                                "<rect x=\"%d\" y=\"%d\" width=\"1\" height=\"1\" fill=\"%s\"/>", x, y, foregroundHex));
                    }
                }
            }

            writer.write("</svg>");
        }
    }

    private static Path resolveFilePath(String baseDir, String fileName, String extension) throws IOException {

        Path directory = Path.of(baseDir);
        Files.createDirectories(directory);

        String safeName = (fileName == null || fileName.isBlank())
                ? "qr_" + System.currentTimeMillis()
                : fileName.replaceAll("[^a-zA-Z0-9а-яА-Я]", "_");

        return directory.resolve(safeName + "." + extension.toLowerCase());
    }
    private static int parseColor(String hex, Color fallback) {
        try {
            return Color.decode(hex).getRGB();
        } catch (Exception e) {
            return fallback.getRGB();
        }
    }
    public Path generateCustomQRCode(
            String content,
            String format,
            String fileName,
            String foregroundColor,
            String backgroundColor,
            MultipartFile logoFile
    ) throws WriterException, IOException {

        Files.createDirectories(Path.of(qrCodePath));

        String ext = (format == null || format.isEmpty()) ? "png" : format.toLowerCase();
        Path outPath = resolveFilePath(qrCodePath, fileName, ext);

        int width = 400, height = 400;
        QRCodeWriter writer = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        int fgColor = parseColor(foregroundColor, Color.BLACK);
        int bgColor = parseColor(backgroundColor, Color.WHITE);
        MatrixToImageConfig config = new MatrixToImageConfig(fgColor, bgColor);

        switch (ext) {
            case "png":
                BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix, config);
                if (logoFile != null && !logoFile.isEmpty()) {
                    qrImage = overlayLogo(qrImage, logoFile);
                }
                ImageIO.write(qrImage, "PNG", outPath.toFile());
                break;
            case "svg":
                writeSVG(matrix, outPath, foregroundColor != null ? foregroundColor : "#000000", backgroundColor != null ? backgroundColor : "#ffffff");
                break;
            case "pdf":
                BufferedImage pdfImage = MatrixToImageWriter.toBufferedImage(matrix, config);
                if (logoFile != null && !logoFile.isEmpty()) {
                    pdfImage = overlayLogo(pdfImage, logoFile);
                }
                try (PDDocument doc = new PDDocument()) {
                    PDPage page = new PDPage();
                    doc.addPage(page);
                    PDImageXObject pdImage = LosslessFactory.createFromImage(doc, pdfImage);
                    try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                        cs.drawImage(pdImage, 100, 400, 300, 300);
                    }
                    doc.save(outPath.toFile());
                }
                break;
            default:
                throw new IllegalArgumentException("Формат не поддерживается: " + ext);
        }
        return outPath;
    }
    private static BufferedImage overlayLogo(BufferedImage qrImage, MultipartFile logoFile) throws IOException {
        BufferedImage logo = ImageIO.read(logoFile.getInputStream());

        int qrWidth = qrImage.getWidth();
        int qrHeight = qrImage.getHeight();

        int logoSize = qrWidth / 6;
        int logoX = (qrWidth - logoSize) / 2;
        int logoY = (qrHeight - logoSize) / 2;

        Image scaledLogo = logo.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);

        BufferedImage combined = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();


        g.drawImage(qrImage, 0, 0, null);


        int padding = 6;
        g.setColor(Color.WHITE);
        g.fillRoundRect(logoX - padding, logoY - padding, logoSize + padding * 2, logoSize + padding * 2, 20, 20);


        g.drawImage(scaledLogo, logoX, logoY, null);

        g.dispose();
        return combined;
    }

}