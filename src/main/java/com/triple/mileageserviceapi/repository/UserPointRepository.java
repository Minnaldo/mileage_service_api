package com.triple.mileageserviceapi.repository;

import com.triple.mileageserviceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<User, String> {
    User save(User user);


}
