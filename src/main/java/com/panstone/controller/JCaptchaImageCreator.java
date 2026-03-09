package com.panstone.controller;

import com.octo.captcha.service.image.ImageCaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class JCaptchaImageCreator {

	private final ImageCaptchaService imageCaptchaService;

	@GetMapping
	public ResponseEntity<String> captcha(HttpServletRequest request) {
		try {
			ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
			String captchaId = request.getSession().getId();
			BufferedImage challenge = imageCaptchaService.getImageChallengeForID(captchaId, request.getLocale());
			ImageIO.write(challenge, "jpeg", jpegOutputStream);
			return ResponseEntity.ok("data:image/png;base64," + Base64.getEncoder().encodeToString(jpegOutputStream.toByteArray()));
		} catch (IOException e) {
			log.error("generate captcha image error: {}", e.getMessage());
			return ResponseEntity.ok(null);
		}
	}

}
