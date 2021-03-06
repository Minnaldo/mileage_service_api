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
        if (reviewRequestDto.getContent().length() > 1)     // ?????? 1??? ??????
        {
            contentPointEntity.setType(CONTENT);
            contentPointEntity.setPoint(1);
            contentPointEntity.setMark(POINT_INCREMENT);
            contentPointEntity.setReviewId(reviewRequestDto.getReviewId());
            contentPointEntity.setUserId(reviewRequestDto.getUserId());

            point_sum += 1;
        }

        Point photoPointEntity = new Point();
        if (reviewRequestDto.getAttachedPhotoIds().length() > 1)   // 1??? ??????
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


        // ??????????????? ?????? user_id ??? ????????????, null ????????? ?????????
        // ??????????????? ?????? ?????? ?????????, user_id??? ????????? ????????? ??? ???????????? ???????????? ??????
        // ???????????? ?????? ????????? ???????????? ?????? ????????????, ????????? user??? ???????????????.
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

        // ????????? ???????????? ????????? ????????? ?????? ?????? ????????? ???????????? ????????? ???????????????
        //        ?????? ????????? ????????? ????????? ???????????? 1?????? ???????????????.
        // ?????? ????????? ??????
        // ?????????????????? ????????? ?????????
        // ?????? ??????? == ????????? null
        // ??????????????? ????????? ????????? ???????
        int checkContent = 0;
        if (reviewRequestDto.getContent().length() > 1) {
            checkContent = 1;
        }

        int checkPhoto = 0;
        if (reviewRequestDto.getAttachedPhotoIds().length() > 1) {
            checkPhoto = 1;
        }


        // ????????? ??????????????? reviewId ??? ????????? ?????????.
        // ?????? ?????? type
        Point contentPoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), CONTENT, POINT_INCREMENT);

        Point photoPoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), PHOTO, POINT_INCREMENT);

        //- checkcontent 1, contentPoint ?????? ??? ?????? : ?????? ?????????
        //- checkcontent 0, contentPoint ?????? ??? ?????? : ????????? ??????
        //- checkcontent 1, contentPoint ?????? ??? ?????? : ????????? +1
        //- checkcontent 0, contentPoint ?????? ??? ?????? : ?????? ?????????


        if (checkContent == 1 && contentPoint != null) {
            //?????? ?????????.
        } else if (checkContent == 0 && contentPoint != null) {
            int retrievedPointId = pointRetrieved(CONTENT, reviewRequestDto.getUserId());

            contentPoint.setRetrievedId(retrievedPointId);
            pointRewardRepository.save(contentPoint);
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
            //?????? ?????????.
        }


        //- checkphoto 1, photoPoint ?????? ??? ?????? : ?????? ?????????
        //- checkphoto 0, photoPoint ?????? ??? ?????? : ????????? ??????
        //- checkphoto 1, photoPoint ?????? ??? ?????? : ????????? +1
        //- checkphoto 0, photoPoint ?????? ??? ?????? : ?????? ?????????

        if (checkPhoto == 1 && photoPoint != null) {
            //?????? ?????????.
        } else if (checkPhoto == 0 && photoPoint != null) {
            int retrievedPointId = pointRetrieved(PHOTO, reviewRequestDto.getUserId());

            photoPoint.setRetrievedId(retrievedPointId);
            pointRewardRepository.save(photoPoint);
        } else if (checkPhoto == 1 && photoPoint == null) {
            Optional<User> user2 = userPointRepository.findById(reviewRequestDto.getUserId());
            Point photoPointEntity = new Point();
            photoPointEntity.setUserId(reviewRequestDto.getUserId());
            photoPointEntity.setType(PHOTO);
            photoPointEntity.setPoint(1);
            photoPointEntity.setMark(POINT_INCREMENT);
            photoPointEntity.setReviewId(reviewRequestDto.getReviewId());

            user2.get().setPoint(user2.get().getPoint() + 1);

            pointRewardRepository.save(photoPointEntity);
            userPointRepository.save(user2.get());
        } else if (checkPhoto == 0 && photoPoint == null) {
            //?????? ?????????.
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


        //content point ??????
        Point contentPoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), CONTENT, POINT_INCREMENT);

        if (contentPoint != null) {
            int retrievedPointId = pointRetrieved(CONTENT, reviewRequestDto.getUserId());

            contentPoint.setRetrievedId(retrievedPointId);
            pointRewardRepository.save(contentPoint);
        }


        //photo point ??????
        Point photoPoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), PHOTO, POINT_INCREMENT);

        if (photoPoint != null) {
            int retrievedPointId = pointRetrieved(PHOTO, reviewRequestDto.getUserId());

            photoPoint.setRetrievedId(retrievedPointId);
            pointRewardRepository.save(photoPoint);
        }

        //firstplace point ??????
        Point firstplacePoint = pointRewardRepository.findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(reviewRequestDto.getReviewId(), FIRSTPLACE, POINT_INCREMENT);

        if (firstplacePoint != null) {
            int retrievedPointId = pointRetrieved(FIRSTPLACE, reviewRequestDto.getUserId());

            firstplacePoint.setRetrievedId(retrievedPointId);
            pointRewardRepository.save(firstplacePoint);
        }


        return "SUCCESS";
    }

    private int pointRetrieved(int pointType, String userId) {
        Point retrievedPointEntity = new Point();
        retrievedPointEntity.setUserId(userId);
        retrievedPointEntity.setType(pointType);
        retrievedPointEntity.setPoint(1);
        retrievedPointEntity.setMark(POINT_DECREMENT);
        retrievedPointEntity.setReviewId(userId);

        Optional<User> user3 = userPointRepository.findById(userId);
        user3.get().setPoint(user3.get().getPoint() - 1);

        Point savedRetrievedPoint = pointRewardRepository.save(retrievedPointEntity);

        userPointRepository.save(user3.get());
        return savedRetrievedPoint.getId();
    }


}
