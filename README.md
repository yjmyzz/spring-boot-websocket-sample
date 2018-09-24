# Spring-Boot 2.x based WebSocket Demo

说明：
 
 1. 包括了server端, 以及js、java客户端（默认端口为8080）
 2. model里面的xxxMessage对象，必须要有默认无参构造函数，否则java 客户端 topic订阅无效（也不会报错！）
 3. server端的入口类DemoWebSocketServer，不要移到其它子级package下，不然client会连不上。
 4. java client端的入口为DemoWebSocketClient，连上Server端后，terminal端打字回车，即可向server发送消息。
 5. js client端的地址为http://localhost:8080/ 先connect后，再send
 
 ---
 
 by [菩提树下的杨过](https://www.cnblogs.com/yjmyzz/p/spring-boot-2-websocket-sample.html) @2018