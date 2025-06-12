package by.belpost.qrmodule.model.parcel;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parcels")
public class ParcelController {
    private final ParcelService parcelService;
    @Autowired
    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @GetMapping
    public ResponseEntity<List<Parcel>> getParcels(){
        return ResponseEntity.ok(parcelService.getParcels());
    }

    @PostMapping
    public Parcel addParcel(@RequestBody Parcel parcel){
        return parcelService.addParcel(parcel);
    }
    @GetMapping("/{id}")
    public Parcel findById(@PathVariable("id") Integer id){
        return parcelService.findById(id);
    }
}
