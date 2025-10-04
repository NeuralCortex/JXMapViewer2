package org.jxmapviewer.pojo;

import java.net.Proxy;

/**
 *
 * @author Neural Cortex
 */
public class ProxyPOJO {

    private final Proxy.Type type;
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private boolean auth = false;

    public ProxyPOJO(Proxy.Type type, String host, int port, String user, String password) {
        this.type = type;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.auth = true;
    }

    public ProxyPOJO(Proxy.Type type, String host, int port) {
        this(type, host, port, null, null);
        this.auth = false;
    }

    public Proxy.Type getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuth() {
        return auth;
    }
}
