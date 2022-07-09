package com.triple.mileageserviceapi.repository;

import com.triple.mileageserviceapi.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRewardRepository extends JpaRepository<Point, String> {
    Point save(Point point);

    Point findByReviewId(String reviewId);

    Point findByReviewIdAndTypeAndMarkAndRetrievedIdIsNull(String reviewId, int type, int mark);
}
