package io.github.tt432.scriptcore.handler.groovy;

import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * @author TT432
 */
@RequiredArgsConstructor
public class ResourceManagerConnector implements ResourceConnector {
    final ResourceManager resourceManager;

    @Override
    public URLConnection getResourceConnection(String name) throws ResourceException {
        try {
            // 创建一个URL对象，用于URLConnection
            URL url = new URL("myprotocol", "localhost", 0, name, new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) {
                    return new URLConnection(u) {
                        @Override
                        public void connect() {
                            // 不需要连接操作
                        }

                        @Override
                        public InputStream getInputStream() {
                            // 调用软件B提供的方法，通过ID获取InputStream
                            return resourceManager.getResource(new ResourceLocation(u.getFile()))
                                    .map(res -> {
                                        try {
                                            return res.open();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .orElse(InputStream.nullInputStream());
                        }
                    };
                }
            });

            return url.openConnection();
        } catch (Exception e) {
            throw new ResourceException("Failed to get resource: " + name, e);
        }
    }
}
