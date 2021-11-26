package ua.lil.proxy.io;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractPacket {

    @Getter
    private final short id;

    protected abstract void read(ByteBuf byteBuf);

    protected abstract void write(ByteBuf byteBuf);

    public static String readString(ByteBuf buf) {
        short length = buf.readShort();
        if (length == -1)
            return null;

        byte[] b = new byte[length];
        buf.readBytes(b);
        return new String(b);
    }

    public static void writeString(ByteBuf buf, String string) {
        if (string != null) {
            byte[] b = string.getBytes();
            buf.writeShort(b.length);
            buf.writeBytes(b);
            return;
        }

        buf.writeShort(-1);
    }

    public static String[] readStringArray(ByteBuf buf) {
        int length = buf.readShort();
        if (length == -1)
            return null;

        String[] array = new String[length];
        for (int i = 0; i < length; ++i)
            array[i] = AbstractPacket.readString(buf);

        return array;
    }

    public static void writeStringArray(ByteBuf buf, String[] array) {
        if (array != null) {
            buf.writeShort(array.length);
            for (int i = 0; i < array.length; ++i)
                AbstractPacket.writeString(buf, array[i]);
            return;
        }

        buf.writeShort(-1);
    }

    public static int[] readIntArray(ByteBuf buf) {
        int length = buf.readShort();
        if (length == -1)
            return null;

        int[] array = new int[length];
        for (int i = 0; i < length; ++i)
            array[i] = buf.readInt();

        return array;
    }

    public static void writeIntArray(ByteBuf buf, int[] array) {
        if (array != null) {
            buf.writeShort(array.length);
            for (int i = 0; i < array.length; ++i)
                buf.writeInt(array[i]);
            return;
        }

        buf.writeShort(-1);
    }
}

