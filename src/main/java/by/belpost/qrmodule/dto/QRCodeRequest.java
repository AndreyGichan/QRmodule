package by.belpost.qrmodule.dto;

public class QRCodeRequest {
    private String content;
    private String format;
    private String fileName;
    private String contentType;
    public QRCodeRequest() {}

    public QRCodeRequest(String content, String format, String fileName, String contentType) {
        this.content = content;
        this.format = format;
        this.fileName = fileName;
        this.contentType = contentType;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
