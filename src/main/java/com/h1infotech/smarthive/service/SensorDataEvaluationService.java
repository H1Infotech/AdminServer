package com.h1infotech.smarthive.service;

public interface SensorDataEvaluationService {
	boolean evaluate(String ruleExpression, Object context);
}
