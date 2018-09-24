package com.cnblogs.yjmyzz.websocket.demo.controller;

import com.cnblogs.yjmyzz.websocket.demo.consts.GlobalConsts;
import com.cnblogs.yjmyzz.websocket.demo.model.ServerMessage;
import com.cnblogs.yjmyzz.websocket.demo.model.ClientMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author junmingyang
 * @date 2018/9/24 7:03 PM
 */
@Controller
public class GreetingController {

    @MessageMapping(GlobalConsts.HELLO_MAPPING)
    @SendTo(GlobalConsts.TOPIC)
    public ServerMessage greeting(ClientMessage message) throws Exception {
        // 模拟延时
        Thread.sleep(1000);
        return new ServerMessage("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
