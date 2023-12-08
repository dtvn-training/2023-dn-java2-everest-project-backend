package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.services.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/v1/images")
public class FirebaseController {
    @Autowired
    private FirebaseService firebaseService;
//    @PostMapping("/upload-image")
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
//        return firebaseService.uploadFile(file);
//    }
}
