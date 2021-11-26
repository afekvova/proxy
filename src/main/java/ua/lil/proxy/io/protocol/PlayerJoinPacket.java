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
public class PlayerJoinPacket extends AbstractPacket {

    String name, ip, server;

    public PlayerJoinPacket() {
        super((short) 101);
    }

    @Override
    protected void read(ByteBuf buf) {
        this.name = PlayerJoinPacket.readString(buf);
        this.ip = PlayerJoinPacket.readString(buf);
        this.server = PlayerJoinPacket.readString(buf);
    }

    @Override
    protected void write(ByteBuf buf) {
        PlayerJoinPacket.writeString(buf, this.name);
        PlayerJoinPacket.writeString(buf, this.ip);
        PlayerJoinPacket.writeString(buf, this.server);
    }

    public String toString() {
        return "PlayerJoinPacket(name=" + this.name + ", ip=" + this.ip + ", server=" + this.server + ")";
    }
}

