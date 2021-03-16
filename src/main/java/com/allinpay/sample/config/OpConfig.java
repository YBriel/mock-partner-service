/**
 * create this file at 上午10:12:12 by renhd.
 */
package com.allinpay.sample.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.allinpay.sdk.OpenClient;
import com.allinpay.sdk.bean.OpenConfig;

/**
 * @author 任海东 2020年3月20日
 *
 */
@Configuration
public class OpConfig {

	@Bean
	@ConfigurationProperties(prefix = "op")
	public OpenConfig openConfig() {
		return new OpenConfig();
	}

	@Bean
	public OpenClient openClient(final OpenConfig openConfig) throws Exception {
		return new OpenClient(openConfig);
	}

}
