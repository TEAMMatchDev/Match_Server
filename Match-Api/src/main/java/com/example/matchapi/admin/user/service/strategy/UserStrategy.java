package com.example.matchapi.admin.user.service.strategy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.matchapi.admin.user.enums.UserFilter;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.user.repository.UserRepository;

public interface UserStrategy {
	Page<UserRepository.UserList> getUserList(Pageable pageable, String content);

	UserFilter getFilter();
}
