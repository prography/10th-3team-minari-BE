package com.prography.minari.answer.dto.req;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class InterviewSttConvertReq implements Serializable {
    private MultipartFile file;
    @Nullable
    private String memo;
}
