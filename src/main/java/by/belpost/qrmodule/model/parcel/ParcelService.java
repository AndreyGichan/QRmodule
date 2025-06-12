package by.belpost.qrmodule.model.parcel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParcelService {
    private final ParcelRepository parcelRepository;
    public ParcelService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    public List<Parcel> getParcels(){
        return parcelRepository.findAll();
    }
    public Parcel addParcel(Parcel parcel){
        return parcelRepository.save(parcel);
    }
    public Parcel findById(Integer Id){
        return parcelRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));

    }
}
