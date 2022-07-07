package com.triple.mileageserviceapi.service;

import com.triple.mileageserviceapi.controller.dto.ReviewRequestDto;
import com.triple.mileageserviceapi.entity.Point;
import com.triple.mileageserviceapi.entity.Review;
import com.triple.mileageserviceapi.entity.User;
import com.triple.mileageserviceapi.repository.PointRewardRepository;
import com.triple.mileageserviceapi.repository.ReviewRegisterRepository;
import com.triple.mileageserviceapi.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewRegisterService {

    private final ReviewRegisterRepository reviewRegisterRepository;
    private final PointRewardRepository pointRewardRepository;
    private final UserPointRepository userPointRepository;


    public int reviewRegister(ReviewRequestDto reviewRequestDto) {
        Review reviewEntity = new Review();
        Point pointEntity = new Point();
        User userEntity = new User();

        reviewEntity.setContent(reviewRequestDto.getContent());
        reviewEntity.setReviewId(reviewRequestDto.getReviewId());
        reviewEntity.setAttachedPhotoIds(reviewRequestDto.getAttachedPhotoIds());
        reviewEntity.setUserId(reviewRequestDto.getUserId());
        reviewEntity.setPlaceId(reviewRequestDto.getPlaceId());

        // TODO reviewRegisterRepository 호출
        reviewRegisterRepository.save(reviewEntity);


        log.info("save review_id : {} is saved.", reviewRequestDto.getReviewId());

        // TODO PointRewardRepository 호출
        if (reviewRequestDto.getContent().length() > 1)     // 내용 1자 이상
        {
            pointEntity.setPoint(1);
            pointEntity.setReviewId(reviewRequestDto.getReviewId());
            pointEntity.setType(1);
            pointEntity.setUserId(reviewRequestDto.getUserId());

            pointRewardRepository.save(pointEntity);


            // TODO user 에 point를 저장해줘야 함.
            if(userEntity.getUserId() == null) {
                userEntity.setUserId(reviewRequestDto.getUserId());
                userEntity.setPoint(pointEntity.getPoint());

            }
            else {
                Optional<User> user = userPointRepository.findById(reviewRequestDto.getUserId());

                user.ifPresent(selectUser -> {
                    userEntity.setPoint(pointEntity.getPoint() + 1);
                });
            }
            userPointRepository.save(userEntity);
        }

        if (reviewRequestDto.getAttachedPhotoIds().length() > 1)   // 1장 이상
        {
            pointEntity.setPoint(1);
            pointEntity.setReviewId(reviewRequestDto.getReviewId());
            pointEntity.setType(1);
            pointEntity.setUserId(reviewRequestDto.getUserId());

            pointRewardRepository.save(pointEntity);


            // TODO user 에 point를 저장해줘야 함.
            if(userEntity.getUserId() == null) {
                userEntity.setUserId(reviewRequestDto.getUserId());
                userEntity.setPoint(pointEntity.getPoint());

            }
            else {
                Optional<User> user = userPointRepository.findById(reviewRequestDto.getUserId());

                user.ifPresent(selectUser -> {
                   userEntity.setPoint(pointEntity.getPoint() + 1);
                });
            }
            userPointRepository.save(userEntity);
        }

        return 0;
    }


    public int reviewUpdate(ReviewRequestDto reviewRequestDto) {
        Review reviewEntity = new Review();
        Point pointEntity = new Point();
        User userEntity = new User();

        Optional<Review> review = reviewRegisterRepository.findById(reviewRequestDto.getReviewId());

        review.ifPresent(selectReview -> {
            reviewEntity.setContent(reviewRequestDto.getContent());
            reviewEntity.setReviewId(reviewRequestDto.getReviewId());
            reviewEntity.setAttachedPhotoIds(reviewRequestDto.getAttachedPhotoIds());
            reviewEntity.setUserId(reviewRequestDto.getUserId());
            reviewEntity.setPlaceId(reviewRequestDto.getPlaceId());

        });

        // TODO Repository 호출
        reviewRegisterRepository.save(reviewEntity);


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
