package com.prography.minari.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.minari.common.response.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class ResponseUtil {

    public static void writeUnauthorizedResponse(HttpServletResponse response, String errorCode, String errorMessage) {
        log.info("{} : {}", errorCode, errorMessage);
        try {
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    CommonResponse.fail(errorCode, errorMessage)
            ));
        } catch (IOException e) {
            log.info("writeUnauthorizedResponse IOException 발생!");
            throw new RuntimeException(e);
        }
    }
}
