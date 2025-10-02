package com.guxuede.gm.net.utils;

import com.guxuede.gm.net.client.registry.pack.PlayerPositionPack;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.junit.Test;

import static io.netty.buffer.Unpooled.wrappedBuffer;


public class PackageUtilsTest {

    @Test
    public void writeString() {
        ByteBuf byteBuf = wrappedBuffer(new byte[]{0, 0, 0, 3, -91, -122, -106, -124, 0, 0, 0, 1, 67, -45, -17, 35, 67, 89, 20 });
        int type = byteBuf.readInt();
        PlayerPositionPack positionPack = new PlayerPositionPack(byteBuf);

    }

    void readString() {
        ByteBuf byteBuf = io.netty.buffer.Unpooled.directBuffer(50);
        byteBuf.writeInt(3);
        byteBuf.writeInt(-1517157046);
        byteBuf.writeInt(1);
        byteBuf.writeFloat(397.31985f);
        byteBuf.writeFloat(145.57436f);

        ByteBufUtil.getBytes(byteBuf);
    }
}