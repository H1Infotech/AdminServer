package com.h1infotech.smarthive.service;

import org.springframework.stereotype.Service;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Service
public class SensorDataEvaluationServiceImpl implements SensorDataEvaluationService {
	private static final String DEFAULT_CONTEXT_KEY = "o";
	private static final ExpressionParser PARSER = new SpelExpressionParser();
	
	@Override
	public boolean evaluate(String ruleExpression, Object context) {
		 EvaluationContext ctx = new StandardEvaluationContext();
	     ctx.setVariable(DEFAULT_CONTEXT_KEY, context);
	     return (boolean) PARSER.parseExpression(ruleExpression).getValue(ctx);
	}
}
