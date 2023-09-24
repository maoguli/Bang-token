![](doc/images/logob.png)

# ElegentToken【令牌管理框架】

### 介绍

​	ElegentToken是一款互联网项目的轻量级令牌管理框架。 这个框架可以让你更优雅地在互联网项目中实现Token创建，校验，续期。这个组件具有的特点：

1. 轻量级的Toekn框架，更易于学习，快速上手。
2. 以拦截器的方式实现了token的续期（双token方式），校验。
3. 基于配置的方式提供了对称加密和非对称加密的两种方式，与业务系统的登录功能解耦。
4. 既适用于基于微服务网关的分布式架构，也适用于单体的Springboot微服务架构。

### 使用说明

#### 微服务网关

如果你的业务系统是基于微服务网关gateway的分布式架构，那么需要做如下处理。

###### 依赖导入

1.在微服务网关中引入依赖

```xml
<dependency>
    <groupId>cn.elegent</groupId>
    <artifactId>elegent-token-gateway</artifactId>
    <version>1.0.0</version>
</dependency>
```
###### 网关配置

2.在微服务网关配置文件添加配置。

```yaml
elegent:
  token:
    verify:
      ####项目中采用对称加密的方式#####
      type: symmetric
      #####对称加密秘钥自定义#####
      secret: elegent
    ignore:
      urls: /user/login # 有些请求地址不需要校验
    header: #头信息
      access:
        accessName: accessToken
        lifespan: 2 # 过期时间2h
        timeUnit: H
      refresh:
        refreshName: refreshToken
        lifespan: 30 # 过期时间30天
        timeUnit: D
```

###### 配置说明

​	公共配置

  （1）type：加密方式，对称加密方式和非对称加密方式。对称加密（symmetric）非对称加密（asymmetric）默认是对称加密的方式。

  （2）ignore_url：忽略地址，例如登录地址，是不需要提取token的。

  （3）header.access.accessName:  双token方式的访问token在请求头中的名称，默认值是access_token。

  （4）header.access.lifespan: 双token方式的访问token的有效期，默认 2

  （5）header.access.timeUnit: 双token方式的访问token的有效期时间单位支持（S:秒 M:分钟 H:小时 D:日 ），默认H

  （6）header.refresh.accessName:  双token方式的刷新token在请求头中的名称，默认值是refresh_token。 

  （7）header.refresh.lifespan: 双token方式的刷新token的有效期 ，默认：30

  （8）header.refresh.timeUnit: 双token方式的刷新token的有效期时间单位支持（S:秒 M:分钟 H:小时 D:日 ）默认：D

​    特殊配置：

​		 当type配置为 **symmetric** 时为对称加密

​		（1）verify.secret：对称加密的密钥

​		  当type配置为 **asymmetric** 时为非对称加密

​		（2）verify.secret.filePath.priKeyPath：私钥存放路径

​		（3）verify.secret.filePath.pubKeyPath：公钥存放路径

#### 微服务代码

##### 鉴权微服务

###### 依赖导入

```xml
<dependency>
    <groupId>cn.elegent</groupId>
    <artifactId>elegent-token-manager</artifactId>
    <version>1.0.0</version>
</dependency>
```

###### 微服务配置

在微服务中添加如下配置

```yml
elegent:
  token:
    verify:
      ####项目中采用对称加密的方式#####
      type: symmetric
      #####对称加密秘钥要一样的自定义#####
      secret: elegent
    ignore:
      urls: /user/login
    header:
      access:
        accessName: accessToken
        lifespan: 2
        timeUnit: H
      refresh:
        refreshName: refreshToken
        lifespan: 30
        timeUnit: D
```

###### 构建系统用户对象

```java
@Data
public class UserVO extends ElegentUser{//UserVO允许用户自己去拓展自己添加开闭原则   ElegentUser是核心的要放到载荷的用户信息

    private String username;

    private String name;

    private Integer age;

}
```

###### 登录与签发token

