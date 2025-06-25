package by.belpost.qrmodule.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "qr_code_metadata")
public class QRCodeMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String format;
    private String fileName;
    private String path;
    private String contentType;
    private LocalDateTime createdAt;

}
