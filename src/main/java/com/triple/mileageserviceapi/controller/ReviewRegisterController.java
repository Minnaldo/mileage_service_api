package com.triple.mileageserviceapi.controller;

import com.triple.mileageserviceapi.controller.dto.ReviewRegisterDto;
import com.triple.mileageserviceapi.service.ReviewRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewRegisterController {

    private final ReviewRegisterService reviewRegisterService;

    @PostMapping("/events")
    public String reviewRegister(@RequestBody ReviewRegisterDto reviewRegisterDto) {
        // 파라미터 검증
//        if (reviewRegisterDto.getType().equals("REVIEW")) {
//            // todo error return  // Type이 REVIEW면 왜 error 를 리턴해??
//            return null;
//        }

        System.out.println("controller 탔냐?");
        // todo 서비스 호출
        reviewRegisterService.reviewRegister(reviewRegisterDto);

        return null;
    }
}
