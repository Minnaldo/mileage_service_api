package com.triple.mileageserviceapi.repository;

import com.triple.mileageserviceapi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRegisterRepository extends JpaRepository<Review, Integer> {
    Review save(Review review);
}
