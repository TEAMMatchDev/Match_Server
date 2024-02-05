package com.example.matchapi.admin.user.service.context;

import static com.example.matchcommon.exception.errorcode.FilterErrorCode.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.matchapi.admin.user.enums.UserFilter;
import com.example.matchapi.admin.user.service.strategy.UserStrategy;
import com.example.matchcommon.exception.BadRequestException;

@Component
public class UserContextFactory {
	private final Map<UserFilter, UserContext> contextMap;

	@Autowired
	public UserContextFactory(List<UserStrategy> strategies) {
		contextMap = new EnumMap<>(UserFilter.class);
		for (UserStrategy strategy : strategies) {
			contextMap.put(strategy.getFilter(), new UserContext(strategy));
		}
	}
	
	public UserContext getContextByFilter(UserFilter filter) {
		UserContext context = contextMap.get(filter);
		if (context == null) {
			throw new BadRequestException(NOT_EXISTS_FILTER);
		}
		return context;
	}
}
