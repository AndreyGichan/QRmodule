package by.belpost.qrmodule.controller;

import by.belpost.qrmodule.model.QRCodeMetadata;
import by.belpost.qrmodule.service.model.QRCodeMetadataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/qrcodes")
public class QRCodeSearchController {

    @Autowired
    private QRCodeMetadataServiceImpl qrCodeMetadataService;

    @GetMapping("/search")
    public List<QRCodeMetadata> searchQRCodes(
            @RequestParam(required = false) String template,
            @RequestParam(required = false) Integer parcelId,
            @RequestParam(required = false) String path,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt,
            @RequestParam(required = false) String format
    ) {
        return qrCodeMetadataService.search(template, parcelId, path, createdAt, format);
    }
}

