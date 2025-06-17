package by.belpost.qrmodule.sevice;

import by.belpost.qrmodule.model.QRCodeMetadata;
import by.belpost.qrmodule.model.QRCodeMetadataRepository;
import org.springframework.stereotype.Service;

@Service
public class QRCodeMetadataService {

    private final QRCodeMetadataRepository repository;

    public QRCodeMetadataService(QRCodeMetadataRepository repository) {
        this.repository = repository;
    }

    public void saveMetadata(Integer parcelId, String path, String format, String template, String contentType) {
        QRCodeMetadata meta = new QRCodeMetadata();
        meta.setParcelId(parcelId);
        meta.setPath(path);
        meta.setFormat(format);
        meta.setTemplate(template);
        meta.setContentType(contentType);
        repository.save(meta);
    }
}