package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jettison.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;


/**
 * @author phan at 2016年2月15日
 */
@Component
public class AccessTokenUtil {

	private HttpSender httpSender;

	private String appId = "wxa50ffd87271d6e9e";
	private String appSecret = "PP6oO5miSKsQsRTDrZaaHLxvZx2hQT9PnWXtESRPP6anCMxaIgfITTIo-riM1Pnu";
	private AccessToken accessToken;
	private long currentTime = 0;
	private long freshTime = 1000 * 60 * 60;

	public AccessToken getAccessToken() {
		/*
		 * 增加java锁，在accesstoken过期后，
		 * 假如同时收到好几个请求，这时可以保证请求的正常执行顺序
		 */
		synchronized (this) {
			if ((System.currentTimeMillis() - currentTime) > freshTime) {
				accessToken = getAccessToken(appId, appSecret);
				if (!accessToken.getToken().equals(null) && !accessToken.getToken().equals("")) {//如果accessToken获取成功
					System.out.println("accessToken获取成功：" + accessToken.getToken());
					currentTime = System.currentTimeMillis();
				}
			}

			return accessToken;
		}
	}

	public AccessToken getAccessToken(String appId, String appSecret) {
		AccessToken accessToken = new AccessToken();
		//访问微信服务器端
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
		//获取token
		httpSender = new HttpSender();
		String accessTokenString = httpSender.sendGet(url, "corpid=" + appId + "&corpsecret=" + appSecret, 3);
		System.out.println("构造url后，accessToken：" + accessTokenString);
		JSONObject json = JSONObject.fromObject(accessTokenString);

		if (json.containsKey("NULL")) {
			return null;
		}

		accessToken.setToken(json.optString("access_token"));
		accessToken.setExpiresIn(new Integer(json.optInt("expires_in")));

		return accessToken;
	}

	// 获取访问权限码URL
	private final static String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

	public static String getAccessToken(HttpServletRequest request) {
		if (request == null) {
			return refleshAccessToken(null);
		}
		AccessToken accesstoken = null;
		ServletContext context = request.getSession().getServletContext();
		accesstoken = (AccessToken) context.getAttribute("accesstoken");
		if (accesstoken != null) {
			Date createTime = accesstoken.getCreateTime();
			if ((new Date().getTime() - createTime.getTime()) < 7200 * 1000) {
				System.out.println("accesstoken:" + accesstoken.getAccessToken());
				return accesstoken.getAccessToken();
			}
		}
		return refleshAccessToken(request);

	}

	private static String refleshAccessToken(HttpServletRequest request) {
		String result = null;
		HttpClient client = HttpClients.createDefault();
		//https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=id&corpsecret=secrect
		HttpGet get = new HttpGet(ACCESS_TOKEN_URL + "?corpid=" + PropertyUtil.getProperty("CorpID") + "&corpsecret=" + PropertyUtil.getProperty("CorpSecret"));
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
					String line = null;
					StringBuffer res = new StringBuffer();
					while ((line = br.readLine()) != null) {
						res.append(line);
					}
					System.out.println(res.toString());
					try {
						JSONObject json = JSONObject.fromObject(res.toString());
						result = json.getString("access_token");
						if (request != null) {
							ServletContext context = request.getSession().getServletContext();
							AccessToken at = new AccessToken();
							at.setAccessToken(result);
							at.setCreateTime(new Date());
							context.setAttribute("accesstoken", at);
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		get.releaseConnection();
		System.out.println("accesstoken:" + result);
		return result;

	}
}
