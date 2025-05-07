package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Role;
import com.hakmesb.lelabovert.payload.RoleDto;

@Service
public class RoleDtoMapper implements Function<Role, RoleDto> {

	@Override
	public RoleDto apply(Role role) {
		return new RoleDto(
				role.getId(),
				role.getName()
				);
	}

}
