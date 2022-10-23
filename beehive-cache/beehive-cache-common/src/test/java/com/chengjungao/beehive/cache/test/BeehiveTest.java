package com.chengjungao.beehive.cache.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.chengjungao.beehive.cache.CacheLoader;
import com.chengjungao.beehive.cache.config.CacheConfig;
import com.chengjungao.beehive.cache.config.RedisConfig;
import com.chengjungao.beehive.cache.impl.BeehiveCache;
import com.chengjungao.beehive.cache.impl.BeehiveCacheKey;

public class BeehiveTest {
	private BeehiveCache<String, String> cache;
	
	@Before
	public void init() {
		List<String> nodeList = new ArrayList<>();
		nodeList.add("redis://8.140.172.9:9091");
		RedisConfig redisConfig = new RedisConfig(nodeList, "TestRedis", 10, 5000, "627@Cheng");
				
		CacheConfig cacheConfig = new CacheConfig("TestBusiness",redisConfig);
		cache = new BeehiveCache<>(cacheConfig, new CacheLoader<String, String>() {

			@Override
			public String load(String key) throws Exception {
				BufferedReader in = null;
				try {
		            URL url = new URL(key);
		            URLConnection urlConnection = url.openConnection();
		            HttpURLConnection connection = null;
		            if (urlConnection instanceof HttpURLConnection) {
		                connection = (HttpURLConnection) urlConnection;
		            } else {
		                throw new RuntimeException("load error!" + key);
		            }

		            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		            StringBuilder content = new StringBuilder();
		            String current;

		            while ((current = in.readLine()) != null) {
		            	content.append(current);
		            }
		            return content.toString();
		        } catch (IOException e) {
		        	throw new RuntimeException("load error!" + key);
		        }finally {
					if (in != null) {
						in.close();
					}
				}
			}

			@Override
			public Map<String, String> loadAll(Iterable<? extends String> keys) {
				throw new RuntimeException("LoadAll is not supported!");
			}
		});
	}
	
	@Test
	public void test() throws Exception {
		for (int i = 0; i < 10; i++) {
			long start = System.currentTimeMillis();
			cache.get(new BeehiveCacheKey<String>("https://www.baidu.com/"));
			System.out.println(System.currentTimeMillis() - start);
			Thread.sleep(1000);
		}
		System.out.println(cache.get(new BeehiveCacheKey<String>("https://www.baidu.com/")).getDataValue());
		System.out.println(cache.stats());
		
	}
}
