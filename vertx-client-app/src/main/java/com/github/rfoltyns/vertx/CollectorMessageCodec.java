package com.github.rfoltyns.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.Json;

public class CollectorMessageCodec implements MessageCodec<ClientMessage, Buffer> {

    @Override
    public void encodeToWire(Buffer buffer, ClientMessage clientMessage) {
        try {
            buffer.appendBytes(Json.mapper.writeValueAsBytes(clientMessage));
        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }
    }

    @Override
    public Buffer decodeFromWire(int pos, Buffer buffer) {
        return buffer;
    }

    @Override
    public Buffer transform(ClientMessage clientMessage) {
        Buffer buffer = Buffer.buffer();
        encodeToWire(buffer, clientMessage);
        return buffer;
    }

    @Override
    public String name() {
        return CollectorMessageCodec.class.getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
