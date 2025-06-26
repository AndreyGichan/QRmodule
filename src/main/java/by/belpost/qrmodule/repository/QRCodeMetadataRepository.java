package by.belpost.qrmodule.repository;

import by.belpost.qrmodule.model.QRCodeMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCodeMetadataRepository extends JpaRepository<QRCodeMetadata, Long> {
}