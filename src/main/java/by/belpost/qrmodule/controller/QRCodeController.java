package by.belpost.qrmodule.controller;

import by.belpost.qrmodule.dto.QRCodeRequest;
import by.belpost.qrmodule.sevice.QRCodeMetadataService;
import by.belpost.qrmodule.sevice.QRCodeTemplateService;
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
    private QRCodeTemplateService templateService;
    @Autowired
    private QRCodeMetadataService metadataService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateQRCode(@RequestBody QRCodeRequest request) {
        try {
            String content;
            String contentType;

            if (request.getTemplateName() != null && !request.getTemplateName().isEmpty()) {
                content = templateService.buildContentFromTemplate(request);
                contentType = switch (request.getTemplateName().toLowerCase()) {
                    case "parcel" -> "parcel";
                    case "link" -> "link";
                    default -> "text";
                };
            } else {
                content = request.getContent();
                contentType = "text";
            }

            Path filePath = QRCodeGenerator.generateCustomQRCode(
                    content,
                    request.getFormat(),
                    request.getFileName(),
                    request.getTemplateName()
            );
            metadataService.saveMetadata(
                    request.getParcelId(),           // может быть null
                    filePath.toString(),             // путь к файлу
                    request.getFormat(),             // png, pdf, svg
                    request.getTemplateName(),       // шаблон
                    contentType                      // text/link/parcel
            );
            return ResponseEntity.ok("QR-код успешно сгенерирован");
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().body("Ошибка при генерации QR-кода: " + e.getMessage());
        }
    }
}
