package com.triple.mileageserviceapi.service;

import com.triple.mileageserviceapi.entity.User;
import com.triple.mileageserviceapi.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointCheckService {

    private final UserPointRepository userPointRepository;

    public int pointCheck(String userId) {
        Optional<User> user = userPointRepository.findById(userId);

        if (user.isPresent()) {
            return user.get().getPoint();
        }


        return 0;
    }

}
