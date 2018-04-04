package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.Json;

import java.io.IOException;

public class ServerMessageProducerCodec implements MessageCodec<ServerMessage, Buffer> {

    @Override
    public void encodeToWire(Buffer buffer, ServerMessage serverMessage) {
        try {
            buffer.appendBytes(JsonUtils.mapper.writeValueAsBytes(serverMessage));
        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }
    }

    @Override
    public Buffer decodeFromWire(int pos, Buffer buffer) {
        return buffer;
    }

    @Override
    public Buffer transform(ServerMessage serverMessage) {
        Buffer buffer = Buffer.buffer();
        encodeToWire(buffer, serverMessage);
        return buffer;
    }

    @Override
    public String name() {
        return ServerMessageProducerCodec.class.getName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
