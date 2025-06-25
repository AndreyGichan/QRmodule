package by.belpost.qrmodule.sevice.model;

import by.belpost.qrmodule.model.QRCodeMetadata;

import java.time.LocalDate;
import java.util.List;

public interface QRCodeMetadataService {

     void saveMetadata(String content, String path,
                             String format,  String fileName,
                             String contentType);

     List<QRCodeMetadata> search(String template,
                                       Integer parcelId,
                                       String path,
                                       LocalDate createdAt,
                                       String format,
                                       String contentType);


}
