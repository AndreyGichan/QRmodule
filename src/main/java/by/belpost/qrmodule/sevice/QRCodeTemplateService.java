package by.belpost.qrmodule.sevice;

import by.belpost.qrmodule.dto.QRCodeRequest;
import by.belpost.qrmodule.model.parcel.Parcel;
import by.belpost.qrmodule.model.parcel.ParcelRepository;
import org.springframework.stereotype.Service;

@Service
public class QRCodeTemplateService {

    private final ParcelRepository parcelRepository;

    public QRCodeTemplateService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    public String buildContentFromTemplate(QRCodeRequest request) {
        String template = request.getTemplateName();
        if ("parcel".equalsIgnoreCase(template)) {
            Parcel parcel = parcelRepository.findById(request.getParcelId())
                    .orElseThrow(() -> new RuntimeException("Parcel not found"));

            return "Отправление ID: " + parcel.getId() + "\n"
                    + "Тип: " + parcel.getType() + "\n"
                    + "Статус: " + parcel.getStatus();

        } else if ("link".equalsIgnoreCase(template)) {
            return "https://belpost.by/track/" + request.getTrackNumber();

        } else if ("custom".equalsIgnoreCase(template)) {
            return request.getCustomText();
        }

        throw new IllegalArgumentException("Неизвестный шаблон: " + template);
    }
}

