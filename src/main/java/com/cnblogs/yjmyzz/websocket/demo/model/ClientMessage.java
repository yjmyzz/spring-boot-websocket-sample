package com.cnblogs.yjmyzz.websocket.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author junmingyang
 */
@Data
@AllArgsConstructor
public class ClientMessage {

    private String name;

    public ClientMessage() {
    }

}
