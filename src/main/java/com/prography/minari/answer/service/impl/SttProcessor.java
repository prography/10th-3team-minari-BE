package com.prography.minari.answer.service.impl;

import com.prography.minari.answer.service.dto.SttConvertAudioFileInfo;
import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@ImplService
@RequiredArgsConstructor
public class SttProcessor {
    private final SttApiClient sttApiClient;

    public SttConvertAudioFileInfo convertToText(byte[] file) {
        AudioFormat format = getAudioFormat(file);
        List<byte[]> rawChunks = splitWavToExactMinutes(file, format);
        double audioDurationInSeconds = getAudioDurationInSeconds(file);
        List<String> texts = Flux.fromIterable(rawChunks)
                .flatMapSequential(chunk -> {
                    byte[] wavChunk = null;
                    try {
                        wavChunk = toAutioFormatBytes(chunk, format);
                    } catch (IOException e) {
                        log.error("오디오 포맷 변환중 에러발생: {}", e.getMessage());
                        return Flux.error(new ApiException(ErrorCode.INTERNAL_SERVER_ERROR));
                    }
                    return sttApiClient.convertToTextNonBlock(wavChunk); // Mono<String>
                })
                .filter(s -> !s.isBlank())
                .map(s -> s.endsWith(".") ? s : s + ".")
                .collectList()
                .block(); // 이 부분에서 예외가 던져짐
        return SttConvertAudioFileInfo.builder()
                .runningTime(audioDurationInSeconds)
                .speech(String.join(" ", Objects.requireNonNull(texts)))
                .build();
    }

    private AudioFormat getAudioFormat(byte[] wavData) {
        log.info("파일형식 조회");
        try (AudioInputStream fullStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(wavData)))) {
            return fullStream.getFormat();
        } catch (UnsupportedAudioFileException e) {
            log.info("[음성파일 포맷조회] 지원하지 않는 오디오 포맷: {}", e.getMessage());
            throw new ApiException(ErrorCode.AUDIO_UNSUPPORT_FORMAT_EXCEPTION);
        } catch (IOException e) {
            log.info("[음성파일 포맷조회] 오디오 파일 처리 중 IO 오류 발생: {}", e.getMessage());
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private List<byte[]> splitWavToExactMinutes(byte[] file, AudioFormat format) {
        try (AudioInputStream fullStream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(new ByteArrayInputStream(file)))) {

            float frameRate = format.getFrameRate();
            int frameSize = format.getFrameSize();
            int bytesPerMinute = (int) (frameRate * frameSize * 60);

            log.info("오디오 분할 시작 - frameRate: {}, frameSize: {}, bytesPerMinute: {}, 총 파일 크기: {} bytes",
                    frameRate, frameSize, bytesPerMinute, file.length);

            List<byte[]> chunks = new ArrayList<>();
            byte[] buffer = new byte[bytesPerMinute];
            int offset = 0;

            int bytesRead;
            int chunkCount = 0;

            while ((bytesRead = fullStream.read(buffer, offset, buffer.length - offset)) != -1) {
                offset += bytesRead;

                if (offset == buffer.length) {
                    chunks.add(Arrays.copyOf(buffer, buffer.length));
                    log.debug("1분 청크 분리 완료: {} bytes (chunk #{})", buffer.length, ++chunkCount);
                    offset = 0;
                }
            }

            if (offset > 0) {
                chunks.add(Arrays.copyOf(buffer, offset));
                log.debug("마지막 청크 분리 완료: {} bytes (chunk #{})", offset, ++chunkCount);
            }

            log.info("오디오 분할 완료 - 총 청크 수: {}", chunks.size());
            return chunks;

        } catch (UnsupportedAudioFileException e) {
            log.info("[음성파일 1분 분리] 지원하지 않는 오디오 포맷: {}", e.getMessage(), e);
            throw new ApiException(ErrorCode.AUDIO_UNSUPPORT_FORMAT_EXCEPTION);
        } catch (IOException e) {
            log.info("[음성파일 1분 분리] 오디오 파일 처리 중 IO 오류 발생: {}", e.getMessage(), e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] toAutioFormatBytes(byte[] rawData, AudioFormat format) throws IOException {
        log.info("오디오 형식({}) 추가", format);
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            AudioInputStream chunkStream = new AudioInputStream(bais, format, rawData.length / format.getFrameSize());
            AudioSystem.write(chunkStream, AudioFileFormat.Type.WAVE, baos);
            return baos.toByteArray();
        }
    }

    private double getAudioDurationInSeconds(byte[] wavBytes) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(wavBytes))) {
            AudioFormat format = audioInputStream.getFormat();
            long frameLength = audioInputStream.getFrameLength(); // 전체 프레임 수
            float frameRate = format.getFrameRate();              // 초당 프레임 수

            double durationInSeconds = frameLength / frameRate;
            log.info("총 프레임 수: {}, 초당 프레임 수: {}, 총 시간: {}초", frameLength, frameRate, durationInSeconds);

            return durationInSeconds;
        } catch (UnsupportedAudioFileException e) {
            log.info("[음성파일 시간계산] 지원하지 않는 오디오 포맷: {}", e.getMessage(), e);
            throw new ApiException(ErrorCode.AUDIO_UNSUPPORT_FORMAT_EXCEPTION);
        } catch (IOException e) {
            log.info("[음성파일 시간계산] 오디오 파일 처리 중 IO 오류 발생: {}", e.getMessage(), e);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
