package com.itdotaer.netty.rpc.common.coders;

import com.itdotaer.netty.rpc.common.serialization.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Decoder
 *
 * @author jt_hu
 * @date 2020/7/24
 */
public class Decoder extends ByteToMessageDecoder {

    /**
     * Decode the from one {@link ByteBuf} to an other. This method will be called till either the input
     * {@link ByteBuf} has nothing to read when return from this method or till nothing was read from the input
     * {@link ByteBuf}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int classLength = in.readInt();
        int contentLength = in.readInt();

        byte[] clz = new byte[classLength];
        in.readBytes(clz);
        byte[] content = new byte[contentLength];
        in.readBytes(content);

        Object obj = SerializerManager.getSerializer(1).deserialize(content, clz.toString());

        out.add(obj);
    }

}
