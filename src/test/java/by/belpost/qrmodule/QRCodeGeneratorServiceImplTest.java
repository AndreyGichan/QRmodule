package by.belpost.qrmodule;

import by.belpost.qrmodule.sevice.app.QRCodeGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QRCodeGeneratorServiceImplTest {

    @Autowired
    private QRCodeGeneratorServiceImpl generatorService;

    @Test
    public void testGeneratePNG() throws Exception {
        Path path = generatorService.generateCustomQRCode(
                "test", "png", "test_qr", "#000000", "#FFFFFF", null
        );
        assertTrue(Files.exists(path));
    }

    @Test
    public void testGenerateWithLogo() throws Exception {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        MockMultipartFile logoFile = new MockMultipartFile(
                "logo", "logo.png", "image/png", baos.toByteArray()
        );
        Path path = generatorService.generateCustomQRCode(
                "test", "png", "test_with_logo", null, null, logoFile
        );
        assertTrue(Files.exists(path));
    }

    @Test
    public void testUnsupportedFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            generatorService.generateCustomQRCode(
                    "test", "unsupported", "test_fail", null, null, null
            );
        });
    }
}