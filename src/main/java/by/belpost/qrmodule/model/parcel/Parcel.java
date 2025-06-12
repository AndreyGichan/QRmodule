package by.belpost.qrmodule.model.parcel;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "parcel")
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "type")
    private String type;

    @Column(name = "send_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sendDate;

    @Column(name = "status")
    private String status;

    public Parcel() {}
    public Parcel(String senderName, String type, LocalDateTime sendDate, String status) {
        this.senderName = senderName;
        this.type = type;
        this.sendDate = sendDate;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getSendDate() { return sendDate; }
    public void setSendDate(LocalDateTime sendDate) { this.sendDate = sendDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