```java
@GetMapping("/login")
public RefreshDTO login(){
    //这里应该是模拟从数据库读取登陆用户对象
    UserVO userVO = new UserVO("admin", "张三", 20);//Vo是上面的三个值
    userVO.setUserId("1");
    //构建JWT
    RefreshDTO token = tokenService.createToken(userVO);//放入载荷信息里面包含两个token一个是access一个是refresh
    return token;
}
```

userService.createToken为Token框架封装的利用JWT创建token的方法，该方法会基于用户配置的密钥方式（对称，非对称）来生成访问token和刷新token。



###### 刷新token接口

Auth框架内置了一个刷新token的接口，会基于用户的配置内容来进行token的刷新

用户只需要进行Post请求 

```http
POST  /elegent/refreshToken # 只要引入了这个框架的依赖这个接口就有了是用来刷新两个token的 不需要自己去写
Content-Type: application/json

{
    "refreshToken":"登录时拿到的刷新token"
}
```

请求该接口即可进行token的刷新

```json
{
  "accessToken": {
    "token": "eyJhbGciOiJSUzI1NiJ9.eyJ1c2VySWQiOiIxIiwidXNlcm5hbWUiOiJhZG1pbiIsIm5hbWUiOiJ6aGFuZ3NhbiIsImFnZSI6MjAsImp0aSI6ImVsZWdlbnRUb2tlbiIsImV4cCI6MTY3MjcyMjE0MX0.U26pf5qCU_2RWpNdXXkK6TP9YY9_y_Tg6aILYlWADFNn1fG1BLPAR4QeB0CqJRMOU3ZX_LDVimK5c_EOuzVxGxaDqymw_-R5ZBe3uXGDNVlJeNwYKymj0TAmBKL0jEJJV0ys4G00AIjnAWcOueGie7C54u8DJkWZoxxdH2p_JVrL0nonJ93SZIVx-Yzr2kOF9PiK6PW1KaGmZlcHG9w0Opx0LGX9sQRNJObB08nhGljPn16k4--OZmaCSmZ0RHTBc0KupAwis0jgb-vYh3iong5Ou8Nhhb_BQ4PHXq1hg5LPVtQW4iK979nnnQBljL_yR4IzqhkxzaEJDWS75w7TAw",
    "lifespan": "2022-12-31 13:02:21"
  },
  "refreshToken": {
    "token": "eyJhbGciOiJSUzI1NiJ9.eyJlbGVnZW50VXNlcklkIjoiTVE9PSIsImp0aSI6ImVsZWdlbnRUb2tlbiIsImV4cCI6MTY3NTMwNjk0MX0.D777-ArWeQZz-xl78RX580p9n9hEbjDUzqi-lChmRk7VmmSSmrCZ415lk-A0G8r2wKiLxGVhzBFy0zngbOpTkX5-11sa5gPZ3R_BdtphQFbnamMDe1hBzf9npcm3haqvd-K91Fqp18VjSPCkkz_B3H1udEe4k6I1J85uC4bGzP_4aMMgzukI3aUwF_Wv7Jga6ZzvI6kBtAL0Xk6KY_gWmJhO_QA6qkCA3vRyu3eDc1oUzTKXjhyy3Wj1Yi1Ch_FUi1kr2UhOA1-d9q5yxXrBqDtvHoTPl9u9d99clvTWX6UmNr1noXH7A4MxiuQFScDpfCgLDS-uP1eIGQoIMpw2dw",
    "lifespan": "2022-1-01 11:02:21"
  }
}
```

###### 返回值解析 前段可能需要直接返回就行

|        参数名         |                        中文含义                         |
| :-------------------: | :-----------------------------------------------------: |
|   accessToken.token   |                      访问token内容                      |
| accessToken.lifespan  |   访问token截止日期，供前端自行判断何时进行token刷新    |
|  refreshToken.token   |                      刷新token内容                      |
| refreshToken.lifespan | 刷新token截止日期，刷新token超过有效期无法进行token刷新 |

###### 状态码判断

若刷新token接口，返回状态码403则需要进行重新登录（说明刷新token已失效）

###### 刷新校验

框架已经提供了默认的刷新校验，即不校验刷新token可以随时进行刷新,但是不同的业务系统对于刷新行为有自己的处理要求。用户可以基于自己系统的特点来自定义自己的刷新规则。

