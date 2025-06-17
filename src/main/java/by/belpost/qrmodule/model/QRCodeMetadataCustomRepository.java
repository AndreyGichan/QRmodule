package by.belpost.qrmodule.model;

import java.time.LocalDate;
import java.util.List;

public interface QRCodeMetadataCustomRepository {
    List<QRCodeMetadata> search(String template,
                                Integer parcelId,
                                String path,
                                LocalDate createdAt,
                                String format,
                                String contentType);
}

