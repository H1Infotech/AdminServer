package com.h1infotech.smarthive.common;

import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class MyUtils {
	
	public static String getStringPinYin(String pinYinStr) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);		
		String tempStr = null;
		StringBuffer sb = new StringBuffer();
        for(int i = 0; i<pinYinStr.length(); i++) {
            try {
				tempStr = PinyinHelper.toHanyuPinyinStringArray(pinYinStr.charAt(i), format)[0];
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
            if(tempStr == null) {
                sb.append(pinYinStr.charAt(i));
            }else {
                sb.append(tempStr);
            }
        }
        return sb.toString();
    }
	
	public static String getUserName(String pinYinStr) {
		Random random = new Random();
		String name = getStringPinYin(pinYinStr);
		String ranmdNum = String.valueOf(random.nextInt(1000));
		for(int i =0; i < 3-ranmdNum.length(); i++) {
			ranmdNum = "0"+ranmdNum;
		}
		return name+ranmdNum;
	}
}
