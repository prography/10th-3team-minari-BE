package com.prography.minari.admin.controller.dto;

import lombok.Getter;

@Getter
public class RewardForceRequest {
    private String userUUID;
    private int seeds;
    private String role;
    private String reason;
    private String memo;
}
