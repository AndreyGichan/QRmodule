package by.belpost.qrmodule.controller;

import by.belpost.qrmodule.sevice.app.QRCodeGeneratorServiceImpl;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/qrcodes")
public class QRCodeController {

    @Autowired
    private QRCodeGeneratorServiceImpl generatorService;

    @PostMapping(value = "/generate", consumes = {"multipart/form-data"})
    public ResponseEntity<String> generateQRCode(
            @RequestParam("content") String content,
            @RequestParam("format") String format,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "foregroundColor", required = false) String foregroundColor,
            @RequestParam(value = "backgroundColor", required = false) String backgroundColor,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile
    ) {
        try {
            String fileNameResult = generatorService.generateAndStoreQRCode(
                    content, format, fileName, foregroundColor, backgroundColor, logoFile
            );
            return ResponseEntity.ok(fileNameResult);
        } catch (WriterException | IOException e) {
            return ResponseEntity.internalServerError().body("Ошибка при генерации QR-кода: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadQRCode(@RequestParam String fileName) throws IOException {
        Path filePath = Paths.get("D:/javaProjects/qrmodule/qrcodes/").resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType;
        if (fileName.toLowerCase().endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.toLowerCase().endsWith(".svg")) {
            contentType = "image/svg+xml";
        } else if (fileName.toLowerCase().endsWith(".pdf")) {
            contentType = "application/pdf";
        } else {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
