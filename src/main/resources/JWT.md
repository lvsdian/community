## JWT

- `json web token`一般用于用户认证(前后端分离/微信小程序/app开发)

### 基于传统的token认证

- 用户登录，服务器返回token，并将token保存在服务端
- 以后用户再来访问时，需要携带token，服务端获取token后去数据库中取token进行校验

### jwt

- 用户登录，服务端给用户返回一个token（服务端不保存）
- 以后用户再来访问时，需要携带token，服务端获取token后，校验token

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

- jwt生成的token由三段字符串组成，用 . 连接起来

  - 第一段：header，内部包含算法/token类型
  - 将header转为字符串再进行base64url加密作为第一段
  - base64url：先用base64进行加密，然后把+ _等符号替换成特殊字符（可以反解）

  ```json
  {
    "alg": "HS256",
    "typ": "JWT"
  }
  ```

  - 第二段：payload，自定义值
  - 将payload转为字符串再进行base64url加密作为第二段（可以反解）

  ```json
  {
    "sub": "1234567890",
    "name": "John Doe",
    "iat": 1516239022
  }
  ```

  - 第三段：将第一、二段密文拼接起来；拼接后进行HS256加密，加盐；加密后的密文再进行base64url加密。（不可以反解）

- 以后用户再来访问时，需要携带token，后端进行校验
  - 获取token、分割token
  - 将第二段进行base64url解密，获取payload信息，判断是否过期
  - 如果没过期，将第一、二段重新进行HS256加密(因为第一段中指定了加密算法为HS256)，与第三段比对。

- 优势
  - 相较于传统的token，无需在服务端保存token，减轻服务端压力
  - 跨语言
- 缺点
  - 一旦签发，无法修改，不能更新有效期，也不能销毁
  - 不包含权限控制(Spring Security可以做权限管理)

- 使用jwt做登录凭证，如果解决token注销问题
  - jwt缺陷是token生成后无法修改，所以只能采取其他方案弥补
  - 适当减短token有效期，让token尽快失效
  - 用户登录，生成JWT，把JWT的id放入redis，只有redis中有id的JWT才是有效的
- token有效期短，如何解决续签问题
  - 判断用户登录的代码中，加逻辑判断：如果cookie快到期时，重新生成一个token
- 如何解决异地登录问题
  - 在Redis中记录登录用户的token信息，即变成了有状态的登录
- cookie被禁用怎么办
  - 给个提示，你的cookie被禁用了，请启用cookie
  - 把jwt作为响应头返回，然后浏览器中JS把token写到sessionStorage里，每次请求都要求携带token。

- 如果解决cookie被篡改
  - cookie改后服务端验证不会通过