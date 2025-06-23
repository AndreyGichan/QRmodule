package by.belpost.qrmodule.sevice;

import by.belpost.qrmodule.model.QRCodeMetadata;
import by.belpost.qrmodule.model.QRCodeMetadataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QRCodeMetadataService {

    private final QRCodeMetadataRepository repository;

    public QRCodeMetadataService(QRCodeMetadataRepository repository) {
        this.repository = repository;
    }

    public void saveMetadata(String content, String path, String format,  String fileName, String contentType) {
        QRCodeMetadata meta = new QRCodeMetadata();
        meta.setContent(content);
        meta.setPath(path);
        meta.setFormat(format);
        meta.setFileName(fileName);
        meta.setContentType(contentType);
        meta.setCreatedAt(LocalDateTime.now().withNano(0));
        repository.save(meta);
    }
}