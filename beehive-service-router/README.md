## Beehive Service Router
一个可以通过配置快速访问RestService的工具，你不需要关心Http连接池和连接管理，只需要关心你的业务逻辑。

## Beehive Service Router的功能

* 通过yml文件配置，快速访问RestService
* 支持多种负载均衡策略，默认使用一致性hash

## 使用示例
### 配置文件
参数说明：
* name: 服务名称
* path: 服务路径，支持变量
* method: 请求方法，支持GET、POST、PUT、DELETE
* timeout: 超时时间
* maxTotal: 最大连接数
* maxPerRoute: 每个路由的最大连接数
* maxRetry: 最大重试次数
* deserializeType: 反序列化响应类型，支持Json、Xml、String、Object
  > 注意：com.chengjungao.base.service.autoconfigure.DeserializeType枚举类中的枚举值
  
```yaml
beehive:
  router:
    services:
      - name: serviceTest
        path: "/blog/${bogId}"
        method: GET
        timeout: 5000
        maxTotal: 2
        maxPerRoute: 1
        maxRetry: 2
        deserializeType: Xml
        urls:
          - http://www.chengjungao.cn
```
### 代码使用示例
```java
  @Resource
  private ServiceRouter serviceRouter;

  @Test
  public void test() {
  CommonParams params = new CommonParams();
  params.addParam("bogId", "11");
  Document res = serviceRouter.execute("serviceTest",params);
  Assert.assertNotNull(res.title());
  System.out.println(res.title());
}
```
