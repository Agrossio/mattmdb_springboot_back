package ar.com.matiabossio.mattmdb.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//@ToString(onlyExplicitlyIncluded = true)
public class Config {
    @Value("${server.port}")
    private String serverPort;

    public Config() {
    }


    public String getServerPort() {
        return serverPort;
    }

    @Override
    public String toString() {
        return serverPort;
    }
}
