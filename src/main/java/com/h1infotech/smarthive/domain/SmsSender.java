package com.h1infotech.smarthive.domain;

import java.util.Map;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import com.yunpian.sdk.model.Result;
import java.util.concurrent.TimeUnit;
import com.yunpian.sdk.YunpianClient;
import javax.annotation.PostConstruct;
import com.yunpian.sdk.model.SmsSingleSend;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import static com.yunpian.sdk.constant.YunpianConstant.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

@Component(value = "smsSender")
public class SmsSender {
	private Logger logger = LoggerFactory.getLogger(SmsSender.class);

	public final static String VERIFICATION_CODE_KEY_PREFIX="VERIFICATION_CODE_FOR_";
	
    @Value("${sms.apikey}")
    private String smsApiKey;
    private YunpianClient client;

    @PostConstruct
    private void init() {
        client = new YunpianClient(smsApiKey).init();
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    private String getParamsAsString(KV... params) {
        List<String> items = new ArrayList<>();
        for (KV kv : params) items.add(kv.toString());
        return StringUtils.join(items, "&");
    }

    @SuppressWarnings("deprecation")
	public Result<SmsSingleSend> send(SmsTemplateEnum smsTemplateEnum, String mobile, KV... params) {
        Map<String, String> param = client.newParam(params.length + 1);
        param.put(MOBILE, mobile);
        param.put(TPL_ID, String.valueOf(smsTemplateEnum.templateId));
        param.put(TPL_VALUE, getParamsAsString(params));
        return client.sms().tpl_single_send(param);
    }

    public void dispatchSMSService(String serviceType, String mobile) {
    	if(SmsSender.SmsTemplateEnum.VERIFICATION_CODE.getTemplateId().equals(serviceType)) {
    		Random random = new Random();
    		String codeNum = String.valueOf(random.nextInt(9000)+1000);
    		KV promopt = new KV("type","登录");
    		KV code = new KV("code",codeNum);
    		this.send(SmsSender.SmsTemplateEnum.VERIFICATION_CODE, mobile, promopt,code);
    		stringRedisTemplate.opsForValue().set(VERIFICATION_CODE_KEY_PREFIX+mobile, codeNum, 15, TimeUnit.MINUTES);
    		logger.info("====Sending Verification Code: {}====", codeNum);
    	}
    }
    public static enum SmsTemplateEnum {
        BOX_NOT_EXISTS("2298882"), BOX_DATA_NOTICE("2298880"),
        VERIFICATION_CODE("2298872"), BOX_EXCEPTION_NOTIFICATION("2298876");
        private final String templateId;

        SmsTemplateEnum(String templateId) {
            this.templateId = templateId;
        }
        
        public String getTemplateId() {
        	return this.templateId;
        }
    }

    public static class KV {
        final String key;
        final String value;

        public KV(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String getKey() {
            return key;
        }

        private String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("#%s#=%s", getKey(), getValue());
        }
    }
    
}
