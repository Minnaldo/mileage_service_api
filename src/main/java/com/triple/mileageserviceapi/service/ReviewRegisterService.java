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

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewRegisterService {

    static int CONTENT = 1;
    static int PHOTO = 2;
    static int FIRSTPLACE = 3;
    static int POINT_INCREMENT = 1;
    static int POINT_DECREMENT = 2;
    static int YES = 1;
    static int NO = 0;
    private final ReviewRegisterRepository reviewRegisterRepository;
    private final PointRewardRepository pointRewardRepository;
    private final UserPointRepository userPointRepository;

    public String reviewRegister(ReviewRequestDto reviewRequestDto) {

        if (reviewRegisterRepository.existsById(reviewRequestDto.getReviewId())) {
            return "FAIL";
        }

        int point_sum = 0;

        Point firstPlacePointEntity = new Point();
        if (reviewRegisterRepository.existsByPlaceId(reviewRequestDto.getPlaceId()) == false) {
            firstPlacePointEntity.setType(FIRSTPLACE);
            firstPlacePointEntity.setPoint(1);
            firstPlacePointEntity.setMark(POINT_INCREMENT);
            firstPlacePointEntity.setReviewId(reviewRequestDto.getReviewId());
            firstPlacePointEntity.setUserId(reviewRequestDto.getUserId());

            point_sum += 1;
            pointRewardRepository.save(firstPlacePointEntity);
        }

        Review reviewEntity = new Review();
        reviewEntity.setContent(reviewRequestDto.getContent());
        reviewEntity.setReviewId(reviewRequestDto.getReviewId());
        reviewEntity.setAttachedPhotoIds(reviewRequestDto.getAttachedPhotoIds());
        reviewEntity.setUserId(reviewRequestDto.getUserId());
        reviewEntity.setPlaceId(reviewRequestDto.getPlaceId());

        reviewRegisterRepository.save(reviewEntity);

        Point contentPointEntity = new Point();
        if (reviewRequestDto.getContent().length() > 1)     // 내용 1자 이상
        {
            contentPointEntity.setType(CONTENT);
            contentPointEntity.setPoint(1);
            contentPointEntity.setMark(POINT_INCREMENT);
            contentPointEntity.setReviewId(reviewRequestDto.getReviewId());
            contentPointEntity.setUserId(reviewRequestDto.getUserId());

            point_sum += 1;
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


        // 회원가입을 하면 user_id 를 받을테니, null 일수는 없지만
        // 회원가입을 하지 않은 회원이, user_id를 가지고 리뷰를 쓸 경우라고 판단하여 작성
        // 회원가입 관련 기능이 명세되어 있지 않으므로, 새로운 user면 저장하였음.
        Optional<User> user = userPointRepository.findById(reviewRequestDto.getUserId());

        if (user.isPresent()) {
            user.get().setPoint(user.get().getPoint() + point_sum);

            userPointRepository.save(user.get());
        } else {
            User userEntity = new User();

            userEntity.setUserId(reviewRequestDto.getUserId());
            userEntity.setPoint(point_sum);

            userPointRepository.save(userEntity);
        }

        return "SUCCESS";
    }


    public String reviewUpdate(ReviewRequestDto reviewRequestDto) {

        Review reviewEntity = new Review();
        Optional<Review> review = reviewRegisterRepository.findById(reviewRequestDto.getReviewId());

        review.ifPresent(selectReview -> {
            reviewEntity.setContent(reviewRequestDto.getContent());
            reviewEntity.setReviewId(reviewRequestDto.getReviewId());
            reviewEntity.setAttachedPhotoIds(reviewRequestDto.getAttachedPhotoIds());
            reviewEntity.setUserId(reviewRequestDto.getUserId());
            reviewEntity.setPlaceId(reviewRequestDto.getPlaceId());

        });
        reviewRegisterRepository.save(reviewEntity);

        // 리뷰를 수정하면 수정한 내용에 맞는 내용 점수를 계산하여 점수를 부여하거나
        //        글만 작성한 리뷰에 사진을 추가하면 1점을 부여합니다.
        // 글만 작성한 리뷰
        // 리뷰아이디로 리뷰를 가져와
        // 글만 작성? == 사진이 null
        // 요청값에서 사진이 있는지 확인?
        int checkContent = 0;
        if (reviewRequestDto.getContent().length() > 1) {
            checkContent = 1;
        }

        int checkPhoto = 0;
        if (reviewRequestDto.getAttachedPhotoIds().length() > 1) {
            checkPhoto = 1;
        }


        // 포인트 테이블에서 reviewId 로 조회를 해야해.
        // 조회 해서 type
        Point contentPoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), CONTENT, POINT_INCREMENT);

        Point photoPoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), PHOTO, POINT_INCREMENT);

        //- checkcontent 1, contentPoint 조회 값 있음 : 그냥 넘어감
        //- checkcontent 0, contentPoint 조회 값 있음 : 포인트 회수
        //- checkcontent 1, contentPoint 조회 값 없음 : 포인트 +1
        //- checkcontent 0, contentPoint 조회 값 없음 : 그냥 넘어감


        if (checkContent == 1 && contentPoint != null) {
            //그냥 넘어감.
        } else if (checkContent == 0 && contentPoint != null) {
            Optional<User> user = userPointRepository.findById(reviewRequestDto.getUserId());
            Point contentPointEntity = new Point();
            contentPointEntity.setUserId(contentPoint.getUserId());
            contentPointEntity.setType(CONTENT);
            contentPointEntity.setPoint(1);
            contentPointEntity.setMark(POINT_DECREMENT);
            contentPointEntity.setReviewId(reviewRequestDto.getReviewId());

            user.get().setPoint(user.get().getPoint() - 1);

            Point p = pointRewardRepository.save(contentPointEntity);

            contentPoint.setRetrievedId(p.getId());

            pointRewardRepository.save(contentPoint);

            userPointRepository.save(user.get());
        } else if (checkContent == 1 && contentPoint == null) {
            Optional<User> user = userPointRepository.findById(reviewRequestDto.getUserId());
            Point contentPointEntity = new Point();
            contentPointEntity.setUserId(reviewRequestDto.getUserId());
            contentPointEntity.setType(CONTENT);
            contentPointEntity.setPoint(1);
            contentPointEntity.setMark(POINT_INCREMENT);
            contentPointEntity.setReviewId(reviewRequestDto.getReviewId());

            user.get().setPoint(user.get().getPoint() + 1);

            pointRewardRepository.save(contentPointEntity);
            userPointRepository.save(user.get());
        } else if (checkContent == 0 && contentPoint == null) {
            //그냥 넘어감.
        }


        //- checkphoto 1, photoPoint 조회 값 있음 : 그냥 넘어감
        //- checkphoto 0, photoPoint 조회 값 있음 : 포인트 회수
        //- checkphoto 1, photoPoint 조회 값 없음 : 포인트 +1
        //- checkphoto 0, photoPoint 조회 값 없음 : 그냥 넘어감
        Optional<User> user2 = userPointRepository.findById(reviewRequestDto.getUserId());
        Point photoPointEntity = new Point();

        if (checkPhoto == 1 && photoPoint != null) {
            //그냥 넘어감.
        } else if (checkPhoto == 0 && photoPoint != null) {
            photoPointEntity.setUserId(photoPoint.getUserId());
            photoPointEntity.setType(PHOTO);
            photoPointEntity.setPoint(1);
            photoPointEntity.setMark(POINT_DECREMENT);
            photoPointEntity.setReviewId(reviewRequestDto.getReviewId());

            user2.get().setPoint(user2.get().getPoint() - 1);

            Point p = pointRewardRepository.save(photoPointEntity);

            photoPoint.setRetrievedId(p.getId());

            pointRewardRepository.save(photoPoint);

            userPointRepository.save(user2.get());
        } else if (checkPhoto == 1 && photoPoint == null) {
            photoPointEntity.setUserId(reviewRequestDto.getUserId());
            photoPointEntity.setType(PHOTO);
            photoPointEntity.setPoint(1);
            photoPointEntity.setMark(POINT_INCREMENT);
            photoPointEntity.setReviewId(reviewRequestDto.getReviewId());

            user2.get().setPoint(user2.get().getPoint() + 1);

            pointRewardRepository.save(photoPointEntity);
            userPointRepository.save(user2.get());
        } else if (checkPhoto == 0 && photoPoint == null) {
            //그냥 넘어감.
        }


        return "SUCCESS";
    }


    public String reviewDelete(ReviewRequestDto reviewRequestDto) {

        Optional<Review> review = reviewRegisterRepository.findById(reviewRequestDto.getReviewId());

        // Review Soft Delete
        review.ifPresent(selectReview -> {
            selectReview.setDeletedAt(LocalDateTime.now());
            selectReview.setIsDelete(YES);
        });


        //content point 회수
        Point contentPoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), CONTENT, POINT_INCREMENT);

        if (contentPoint != null) {
            Point contentPointEntity = new Point();
            contentPointEntity.setUserId(contentPoint.getUserId());
            contentPointEntity.setType(CONTENT);
            contentPointEntity.setPoint(1);
            contentPointEntity.setMark(POINT_DECREMENT);
            contentPointEntity.setReviewId(reviewRequestDto.getReviewId());

            Optional<User> user = userPointRepository.findById(reviewRequestDto.getUserId());
            user.get().setPoint(user.get().getPoint() - 1);

            Point pointDecrement = pointRewardRepository.save(contentPointEntity);

            contentPoint.setRetrievedId(pointDecrement.getId());

            pointRewardRepository.save(contentPoint);

            userPointRepository.save(user.get());
        }


        //photo point 회수
        Point photoPoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), PHOTO, POINT_INCREMENT);

        if (photoPoint != null) {
            Point photoPointEntity = new Point();
            photoPointEntity.setUserId(photoPoint.getUserId());
            photoPointEntity.setType(PHOTO);
            photoPointEntity.setPoint(1);
            photoPointEntity.setMark(POINT_DECREMENT);
            photoPointEntity.setReviewId(reviewRequestDto.getReviewId());

            Optional<User> user2 = userPointRepository.findById(reviewRequestDto.getUserId());
            user2.get().setPoint(user2.get().getPoint() - 1);

            Point pointDecrement2 = pointRewardRepository.save(photoPointEntity);

            photoPoint.setRetrievedId(pointDecrement2.getId());

            pointRewardRepository.save(photoPoint);

            userPointRepository.save(user2.get());
        }

        //firstplace point 회수
        Point firstplacePoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), FIRSTPLACE, POINT_INCREMENT);

        if (firstplacePoint != null) {
            Point firstplacePointEntity = new Point();
            firstplacePointEntity.setUserId(firstplacePoint.getUserId());
            firstplacePointEntity.setType(FIRSTPLACE);
            firstplacePointEntity.setPoint(1);
            firstplacePointEntity.setMark(POINT_DECREMENT);
            firstplacePointEntity.setReviewId(reviewRequestDto.getReviewId());

            Optional<User> user3 = userPointRepository.findById(reviewRequestDto.getUserId());
            user3.get().setPoint(user3.get().getPoint() - 1);

            Point pointDecrement3 = pointRewardRepository.save(firstplacePointEntity);

            firstplacePoint.setRetrievedId(pointDecrement3.getId());

            pointRewardRepository.save(firstplacePoint);

            userPointRepository.save(user3.get());
        }


        return "SUCCESS";
    }


}