用户可能需要进行一定的token拦截或校验，我们定义了一个CustomService供用户来进行自定义操作

下列代码基于Auth框架提供的CustomService来实现一个刷新token只能刷新一次。

其中Token框架提供的CustomService方法提供了3个方法

ckeckRefreshToken();在请求刷新token接口时用户需要自定义校验的逻辑

customHook();在登录时创建完成token之后执行的方法

afterRefreshHook();在token刷新成功之后执行的方法

```java
@ConditionalOnProperty(prefix = "elegent.token.custom",name = "check",havingValue = "one")
@Service
public class RefreshTokenServiceImpl implements CustomService {

    private final static String tokenKey = "refreshToken:";

    private final static String userIdKey = "userId";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用于校验刷新token
     * @param refreshToken
     * @param user
     * @return
     */
    @Override
    public Boolean ckeckRefreshToken(String refreshToken,Object user) {
        String redisKey = getRedisKey(user);
        //如果刷新token在redis中存在  并且当前redis中的刷新token就是传入的刷新token
        if((redisTemplate.opsForValue().get(redisKey)!=null)&&(refreshToken.equals(redisTemplate.opsForValue().get(redisKey)))){
            //表明本次请求刷新token是被允许的 将原有的redis的key删除
            redisTemplate.delete(redisKey);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 记录刷新token到Redis中
     * @param accessToken
     * @param refreshToken
     */
    @Override
    public void customHook(TokenDTO accessToken, TokenDTO refreshToken,Object user) {
        String redisKey = getRedisKey(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //计算相差多少秒
            Long secDiff = dateDiff(new Date(), sdf.parse(refreshToken.getLifespan()));
            redisTemplate.opsForValue().set(redisKey, refreshToken.getToken(), secDiff, TimeUnit.SECONDS);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 在重新生成刷新token之后将刷新token存入redis中
     * @param refreshDTO
     * @param user
     */
    @Override
    public void afterRefreshHook(RefreshDTO refreshDTO, Object user) {
        String redisKey = getRedisKey(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //计算相差多少秒
            Long secDiff = dateDiff(new Date(), sdf.parse(refreshDTO.getRefresToken().getLifespan()));
            redisTemplate.opsForValue().set(redisKey, refreshDTO.getRefresToken().getToken(), secDiff, TimeUnit.SECONDS);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据框架提供的用户对象获取redis里的key
     * @param user
     * @return
     */
    private String getRedisKey(Object user){
        String redisKey = null;
        try {
            Map<String, Object> payLoadMap = JsonUtil.convertToMap(user);
            redisKey = tokenKey + payLoadMap.get(userIdKey);
            return redisKey;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 计算2个时间相差的天数、小时、分钟、秒
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @return
     */
    public static Long dateDiff(Date startTime, Date endTime) {
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long second = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = endTime.getTime() - startTime.getTime();
            // 计算差多少秒
            second = diff / ns;
            // 输出结果
            /*System.out.println("hour=" + hour + ",min=" + min);*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return second;
    }
}
```

##### 业务微服务

在其它的微服务，如果需要获取当前登录用户信息，引入以下依赖

```xml
<dependency>
    <groupId>cn.elegent</groupId>
    <artifactId>elegent-token-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

###### 用户信息的获取 在业务方法中通过ElegentTokenContext获取当前用户。

```java
@GetMapping("/getUserInfo")
public Map test() {
    return new HashMap() {
        {
            put("user", ElegentTokenContext.getLoginUser());//只需要通过这个两个方法就可以获取用户信息
            put("userId",ElegentTokenContext.getUserId());
        }
    };
}
```

框架提供了ElegentTokenContext上下文来获取当前的登录用户对象

### 参与贡献

1.  从 `master` 分支 `checkout` 一个新分支（**注**：*请务必保证 master 代码是最新的*）
2.  新分支命名格式：`docs/username_description`，例如：`docs/tom_新增分布式锁配置项`
3.  在新分支上编辑文档、代码，并提交代码
4.  最后 `PR` 合并到 `develop` 分支，等待作者合并即可

