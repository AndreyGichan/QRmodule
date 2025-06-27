package by.belpost.qrmodule;

import by.belpost.qrmodule.controller.QRCodeController;
import by.belpost.qrmodule.service.app.QRCodeGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    private QRCodeController controller;

    @Autowired
    private QRCodeGeneratorServiceImpl generatorService;

    @Test
    public void testFullFlow() throws Exception {
        String fileName = controller.generateQRCode(
                "https://belpost.by", "png", "test", null, null, null
        ).getBody();

        assertNotNull(controller.downloadQRCode(fileName));
    }
    @Test
    public void testGenerateAndDownloadQRCodeWithLogo() throws Exception {
        MultipartFile logoFile = createMockLogoFile();

        String fileName = controller.generateQRCode(
                "https://belpost.by",
                "png",
                "test_with_logo",
                "#FF0000",
                "#FFFFFF",
                logoFile
        ).getBody();

        assertNotNull(fileName);
        assertNotNull(controller.downloadQRCode(fileName));

    }

    private MockMultipartFile createMockLogoFile() throws IOException {
        BufferedImage logoImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = logoImage.createGraphics();
        graphics.setColor(Color.BLUE);
        graphics.fillRect(0, 0, 100, 100);
        graphics.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(logoImage, "png", baos);

        return new MockMultipartFile(
                "logo",
                "test-logo.png",
                "image/png",
                baos.toByteArray()
        );
    }
}