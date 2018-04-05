package com.github.rfoltyns.vertx;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.vertx.core.Vertx;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocalhostNameResolver extends NameResolver.Factory {

    private final String mapName;
    private final Vertx vertx;
    private final String serviceName;

    public LocalhostNameResolver(Vertx vertx, String clusterWideMapName, String serviceName) {
        this.vertx = vertx;
        this.mapName = clusterWideMapName;
        this.serviceName = serviceName;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        return new NameResolver() {
            List<EquivalentAddressGroup> servers = new ArrayList<>();
            {
                vertx.sharedData().getClusterWideMap(mapName, event ->
                    event.result().get(serviceName, ipaddresses ->
                            ((Set< URL>)ipaddresses.result()).stream().forEach(url -> {
                                List<SocketAddress> socketAddresses = new ArrayList<>();
                                socketAddresses.add(new InetSocketAddress(url.getHost(), url.getPort()));
                                EquivalentAddressGroup equivalentAddressGroup = new EquivalentAddressGroup(socketAddresses);
                                servers.add(equivalentAddressGroup);
                            }))
                );

            }
            @Override
            public String getServiceAuthority() {
                return "localhost";
            }

            @Override
            public void start(Listener listener) {
                System.out.println("Resolved servers: " + servers);
                listener.onAddresses(servers, Attributes.newBuilder().build());
            }

            @Override
            public void shutdown() {
            }
        };
    }

    @Override
    public String getDefaultScheme() {
        return "http";
    }

}
