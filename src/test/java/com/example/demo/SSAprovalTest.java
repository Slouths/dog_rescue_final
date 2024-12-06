package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class SSAprovalTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void testApproveStory_SuccessfulApproval() throws Exception {
        mockMvc.perform(post("/api/approve-story/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void testRejectStory_SuccessfulRejection() throws Exception {
        mockMvc.perform(post("/api/reject-story/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testApproveStoryWithInvalidFile_RainyDay() throws Exception {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "photo",
                "invalid_file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is not an image.".getBytes()
        );

        mockMvc.perform(multipart("/api/submit-story")
                .file(invalidFile)
                .param("story", "This is a test story with an invalid file.")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error submitting story: Invalid file format detected. Only image files (JPG, PNG, GIF) are allowed."));
    }
    
}
