package ua.lil.proxy.io;

import io.netty.buffer.ByteBuf;
import ua.lil.proxy.io.exception.BadPacketException;
import ua.lil.proxy.io.protocol.HandshakePacket;
import ua.lil.proxy.io.protocol.UserMessagePacket;

import java.util.HashMap;

public class PacketMapper {
    private static final HashMap<Short, Class<? extends AbstractPacket>> packets = new HashMap();

    public static AbstractPacket readPacket(ByteBuf buf) throws BadPacketException {
        short id = buf.readShort();
        Class<? extends AbstractPacket> clazz = packets.get(id);
        if (clazz == null)
            throw new BadPacketException("Bad packet ID: " + id);

        try {
            AbstractPacket packet = clazz.newInstance();
            packet.read(buf);
            return packet;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static void writePacket(AbstractPacket packet, ByteBuf buf) {
        buf.writeShort(packet.getId());
        packet.write(buf);
    }

    static {
        packets.put((short) 0, HandshakePacket.class);
        packets.put((short) 1, UserMessagePacket.class);
    }
}

