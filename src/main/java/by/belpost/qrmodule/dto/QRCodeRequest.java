package by.belpost.qrmodule.dto;

public class QRCodeRequest {
    private String content;    // содержимое для кодирования
    private String format;     // PNG, SVG, PDF (на будущее)
    private String fileName;   // имя файла, если нужно

    private String templateName; // "parcel", "link", "custom"
    private Integer parcelId;
    private String trackNumber;
    private String customText;

    public QRCodeRequest() {}

    public QRCodeRequest(String content, String format, String fileName) {
        this.content = content;
        this.format = format;
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getParcelId() {
        return parcelId;
    }

    public void setParcelId(Integer parcelId) {
        this.parcelId = parcelId;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }
}
