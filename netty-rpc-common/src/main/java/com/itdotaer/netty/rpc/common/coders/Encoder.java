package com.itdotaer.netty.rpc.common.coders;

import com.itdotaer.netty.rpc.common.dtos.RpcRequest;
import com.itdotaer.netty.rpc.common.dtos.RpcResponse;
import com.itdotaer.netty.rpc.common.serialization.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.Serializable;

/**
 * Encoder
 *
 * @author jt_hu
 * @date 2020/7/24
 */
public class Encoder extends MessageToByteEncoder<Serializable> {

    /**
     * Encode a message into a {@link ByteBuf}. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        String clz = "";
        if (msg instanceof RpcRequest) {
            clz = RpcRequest.class.getName();
        } else if (msg instanceof RpcResponse) {
            clz = RpcResponse.class.getName();
        }

        out.writeInt(clz.length());

        byte[] content = SerializerManager.getSerializer(1).serialize(msg);
        out.writeInt(content.length);

        out.writeBytes(clz.getBytes());
        out.writeBytes(content);
    }

}
