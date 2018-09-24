package com.cnblogs.yjmyzz.websocket.demo;

import com.cnblogs.yjmyzz.websocket.demo.consts.GlobalConsts;
import com.cnblogs.yjmyzz.websocket.demo.model.ServerMessage;
import com.cnblogs.yjmyzz.websocket.demo.model.ClientMessage;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @author junmingyang
 */
public class DemoWebSocketClient {

    public static final String SEND_URL = GlobalConsts.APP_PREFIX + GlobalConsts.HELLO_MAPPING;

    static public class MyStompSessionHandler extends StompSessionHandlerAdapter {

        private String name;

        public MyStompSessionHandler(String name) {
            this.name = name;
        }

        private void showHeaders(StompHeaders headers) {
            for (Map.Entry<String, List<String>> e : headers.entrySet()) {
                System.err.print("  " + e.getKey() + ": ");
                boolean first = true;
                for (String v : e.getValue()) {
                    if (!first) {
                        System.err.print(", ");
                    }
                    System.err.print(v);
                    first = false;
                }
                System.err.println();
            }
        }

        private void sendJsonMessage(StompSession session) {
            ClientMessage msg = new ClientMessage(name);
            session.send(SEND_URL, msg);
        }

        private void subscribeTopic(String topic, StompSession session) {
            session.subscribe(topic, new StompFrameHandler() {

                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return ServerMessage.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    System.err.println(payload.toString());
                }
            });
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.err.println("Connected! Headers:");
            showHeaders(connectedHeaders);

            subscribeTopic(GlobalConsts.TOPIC, session);
            sendJsonMessage(session);

            System.err.println("please input your new name:");
        }
    }

    public static void main(String[] args) throws Exception {
        WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        String url = "ws://localhost:8080" + GlobalConsts.ENDPOINT;
        String name = "spring-" + ThreadLocalRandom.current().nextInt(1, 99);
        StompSessionHandler sessionHandler = new MyStompSessionHandler(name);
        StompSession session = stompClient.connect(url, sessionHandler).get();

        //发送消息
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            System.out.print(name + " >> ");
            System.out.flush();
            String line = in.readLine();
            if (line == null) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }
            ClientMessage msg = new ClientMessage(name + ": I have a new name [" + line + "]");
            session.send(SEND_URL, msg);
        }
    }
}