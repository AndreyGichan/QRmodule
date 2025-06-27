package by.belpost.qrmodule.service.app;

import com.google.zxing.WriterException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface QRCodeGeneratorService {
    Path generateCustomQRCode(
            String content,
            String format,
            String fileName,
            String foregroundColor,
            String backgroundColor,
            MultipartFile logoFile
    ) throws WriterException, IOException;
}