package by.belpost.qrmodule.service.model;

import by.belpost.qrmodule.model.QRCodeMetadata;

import java.time.LocalDate;
import java.util.List;

public interface QRCodeMetadataService {

    void saveMetadata(String content, String path,
                      String format,  String fileName);

    List<QRCodeMetadata> search(String template,
                                Integer parcelId,
                                String path,
                                LocalDate createdAt,
                                String format);


}