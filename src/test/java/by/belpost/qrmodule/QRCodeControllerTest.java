package by.belpost.qrmodule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QRCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGenerateQRCode_Success() throws Exception {
        mockMvc.perform(multipart("/qrcodes/generate")
                .param("content", "https://belpost.by")
                .param("format", "png"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testDownloadQRCode_NotFound() throws Exception {
        mockMvc.perform(get("/qrcodes/download")
                        .param("fileName", "nonexistent.png"))
                .andExpect(status().isNotFound());
    }

}