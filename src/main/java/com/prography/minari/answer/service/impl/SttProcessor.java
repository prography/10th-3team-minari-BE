package com.prography.minari.answer.service.impl;

import com.prography.minari.common.aop.ImplService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ImplService
@RequiredArgsConstructor
public class SttProcessor {
    private final SttApiClient sttApiClient;

    public String convertToText(MultipartFile file) {
        try {
            AudioFormat format = getAudioFormat(file);
            List<byte[]> rawChunks = splitWavToExactMinutes(file, format);
            List<String> texts = rawChunks.stream()
                    .map(chunk -> {
                        try {
                            byte[] wavChunk = toAutioFormatBytes(chunk, format);
                            return sttApiClient.convertToText(wavChunk);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            return texts.stream()
                    .filter(s -> !s.isBlank())
                    .map(s -> s.endsWith(".") ? s : s + ".")
                    .collect(Collectors.joining(" "));

        } catch (Exception e) {
            throw new RuntimeException("STT 변환 실패", e);
        }
    }

    private AudioFormat getAudioFormat(MultipartFile file) throws Exception {
        try (InputStream is = new BufferedInputStream(file.getInputStream());
             AudioInputStream fullStream = AudioSystem.getAudioInputStream(is)) {
            return fullStream.getFormat();
        }
    }

    private List<byte[]> splitWavToExactMinutes(MultipartFile file, AudioFormat format) throws Exception {
        try (InputStream is = new BufferedInputStream(file.getInputStream());
             AudioInputStream fullStream = AudioSystem.getAudioInputStream(is)) {

            float frameRate = format.getFrameRate();     // ex: 44100.0
            int frameSize = format.getFrameSize();       // ex: 4 (16bit stereo)

            int bytesPerMinute = (int) (frameRate * frameSize * 60); // 정확히 1분 분량
            List<byte[]> chunks = new ArrayList<>();

            byte[] buffer = new byte[bytesPerMinute];
            int offset = 0;

            int bytesRead;
            while ((bytesRead = fullStream.read(buffer, offset, buffer.length - offset)) != -1) {
                offset += bytesRead;

                // 1분 분량이 꽉 찼을 때만 추가
                if (offset == buffer.length) {
                    chunks.add(Arrays.copyOf(buffer, buffer.length));
                    offset = 0;
                }
            }

            // 마지막 덜 찬 chunk도 추가
            if (offset > 0) {
                chunks.add(Arrays.copyOf(buffer, offset));
            }

            return chunks;
        }
    }

    private byte[] toAutioFormatBytes(byte[] rawData, AudioFormat format) throws IOException {
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            AudioInputStream chunkStream = new AudioInputStream(bais, format, rawData.length / format.getFrameSize());
            AudioSystem.write(chunkStream, AudioFileFormat.Type.WAVE, baos);
            return baos.toByteArray();
        }
    }
}
