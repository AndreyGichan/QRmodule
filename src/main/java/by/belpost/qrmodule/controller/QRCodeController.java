package by.belpost.qrmodule.controller;

import by.belpost.qrmodule.dto.QRCodeRequest;
import by.belpost.qrmodule.sevice.QRCodeMetadataService;
import by.belpost.qrmodule.utils.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/qrcodes")
public class QRCodeController {
    @Autowired
    private QRCodeMetadataService metadataService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateQRCode(@RequestBody QRCodeRequest request) {
        try {
            String content = request.getContent();
            String format = request.getFormat();
            String fileName = request.getFileName();
            String contentType = request.getContentType();


            Path filePath = QRCodeGenerator.generateCustomQRCode(content, format, fileName);
            metadataService.saveMetadata(
                    content,
                    filePath.toString(),
                    format,
                    fileName,
                    contentType
            );
            return ResponseEntity.ok("QR-код успешно сгенерирован");
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().body("Ошибка при генерации QR-кода: " + e.getMessage());
        }
    }
}
