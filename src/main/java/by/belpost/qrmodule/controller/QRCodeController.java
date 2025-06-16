package by.belpost.qrmodule.controller;

import by.belpost.qrmodule.dto.QRCodeRequest;
import by.belpost.qrmodule.sevice.QRCodeTemplateService;
import by.belpost.qrmodule.utils.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/qrcodes")
public class QRCodeController {
    @Autowired
    private QRCodeTemplateService templateService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateQRCode(@RequestBody QRCodeRequest request) {
        try {
            String content;

            if (request.getTemplateName() != null && !request.getTemplateName().isEmpty()) {
                content = templateService.buildContentFromTemplate(request);
            } else {
                content = request.getContent();
            }

            QRCodeGenerator.generateCustomQRCode(
                    content,
                    request.getFormat(),
                    request.getFileName()
            );
            return ResponseEntity.ok("QR-код успешно сгенерирован");
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().body("Ошибка при генерации QR-кода: " + e.getMessage());
        }
    }
}
