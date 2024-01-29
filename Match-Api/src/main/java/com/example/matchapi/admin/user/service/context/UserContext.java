package com.example.matchapi.admin.user.service.context;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.matchapi.admin.user.service.strategy.UserStrategy;
import com.example.matchdomain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserContext {
	private final UserStrategy userStrategy;
	public Page<UserRepository.UserList> getUserList(Pageable pageable, String content) {
		return userStrategy.getUserList(pageable, content );
	}
}
