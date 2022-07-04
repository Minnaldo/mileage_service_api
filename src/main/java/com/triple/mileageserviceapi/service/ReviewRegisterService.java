package com.triple.mileageserviceapi.service;

import com.triple.mileageserviceapi.controller.dto.ReviewRegisterDto;
import com.triple.mileageserviceapi.entity.Review;
import com.triple.mileageserviceapi.repository.ReviewRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewRegisterService {

    private final ReviewRegisterRepository reviewRegisterRepository;

    public int reviewRegister(ReviewRegisterDto reviewRegisterDto) {
        Review reviewEntity = new Review();

        reviewEntity.setContent(reviewRegisterDto.getContent());
        reviewEntity.setReviewId(reviewRegisterDto.getReviewId());
        reviewEntity.setAttachedPhotoIds(reviewRegisterDto.getAttachedPhotoIds());
        reviewEntity.setUserId(reviewRegisterDto.getUserId());
        reviewEntity.setPlaceId(reviewRegisterDto.getPlaceId());
        // todo createdAt 관련 LocalDateTime 어떻게 추가할 지 생각!


        // todo Repository 호출
        reviewRegisterRepository.save(reviewEntity);

        return 0;
    }


}
