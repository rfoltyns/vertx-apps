package com.github.rfoltyns.vertx;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.Json;

import java.io.IOException;

public class ServerMessageConsumerCodec implements MessageCodec<Buffer, ServerMessage> {

    @Override
    public void encodeToWire(Buffer buffer, Buffer buffer2) {
       buffer.appendBuffer(buffer2);
    }

    @Override
    public ServerMessage decodeFromWire(int pos, Buffer buffer) {
        return JsonUtils.decodeValue(buffer, ServerMessage.class);
    }

    @Override
    public ServerMessage transform(Buffer buffer) {
        return decodeFromWire(0, buffer);
    }

    @Override
    public String name() {
        return ServerMessageConsumerCodec.class.getName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
