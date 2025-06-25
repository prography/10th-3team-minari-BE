package com.prography.minari.answer.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SttConvertAudioFileInfo {
    private double runningTime;
    private String speech;
}
