package com.prography.minari.answer.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    private static final String HOST_SHARED_DIR = "/home/ubuntu/minari-data";
    private static final String LOCAL_DIR = System.getProperty("user.dir");

    private static final String INPUT_RELATIVE_PATH = "/input/";
    private static final String OUTPUT_RELATIVE_PATH = "/output/";

    public byte[] convertToWavAsByte(MultipartFile file) {
        File inputDir = new File(getInputDir());
        File outputDir = new File(getOutputDir());

        try {
            if (!inputDir.exists() && !inputDir.mkdirs()) {
                log.error("입력 디렉토리를 생성할 수 없습니다: {}", inputDir.getAbsolutePath());
                throw new ApiException(INTERNAL_SERVER_ERROR);
            }
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                log.error("출력 디렉토리를 생성할 수 없습니다: {}", outputDir.getAbsolutePath());
                throw new ApiException(INTERNAL_SERVER_ERROR);
            }

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

            file.transferTo(inputFile);
            log.info("업로드 파일 저장 완료: {}", inputFile.getAbsolutePath());

            String cmd = buildFFmpegCommand(inputFileName, outputFileName);
            log.info("실행할 ffmpeg 명령어: {}", cmd);

            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("ffmpeg >> {}", line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0 || !outputFile.exists()) {
                log.error("ffmpeg 변환 실패 - exitCode: {}, 파일 존재 여부: {}", exitCode, outputFile.exists());
                throw new ApiException(INTERNAL_SERVER_ERROR);
            }

            byte[] wavData = Files.readAllBytes(outputFile.toPath());
            log.info("WAV 변환 완료: {} ({} bytes)", outputFile.getName(), wavData.length);

            return wavData;

        } catch (IOException e) {
            log.error("I/O 오류 발생: {}", e.getMessage(), e);
            throw new ApiException(INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("ffmpeg 프로세스 대기 중 인터럽트 발생", e);
            throw new ApiException(INTERNAL_SERVER_ERROR);
        } finally {
            /*deleteTempFiles(inputDir, ".webm");
            deleteTempFiles(outputDir, ".wav");*/
        }
    }

    private String getInputDir() {
        return getBaseDir() + INPUT_RELATIVE_PATH;
    }

    private String getOutputDir() {
        return getBaseDir() + OUTPUT_RELATIVE_PATH;
    }

    private String getBaseDir() {
        return isLocalProfile() ? LOCAL_DIR : HOST_SHARED_DIR;
    }

    private String buildFFmpegCommand(String inputFileName, String outputFileName) {
        String dockerBin = isLocalProfile() ? "docker" : "/usr/bin/docker";
        String hostPath = getBaseDir();

        return String.format(
                "%s run --rm %s -v %s:/data linuxserver/ffmpeg:latest -i /data/input/%s /data/output/%s",
                dockerBin,
                isLiveProfile() ? "-v /var/run/docker.sock:/var/run/docker.sock" : "",
                hostPath,
                inputFileName,
                outputFileName
        );
    }

    private boolean isLocalProfile() {
        return "local".equalsIgnoreCase(activeProfile);
    }

    private boolean isLiveProfile() {
        return "live".equalsIgnoreCase(activeProfile);
    }

    private void deleteTempFiles(File dir, String extension) {
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(f -> f.getName().endsWith(extension))
                .forEach(f -> {
                    if (f.delete()) log.debug("파일 삭제 완료: {}", f.getName());
                });
    }
}
