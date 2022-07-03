package com.triple.mileageserviceapi.service;

import com.triple.mileageserviceapi.controller.dto.ReviewRegisterDto;
import com.triple.mileageserviceapi.entity.Point;
import com.triple.mileageserviceapi.entity.Review;
import com.triple.mileageserviceapi.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public int pointSave(ReviewRegisterDto reviewRegisterDto)
    {
        Review reviewentity = new Review();

        reviewentity.setReviewId(reviewRegisterDto.getReviewId());

        return 0;
    }


}
