package com.triple.mileageserviceapi.repository;

import com.triple.mileageserviceapi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRegisterRepository extends JpaRepository<Review, String> {
    Review save(Review review);

    boolean existsByPlaceId(String placeId);

    List<Review> findByUserIdAndPlaceId(String userId, String placeId);

    //Review findByBetweenCreatedAt(LocalDateTime from, LocalDateTime to);

}
