/**
 * create this file at 上午9:48:45 by renhd.
 */
package com.allinpay.sample.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allinpay.sample.model.Req;
import com.allinpay.sample.model.Resp;
import com.allinpay.sdk.OpenClient;
import com.allinpay.sdk.bean.BizParameter;
import com.allinpay.sdk.bean.OpenResponse;
import com.allinpay.sdk.util.SecretUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 任海东 2020年3月20日
 *
 */
@RestController
@Slf4j
public class MockController {

	@Autowired
	private OpenClient openClient;

	/**
	 * 模拟客户调用服务商的接口method
	 */
	private static final String MOCK_API_METHOD = "allinpay.top.mock.api";

	/**
	 * 开放平台开通服务商产品通知调用服务商的method
	 */
	private static final String MOCK_CHANGE_PRODUCT_METHOD = "allinpay.top.info.companyInfoNotify";

	private static final String REDIRECT_METHOD = "allinpay.top.common.jump";

	/**
	 * 模拟服务商接口
	 *
	 * @param req
	 * @return
	 */
	@RequestMapping("/mock")
	public Resp mockApi(final Req req) {
		log.info(">>>>>>>>>>  req:{}", req);
		final long begin = System.currentTimeMillis();
		final Resp resp = new Resp();
		try {
			final String signedValue = getSignedSource(req);
			if (!openClient.checkSign(signedValue, req.getSign())) {
				resp.setCode("1000");
				resp.setMsg("check signinature error");
				resp.setSign(sign(resp));
				return resp;
			}
			final String method = req.getMethod();
			final Map<String, Object> bizData = JSON.parseObject(req.getBizContent());
			if (MOCK_API_METHOD.equals(method)) {
				resp.setCode("0000");
				resp.setMsg("");
				final Map<String, Object> map = new HashMap<>();
				map.put("appId", req.getClientAppId());
				map.put("filed1", "abcdefg");
				map.put("filed2", 123456L);
				resp.setData(JSON.toJSONString(map));
				resp.setSign(sign(resp));
				log.info("<<<<<<<<<< resp:{} ,total costs:{}ms", resp, System.currentTimeMillis() - begin);
				return resp;
			}
			if (MOCK_CHANGE_PRODUCT_METHOD.equals(method)) {
				resp.setCode("0000");
				resp.setMsg("");
				final Map<String, Object> map = new HashMap<>();
				map.put("appId", req.getClientAppId());
				map.put("productId", bizData == null ? "" : bizData.get("productId"));
				map.put("productAppId", System.currentTimeMillis() + "");
				map.put("resultCode", "success");
				map.put("resultMsg", "开通成功");
				resp.setData(JSON.toJSONString(map));
				resp.setSign(sign(resp));
				return resp;
			}
			resp.setCode("2000");
			resp.setMsg("not find service for method:" + method);
			resp.setSign(sign(resp));
			log.info("costs:{}ms", System.currentTimeMillis() - begin);
			return resp;
		} catch (final Exception e) {
			resp.setCode("9999");
			resp.setMsg(e.getMessage());
			return resp;
		}
	}

	/**
	 * 模拟服务商产品开通成功通知开放平台
	 */
//	@RequestMapping("/mockOpenResultNotice")
	public void mockOpenResultNotice() {
		try {
			final BizParameter map = new BizParameter();
			map.put("appId", "1298815853533458434");
			map.put("productId", "1259807951602946049");
			map.put("productAppId", System.currentTimeMillis() + "");
			map.put("resultCode", "success");
			map.put("resultMsg", "开通成功");
			final OpenResponse response = openClient.execute("allinpay.top.info.updateAppInfo", map);
			if (response != null) {
				log.info(response.getData());
			}
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 模拟服务商跳转到客户系统页面
	 */
//	@RequestMapping("/mockJumpClientUrl")
	public void mockJumpClientUrl(final HttpServletResponse response) {
		try {
			final BizParameter param = new BizParameter();
			// 组装通知参数
			param.put("bizField1", "aaabbbccc");
			final String clientAppId = "1298815853533458434";// 客户的应用id
			final String jumpUrl = "https://www.baidu.com";// 跳转到客户系统的url
			response.sendRedirect(openClient.concatUrlForServer(REDIRECT_METHOD, param, jumpUrl, clientAppId));
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 模拟服务商调用开放平台查询企业信息接口
	 *
	 * @param companyId
	 * @return
	 */
//	@RequestMapping("/mockGetCompanyInfo")
	public String mockGetCompanyInfo(@RequestParam("companyId") final String companyId) {
		try {
			final BizParameter map = new BizParameter();
			map.put("companyId", companyId);
			final OpenResponse response = openClient.execute("allinpay.top.info.getCompanyInfo", map);
			if (response != null) {
				log.info("response:{}", response);
				return response.toString();
			}
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
		return "failed";
	}

	/**
	 * 模拟服务商发送异步通知到客户系统
	 *
	 */
//	@RequestMapping("/mockSendAsyncNotify")
	public void mockSendAsyncNotify() {
		try {
			final BizParameter param = new BizParameter();
			// 组装通知参数
			param.put("bizField1", "aaabbbccc");
			final String clientAppId = "1298815853533458434";// 客户的应用id
			final String notifyUrl = "https://www.baidu.com";// 通知到到客户系统的url
			final String notifyType = "allinpay.yunst.OrderService.pay";// 服务商自定义
			openClient.asynNotifyForServer(notifyType, param, notifyUrl, clientAppId);
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 响应待签名字符串
	 *
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	private String sign(final Resp resp) throws Exception {
		final String signedSource = JSON.toJSONString(resp, SerializerFeature.SortField);
		final String sign = SecretUtils.sign(openClient.getPrivateKey(), signedSource, "SHA256WithRSA");
		log.info("signedSource:{},sign:{}", signedSource, sign);
		return sign;
	}

	/**
	 * 获取请求签名源串
	 *
	 * @param req
	 * @return
	 */
	private String getSignedSource(final Req req) {
		final Map<String, Object> reqMap = (JSONObject) JSON.toJSON(req);
		reqMap.remove("sign");
		reqMap.remove("signType");
		final Map<String, Object> copy = new TreeMap<>();
		reqMap.forEach(copy::put);
		final StringBuilder sb = new StringBuilder();
		copy.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
		return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1);
	}

}
