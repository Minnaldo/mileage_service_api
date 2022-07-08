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
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewRegisterService {

    private final ReviewRegisterRepository reviewRegisterRepository;
    private final PointRewardRepository pointRewardRepository;
    private final UserPointRepository userPointRepository;

    static int CONTENT = 1;
    static int PHOTO = 2;
    static int FIRSTPLACE = 3;
    static int POINT_INCREMENT = 1;
    static int POINT_DECREMENT = 2;

    public int reviewRegister(ReviewRequestDto reviewRequestDto) {

        int point_sum = 0;

        Point firstPlacePointEntity = new Point();
        if (reviewRegisterRepository.existsByPlaceId(reviewRequestDto.getPlaceId()) == false)
        {
            firstPlacePointEntity.setType(FIRSTPLACE);
            firstPlacePointEntity.setPoint(1);
            firstPlacePointEntity.setMark(POINT_INCREMENT);
            firstPlacePointEntity.setReviewId(reviewRequestDto.getReviewId());
            firstPlacePointEntity.setUserId(reviewRequestDto.getUserId());

            point_sum += 1;
        }

        Review reviewEntity = new Review();
        reviewEntity.setContent(reviewRequestDto.getContent());
        reviewEntity.setReviewId(reviewRequestDto.getReviewId());
        reviewEntity.setAttachedPhotoIds(reviewRequestDto.getAttachedPhotoIds());
        reviewEntity.setUserId(reviewRequestDto.getUserId());
        reviewEntity.setPlaceId(reviewRequestDto.getPlaceId());

        // TODO reviewRegisterRepository 호출
        reviewRegisterRepository.save(reviewEntity);

        log.info("save review_id : {} is saved.", reviewRequestDto.getReviewId());

        // TODO PointRewardRepository 호출
        Point contentPointEntity = new Point();
        if (reviewRequestDto.getContent().length() > 1)     // 내용 1자 이상
        {
            contentPointEntity.setType(CONTENT);
            contentPointEntity.setPoint(1);
            contentPointEntity.setMark(POINT_INCREMENT);
            contentPointEntity.setReviewId(reviewRequestDto.getReviewId());
            contentPointEntity.setUserId(reviewRequestDto.getUserId());

            point_sum += 1;

            log.info("content test");
        }

        Point photoPointEntity = new Point();
        if (reviewRequestDto.getAttachedPhotoIds().length() > 1)   // 1장 이상
        {
            photoPointEntity.setType(PHOTO);
            photoPointEntity.setPoint(1);
            photoPointEntity.setMark(POINT_INCREMENT);
            photoPointEntity.setReviewId(reviewRequestDto.getReviewId());
            photoPointEntity.setUserId(reviewRequestDto.getUserId());

            point_sum += 1;
        }

        pointRewardRepository.save(contentPointEntity);
        pointRewardRepository.save(photoPointEntity);
        pointRewardRepository.save(firstPlacePointEntity);


        // 회원가입을 하면 user_id 를 받을테니, null 일수는 없지만
        // 회원가입을 하지 않은 회원이, user_id를 가지고 리뷰를 쓸 경우라고 판단하여 작성
        // 회원가입 관련 기능이 명세되어 있지 않으므로, 새로운 user면 저장하였음.
        Optional<User> user = userPointRepository.findById(reviewRequestDto.getUserId());

        if (user.isPresent()) {
            user.get().setPoint(user.get().getPoint() + point_sum);

            userPointRepository.save(user.get());
        }
        else {
            User userEntity = new User();

            userEntity.setUserId(reviewRequestDto.getUserId());
            userEntity.setPoint(point_sum);

            userPointRepository.save(userEntity);
        }

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

        });
        log.info("update save");
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
