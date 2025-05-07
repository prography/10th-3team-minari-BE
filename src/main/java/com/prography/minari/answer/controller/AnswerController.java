package com.prography.minari.answer.controller;

import com.prography.minari.answer.service.SttProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController {
    private final SttProcessingService sttProcessingService;

    @PostMapping("/answer")
    public ResponseEntity<String> getAnswer(@RequestParam("file") MultipartFile files) {
        return ResponseEntity.ok(sttProcessingService.convertToText(files));
    }
}
