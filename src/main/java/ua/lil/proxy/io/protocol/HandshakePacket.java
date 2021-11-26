package ua.lil.proxy.io.protocol;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ua.lil.proxy.io.AbstractPacket;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandshakePacket extends AbstractPacket {

    String name;
    byte type;
    int port;
    boolean allowed;

    public HandshakePacket() {
        super((short) 0);
    }

    @Override
    protected void read(ByteBuf buf) {
        this.name = HandshakePacket.readString(buf);
        this.type = buf.readByte();
        this.port = buf.readInt();
        this.allowed = buf.readBoolean();
    }

    @Override
    protected void write(ByteBuf buf) {
        HandshakePacket.writeString(buf, this.name);
        buf.writeByte(this.type);
        buf.writeInt(this.port);
        buf.writeBoolean(this.allowed);
    }

    public String toString() {
        return "HandshakePacket(name=" + this.name + ", type=" + this.type + ", port=" + this.port + ", allowed=" + this.allowed + ")";
    }
}

