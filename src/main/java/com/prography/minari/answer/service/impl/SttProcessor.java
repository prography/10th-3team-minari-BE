package com.prography.minari.answer.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
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

    public String convertToText(byte[] file) {
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

    }

    private AudioFormat getAudioFormat(byte[] wavData) {
        log.info("파일형식 조회");
        try (AudioInputStream fullStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(wavData)))) {
            return fullStream.getFormat();
        } catch (UnsupportedAudioFileException e) {
            log.error("지원하지 않는 오디오 포맷: {}", e.getMessage());
            throw new ApiException(ErrorCode.AUDIO_UNSUPPORT_FORMAT_EXCEPTION);
        } catch (IOException e) {
            log.error("오디오 파일 처리 중 IO 오류 발생: {}", e.getMessage());
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private List<byte[]> splitWavToExactMinutes(byte[] file, AudioFormat format) {
        try (AudioInputStream fullStream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(new ByteArrayInputStream(file)))) {

            float frameRate = format.getFrameRate();     // 예: 44100.0
            int frameSize = format.getFrameSize();       // 예: 4 (16bit stereo)
            int bytesPerMinute = (int) (frameRate * frameSize * 60); // 1분 분량

            log.info("오디오 분할 시작 - frameRate: {}, frameSize: {}, bytesPerMinute: {}, 총 파일 크기: {} bytes",
                    frameRate, frameSize, bytesPerMinute, file.length);

            List<byte[]> chunks = new ArrayList<>();
            byte[] buffer = new byte[bytesPerMinute];
            int offset = 0;

            int bytesRead;
            int chunkCount = 0;

            while ((bytesRead = fullStream.read(buffer, offset, buffer.length - offset)) != -1) {
                offset += bytesRead;

                // 1분 분량이 꽉 찼을 때만 추가
                if (offset == buffer.length) {
                    chunks.add(Arrays.copyOf(buffer, buffer.length));
                    log.debug("1분 청크 분리 완료: {} bytes (chunk #{})", buffer.length, ++chunkCount);
                    offset = 0;
                }
            }

            // 남은 데이터도 마지막 청크로 추가
            if (offset > 0) {
                chunks.add(Arrays.copyOf(buffer, offset));
                log.debug("마지막 청크 분리 완료: {} bytes (chunk #{})", offset, ++chunkCount);
            }

            log.info("오디오 분할 완료 - 총 청크 수: {}", chunks.size());
            return chunks;

        } catch (UnsupportedAudioFileException e) {
            log.error("지원하지 않는 오디오 포맷: {}", e.getMessage(), e);
            throw new ApiException(ErrorCode.AUDIO_UNSUPPORT_FORMAT_EXCEPTION);
        } catch (IOException e) {
            log.error("오디오 파일 처리 중 IO 오류 발생: {}", e.getMessage(), e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] toAutioFormatBytes(byte[] rawData, AudioFormat format) throws IOException {
        log.info("오디오 형식({}) 추가",format);
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
