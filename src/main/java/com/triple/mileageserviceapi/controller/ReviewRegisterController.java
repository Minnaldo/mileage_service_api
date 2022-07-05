package com.triple.mileageserviceapi.controller;

import com.triple.mileageserviceapi.controller.dto.ReviewRequestDto;
import com.triple.mileageserviceapi.service.ReviewRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewRegisterController {

    private final ReviewRegisterService reviewRegisterService;

    @PostMapping("/events")
    public ResponseEntity<String> reviewRegister(@RequestBody ReviewRequestDto reviewRequestDto) {
        // 파라미터 검증
//        if (reviewRegisterDto.getType().equals("REVIEW")) {
//            // todo error return
//            return null;
//        }

        log.info("type : {} ", reviewRequestDto.getType());
        log.info("action : {} ", reviewRequestDto.getAction());
        log.info("review_id : {} ", reviewRequestDto.getReviewId());

        // todo 서비스 호출
        if (reviewRequestDto.getAction().equals("ADD")) {
            reviewRegisterService.reviewRegister(reviewRequestDto);
        }
        else if(reviewRequestDto.getAction().equals("MOD")) {
            reviewRegisterService.reviewUpdate(reviewRequestDto);
        }
        else if(reviewRequestDto.getAction().equals("DELETE")) {
            reviewRegisterService.reviewDelete(reviewRequestDto);
        }


        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
