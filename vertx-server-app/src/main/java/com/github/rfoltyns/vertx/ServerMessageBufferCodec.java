package com.github.rfoltyns.vertx;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class ServerMessageBufferCodec implements MessageCodec<Buffer, Buffer> {
    @Override
    public void encodeToWire(Buffer buffer, Buffer buffer2) {
        buffer.writeToBuffer(buffer2);
    }

    @Override
    public Buffer decodeFromWire(int pos, Buffer buffer) {
        return Buffer.buffer(buffer.getByteBuf());
    }

    @Override
    public Buffer transform(Buffer buffer) {
        return Buffer.buffer(buffer.getBytes());
    }

    @Override
    public String name() {
        return ServerMessageBufferCodec.class.getName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
