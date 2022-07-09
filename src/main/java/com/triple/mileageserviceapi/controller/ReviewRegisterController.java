package com.triple.mileageserviceapi.controller;

import com.triple.mileageserviceapi.controller.dto.ReviewRequestDto;
import com.triple.mileageserviceapi.service.ReviewRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewRegisterController {

    static String add = "ADD";
    static String mod = "MOD";
    static String delete = "DELETE";
    private final ReviewRegisterService reviewRegisterService;

    @PostMapping("/events")
    public ResponseEntity<Map> reviewRegister(@RequestBody ReviewRequestDto reviewRequestDto) {

        String result = "";
        if (add.equals(reviewRequestDto.getAction())) {
            result = reviewRegisterService.reviewRegister(reviewRequestDto);
        } else if (mod.equals(reviewRequestDto.getAction())) {
            result = reviewRegisterService.reviewUpdate(reviewRequestDto);
        } else if (delete.equals(reviewRequestDto.getAction())) {
            result = reviewRegisterService.reviewDelete(reviewRequestDto);
        }

        Map<String, String> response = new HashMap<>();
        if (result.equals("SUCCESS")) {
            response.put("result", "SUCCESS");
        } else {
            response.put("result", "FAIL");
        }

        return ResponseEntity.ok(response);
    }
}
