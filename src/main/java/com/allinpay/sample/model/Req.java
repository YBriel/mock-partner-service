/**
 * create this file at 上午9:50:55 by renhd.
 */
package com.allinpay.sample.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * @author 任海东 2020年3月20日
 *
 */
@Data
public class Req implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8217776621796157561L;

	private String appId;
	private String clientAppId;
	private String method;
	private String format;
	private String charset;
	private String signType;
	private String sign;
	private String timestamp;
	private String version;
	private String bizContent;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
