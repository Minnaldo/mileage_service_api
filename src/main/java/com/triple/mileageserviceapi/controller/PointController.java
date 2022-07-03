package com.triple.mileageserviceapi.controller;

import com.triple.mileageserviceapi.controller.dto.ReviewRegisterDto;
import com.triple.mileageserviceapi.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/events")
    public String PointSave(ReviewRegisterDto reviewRegisterDto)
    {
        // 파라미터 검증
        if(reviewRegisterDto.getType().equals("REVIEW"))
        {
            // todo error return
            return null;
        }



        return null;
    }
}
