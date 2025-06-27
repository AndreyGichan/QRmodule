package by.belpost.qrmodule.service.app;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.time.Instant;

@Component
public class QRCodeCleanupScheduler {
    @Value("${qr.code.dir}")
    private String qrCodeDir;

    private static final long MAX_FILE_AGE_MILLIS = 10 * 60 * 1000;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void cleanOldFiles() {
        File folder = new File(qrCodeDir);
        if (!folder.exists() || !folder.isDirectory()) return;

        File[] files = folder.listFiles();
        if (files == null) return;

        long now = Instant.now().toEpochMilli();

        for (File file : files) {
            if (now - file.lastModified() > MAX_FILE_AGE_MILLIS) {
                file.delete();
            }
        }
    }
}
