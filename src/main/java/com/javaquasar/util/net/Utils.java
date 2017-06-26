package com.javaquasar.util.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void main(String[] args) {

    }

    private static void installProxySelector(final String hostName, final int portNum) {
        ProxySelector.setDefault(new ProxySelector() {
            public List select(URI uri) {
                List list = new ArrayList();
                list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, portNum)));
                return list;
            }

            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
            }
        });
    }

    private Proxy findProxy(URI uri) {
        try {
            ProxySelector selector = ProxySelector.getDefault();
            List<Proxy> proxyList = selector.select(uri);
            if (proxyList.size() > 1) {
                return proxyList.get(0);
            }
        } catch (IllegalArgumentException e) {
        }
        return Proxy.NO_PROXY;
    }
}
