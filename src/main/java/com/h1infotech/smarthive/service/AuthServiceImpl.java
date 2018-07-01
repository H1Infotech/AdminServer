package com.h1infotech.smarthive.service;

import com.h1infotech.smarthive.domain.Admin;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.SmsSender;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.AdminRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service(value = "authService")
public class AuthServiceImpl implements AuthService {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	AdminRightService adminRightService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public Admin register(Admin admin) {
		Admin registeredAdmin = adminRepository.findDistinctFirstByUsername(admin.getName());
		if (registeredAdmin != null)
			throw new BusinessException(BizCodeEnum.REGISTER_ERROR);
		admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
		return adminRepository.save(admin);
	}

	@Override
	public Object login(String userName, String password) throws AuthenticationException {
		UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
		final Authentication authentication = authenticationManager.authenticate(upToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtTokenUtil.generateToken(userName);
		((Admin) authentication.getPrincipal()).setAuthToken(token);
		((Admin) authentication.getPrincipal())
				.setRights(adminRightService.getAdminRights(((Admin) authentication.getPrincipal()).getId()));
		stringRedisTemplate.opsForValue().set(token, "true");
		return authentication.getPrincipal();
	}

	@Override
	public String refreshToken(String token) {
		return null;
	}

	@Override
	@Transactional
	public String updatePassword(String username, String password, String oldPassword, String mobile, String smsCode) {

		Admin admin = adminRepository.findDistinctFirstByUsername(username);
		if (admin == null) {
			throw new BusinessException(BizCodeEnum.USER_NAME_INEXISTENCE);
		}
		if (!mobile.equals(admin.getMobile())) {
			throw new BusinessException(BizCodeEnum.MOBILE_NUM_INCONSISTENT);
		}
		String code = stringRedisTemplate.opsForValue().get(SmsSender.VERIFICATION_CODE_KEY_PREFIX + mobile);
		if (!smsCode.equals(code)) {
			throw new BusinessException(BizCodeEnum.WRONG_SMS_CODE);
		}
		UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, oldPassword);
		final Authentication authentication = authenticationManager.authenticate(upToken);
		if (authentication == null) {
			throw new BusinessException(BizCodeEnum.OLD_PASSWORD_ERROR);
		}
		// if(!StringUtils.isEmpty(oldPassword)) {
		// if(!bCryptPasswordEncoder.encode(oldPassword).equals(admin.getPassword())) {
		// throw new BusinessException(BizCodeEnum.OLD_PASSWORD_ERROR);
		// }
		// }
		adminRepository.updatePassword(username, bCryptPasswordEncoder.encode(password));
		return "Update Sucess";

	}
}
