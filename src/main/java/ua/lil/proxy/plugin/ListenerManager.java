package ua.lil.proxy.plugin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import ua.lil.proxy.connection.Connection;
import ua.lil.proxy.helpers.LogHelper;
import ua.lil.proxy.io.AbstractPacket;

import java.util.Collection;
import java.util.Iterator;

public class ListenerManager {

    private final Multimap<Class<? extends AbstractPacket>, Listener> listeners = Multimaps.synchronizedMultimap(HashMultimap.create());

    public Listener registerListener(Class<? extends AbstractPacket> packetClass, Listener listener) {
        this.listeners.put(packetClass, listener);
        LogHelper.info("New listener for packet " + packetClass.getSimpleName() + " registed!");
        return listener;
    }

    public void unregisterListener(Class<? extends AbstractPacket> packetClass, Listener listener) {
        if (this.listeners.remove(packetClass, listener)) {
            LogHelper.info("Listener for packet " + packetClass.getSimpleName() + " unregisted!");
        } else {
            LogHelper.info("Cannot find listener for " + packetClass.getSimpleName() + " packet!");
        }
    }

    public void unregisterListeners(Class<? extends AbstractPacket> packetClass) {
        this.listeners.removeAll(packetClass);
        LogHelper.info("All listeners for " + packetClass.getSimpleName() + " packet unregisted!");
    }
    
    public void handleListeners(Connection connection, AbstractPacket packet) {
        Collection<Listener> listeners = this.listeners.get(packet.getClass());
        if (listeners != null) {
            synchronized (this.listeners) {
                Iterator<Listener> iterator = listeners.iterator();
                while (iterator.hasNext())
                    iterator.next().handle(connection, packet);
            }
        }
    }
}

