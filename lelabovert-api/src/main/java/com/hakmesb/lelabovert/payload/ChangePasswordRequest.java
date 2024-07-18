package com.hakmesb.lelabovert.payload;

public record ChangePasswordRequest(
		String oldPassword,
		String newPassword
		) {

}
