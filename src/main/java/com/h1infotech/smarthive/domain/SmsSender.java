package com.h1infotech.smarthive.domain;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.SmsSingleSend;
import org.apache.commons.lang3.StringUtils;
import static com.yunpian.sdk.constant.YunpianConstant.*;

public final class SmsSender {
    private static final int VERIFICATION_TEMPLATE_ID = 2298872;
    private final YunpianClient client = new YunpianClient("c26b373454f5dd4400e43304e896b478").init();

    @SuppressWarnings("deprecation")
	private Result<SmsSingleSend> send(String mobile, int templateId, Map<String, String> params) {
        Map<String, String> param = client.newParam(3);
        param.put(MOBILE, mobile);
        param.put(TPL_ID, String.valueOf(templateId));
        param.put(TPL_VALUE, getParamsAsString(params));
        return client.sms().tpl_single_send(param);
    }

    private String getParamsAsString(Map<String, String> params) {
        List<String> items = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            items.add("#" + entry.getKey() + "#=" + entry.getValue());
        }
        return StringUtils.join(items, "&");
    }

    public Result<SmsSingleSend> sendVerificationCode(String mobile, String type, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("code", code);
        return send(mobile, VERIFICATION_TEMPLATE_ID, params);
    }
}
