package com.my.ssm.controller.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class CustomDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		// 实现将日期串转换成日期类型格式（yyyy-MM-dd HH:mm:ss）
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 参数绑定失败返回空
		return null;
	}

}
