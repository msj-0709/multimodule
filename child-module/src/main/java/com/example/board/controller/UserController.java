package com.example.board.controller;

import com.example.board.constant.UserStatusType;
import com.example.board.domain.UserInfo;
import com.example.board.model.CreateUserRequest;
import com.example.board.model.GetUserResponse;
import com.example.board.model.UpdatePasswordRequest;
import com.example.board.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@Slf4j
public class UserController {

	@Autowired
	private UserInfoRepository userInfoRepository;

	@GetMapping({ "/user/count" })
	@ResponseBody
	public long userCount(@RequestHeader("User-Agent") String userAgent) {
		log.info("userAgent : {}", userAgent);
		return this.userInfoRepository.count();
	}

	@PostMapping({ "/user" })
	@ResponseBody
	public void createUser(@RequestBody @Valid CreateUserRequest request) {
		log.info("invoke createUser. request[{}]", request);
		if (0L < this.userInfoRepository.countByUsername(request.getUsername()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exists username");
		long id = this.userInfoRepository.getNextUserInfoId();
		this.userInfoRepository.save(UserInfo.builder().userInfoId(Objects.toString(Long.valueOf(id)))
				.username(request.getUsername()).password(request.getPassword()).koreanName(request.getKoreanName())
				.status(UserStatusType.NORMAL).build());
	}

	@PutMapping({ "/user/{userInfoId}/password" })
	@ResponseBody
	public void updatePasswordUser(@PathVariable("userInfoId") String id,
								   @RequestBody @Valid UpdatePasswordRequest request) {
		UserInfo userInfo = this.userInfoRepository.findByUserInfoId(id)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "not exists user"));
		userInfo.setPassword(request.getPassword());
		this.userInfoRepository.save(userInfo);
	}

	@DeleteMapping({ "/user/{userInfoId}" })
	@ResponseBody
	public void deregister(@PathVariable("userInfoId") String id) {
		UserInfo userInfo = this.userInfoRepository.findByUserInfoId(id)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "not exists user"));
		userInfo.setStatus(UserStatusType.DEREGISTER);
		this.userInfoRepository.save(userInfo);
	}

	@GetMapping("/user")
	@ResponseBody
	public GetUserResponse getUser(@RequestParam("username") String usrename) {

		UserInfo userInfo = this.userInfoRepository.findByUsername(usrename)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "not exists user"));

		return GetUserResponse.builder().userInfoId(userInfo.getUserInfoId()).username(userInfo.getUsername())
				.password(userInfo.getPassword()).koreanName(userInfo.getKoreanName()).build();
	}
}
