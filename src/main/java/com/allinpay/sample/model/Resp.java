/**
 * create this file at 上午9:55:09 by renhd.
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
public class Resp implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 2748950518217719709L;

	private String code;
	private String msg;
	private String sign;
	private String data;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
