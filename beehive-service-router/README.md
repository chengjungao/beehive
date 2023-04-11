## Beehive Service Router
一个可以通过配置快速访问RestService的工具，你不需要关心Http连接池和连接管理，只需要关心你的业务逻辑。

## Beehive Service Router的功能

* 通过yml文件配置，快速访问RestService
* 支持多种负载均衡策略，默认使用一致性hash

## 使用示例

```java
		List<String> nodeList = new ArrayList<>();
		nodeList.add("redis://*.*.*.*:*");
		RedisConfig redisConfig = new RedisConfig(nodeList, "TestRedis", 10, 5000, "****");
				
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
		
		//获取缓存值
		cache.get(new BeehiveCacheKey<String>("https://www.baidu.com/"));
```
