package com.prography.minari.answer.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static com.prography.minari.common.execption.ErrorCode.AUDIO_UNSUPPORT_FORMAT_EXCEPTION;
import static com.prography.minari.common.execption.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@ImplService
@RequiredArgsConstructor
public class AudioFileFormatConverter {
    private static final String BASE_DIR = System.getProperty("user.dir");
    private static final String INPUT_DIR = BASE_DIR + "/input/";
    private static final String OUTPUT_DIR = BASE_DIR + "/output/";

    public byte[] convertToWavAsByte(MultipartFile file) {
        // 0. 디렉토리 경로 설정
        File inputDir = new File(INPUT_DIR);
        File outputDir = new File(OUTPUT_DIR);

        try {
            // 1. 디렉토리 존재 확인 및 생성
            if (!inputDir.exists() && !inputDir.mkdirs()) {
                log.error("입력 디렉토리를 생성할 수 없습니다: {}", inputDir.getAbsolutePath());
                throw new ApiException(INTERNAL_SERVER_ERROR);
            }
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                log.error("출력 디렉토리를 생성할 수 없습니다: {}", outputDir.getAbsolutePath());
                throw new ApiException(INTERNAL_SERVER_ERROR);
            }

            // 2. 파일명 검증 및 설정
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.endsWith(".webm")) {
                log.warn("지원하지 않는 파일 형식: {}", originalFilename);
                throw new ApiException(AUDIO_UNSUPPORT_FORMAT_EXCEPTION);
            }

            String baseName = UUID.randomUUID().toString();
            String inputFileName = baseName + ".webm";
            String outputFileName = baseName + ".wav";

            File inputFile = new File(inputDir, inputFileName);
            File outputFile = new File(outputDir, outputFileName);

            // 3. 업로드 파일 저장
            file.transferTo(inputFile);
            log.info("업로드 파일 저장 완료: {}", inputFile.getAbsolutePath());

            // 4. ffmpeg 명령 실행
            String cmd = String.format(
                    "docker run --rm -v %s:/data linuxserver/ffmpeg:latest -i /data/input/%s /data/output/%s",
                    new File(".").getAbsolutePath(), inputFileName, outputFileName
            );

            log.debug("실행할 ffmpeg 명령어: {}", cmd);

            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug("ffmpeg >> {}", line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0 || !outputFile.exists()) {
                log.error("ffmpeg 변환 실패 - exitCode: {}, 파일 존재 여부: {}", exitCode, outputFile.exists());
                throw new ApiException(INTERNAL_SERVER_ERROR);
            }

            // 5. 변환된 파일 읽기
            byte[] wavData = Files.readAllBytes(outputFile.toPath());
            log.info("WAV 변환 완료: {} ({} bytes)", outputFile.getName(), wavData.length);

            return wavData;

        } catch (IOException e) {
            log.error("I/O 오류 발생: {}", e.getMessage(), e);
            throw new ApiException(INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태 복원
            log.error("ffmpeg 프로세스 대기 중 인터럽트 발생", e);
            throw new ApiException(INTERNAL_SERVER_ERROR);
        } finally {
            // 안전하게 임시 파일 삭제
            Arrays.stream(Objects.requireNonNull(inputDir.listFiles()))
                    .filter(f -> f.getName().endsWith(".webm"))
                    .forEach(f -> {
                        if (f.delete()) log.debug("입력 파일 삭제 완료: {}", f.getName());
                    });
            Arrays.stream(Objects.requireNonNull(outputDir.listFiles()))
                    .filter(f -> f.getName().endsWith(".wav"))
                    .forEach(f -> {
                        if (f.delete()) log.debug("출력 파일 삭제 완료: {}", f.getName());
                    });
        }
    }
}
