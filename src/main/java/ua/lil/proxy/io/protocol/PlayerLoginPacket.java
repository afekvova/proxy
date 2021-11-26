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
public class PlayerLoginPacket extends AbstractPacket {
    
    public String name, cancelReason;
    public boolean allowed;

    public PlayerLoginPacket() {
        super((short) 100);
    }

    @Override
    protected void read(ByteBuf buf) {
        this.name = PlayerLoginPacket.readString(buf);
        this.allowed = buf.readBoolean();
        this.cancelReason = PlayerLoginPacket.readString(buf);
    }

    @Override
    protected void write(ByteBuf buf) {
        PlayerLoginPacket.writeString(buf, this.name);
        buf.writeBoolean(this.allowed);
        PlayerLoginPacket.writeString(buf, this.cancelReason);
    }

    public String toString() {
        return "PlayerLoginPacket(name=" + this.name + ", allowed=" + this.allowed + ", cancelReason=" + this.cancelReason + ")";
    }
}

