package com.triple.mileageserviceapi.controller;

import com.triple.mileageserviceapi.entity.User;
import com.triple.mileageserviceapi.service.PointCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointCheckController {

    private final PointCheckService pointCheckService;

    @GetMapping("/point/check")
    public ResponseEntity<User> reviewRegister(@RequestParam String userId) {

        int point = pointCheckService.pointCheck(userId);

        User user = new User();
        user.setUserId(userId);
        user.setPoint(point);

        return ResponseEntity.ok(user);

    }

}
