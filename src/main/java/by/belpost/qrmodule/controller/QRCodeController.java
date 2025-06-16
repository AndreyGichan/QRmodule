package by.belpost.qrmodule.controller;

import by.belpost.qrmodule.dto.QRCodeRequest;
import by.belpost.qrmodule.utils.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/qrcodes")
public class QRCodeController {

    @PostMapping("/generate")
    public ResponseEntity<String> generateQRCode(@RequestBody QRCodeRequest request) {
        try {
            QRCodeGenerator.generateCustomQRCode(
                    request.getContent(),
                    request.getFormat(),
                    request.getFileName()
            );
            return ResponseEntity.ok("QR-код успешно сгенерирован");
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().body("Ошибка при генерации QR-кода: " + e.getMessage());
        }
    }
}
