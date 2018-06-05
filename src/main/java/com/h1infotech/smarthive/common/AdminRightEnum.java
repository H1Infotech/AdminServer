package com.h1infotech.smarthive.common;

public enum AdminRightEnum {
	ADMIN_MANAGEMENT(1),
	ORGANIZATION_MANAGEMENT(2),
	BEEFARMER_MANAGEMENT(3),
	BEEBOX_MANAGEMENT(4),
	EVENT_MANAGEMENT(5),
	ADMIN_QUERY(6),
	ORGANIZATION_QUERY(7),
	BEEFARMER_QUERY(8),
	BEEBOX_QUERY(9),
	EVENT_QUERY(10),
	;
	
	private int right;
	
	private AdminRightEnum(int right) {
		this.right = right;
	}
	
	public int getRight() {
		return right;
	}
	
	public AdminRightEnum getRight(int right) {
		for(AdminRightEnum element: AdminRightEnum.values()) {
			if(element.right == right) {
				return element;
			}
		}
		throw new BusinessException(BizCodeEnum.NO_RIGHT);
	}
}
