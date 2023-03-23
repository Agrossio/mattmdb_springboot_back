package ar.com.matiabossio.mattmdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Config implements IConfig {
    @Value("${app.version}")
    private String version;
    @Value("${server.port}")
    private String serverPort;
    @Value("${server.servlet.context-path}")
    private String applicationPath;

/*    public Config() {
    }*/

    public String getVersion() {
        return version;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getApplicationPath() {
        return applicationPath;
    }
}
