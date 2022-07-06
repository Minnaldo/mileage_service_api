package com.triple.mileageserviceapi.service;

import com.triple.mileageserviceapi.controller.dto.ReviewRequestDto;
import com.triple.mileageserviceapi.entity.Review;
import com.triple.mileageserviceapi.repository.ReviewRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewRegisterService {

    private final ReviewRegisterRepository reviewRegisterRepository;

    public int reviewRegister(ReviewRequestDto reviewRequestDto) {
        Review reviewEntity = new Review();

        reviewEntity.setContent(reviewRequestDto.getContent());
        reviewEntity.setReviewId(reviewRequestDto.getReviewId());
        reviewEntity.setAttachedPhotoIds(reviewRequestDto.getAttachedPhotoIds());
        reviewEntity.setUserId(reviewRequestDto.getUserId());
        reviewEntity.setPlaceId(reviewRequestDto.getPlaceId());


        // TODO Repository 호출
        reviewRegisterRepository.save(reviewEntity);

        log.info("save review_id : {} is saved.", reviewRequestDto.getReviewId());

        return 0;
    }


    public int reviewUpdate(ReviewRequestDto reviewRequestDto) {
        Review reviewEntity = new Review();

        Optional<Review> review = reviewRegisterRepository.findById(reviewRequestDto.getReviewId());

        review.ifPresent(selectReview -> {
            reviewEntity.setContent(reviewRequestDto.getContent());
            reviewEntity.setReviewId(reviewRequestDto.getReviewId());
            reviewEntity.setAttachedPhotoIds(reviewRequestDto.getAttachedPhotoIds());
            reviewEntity.setUserId(reviewRequestDto.getUserId());
            reviewEntity.setPlaceId(reviewRequestDto.getPlaceId());

            reviewRegisterRepository.save(reviewEntity);
        });

        // TODO Repository 호출
        reviewRegisterRepository.save(reviewEntity);

        log.info("update review_id : {} is update.", reviewRequestDto.getReviewId());

        return 0;
    }


    public int reviewDelete(ReviewRequestDto reviewRequestDto) {
        Review reviewEntity = new Review();

        Optional<Review> review = reviewRegisterRepository.findById(reviewRequestDto.getReviewId());

        // TODO Repository 호출
        review.ifPresent(selectReview -> {
            reviewRegisterRepository.delete(selectReview);
        });

        return 0;
    }

}
