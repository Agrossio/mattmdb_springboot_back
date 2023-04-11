package ar.com.matiabossio.mattmdb;

import ar.com.matiabossio.mattmdb.config.IConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MattmdbApplication implements CommandLineRunner {

    private final IConfig config;

    public MattmdbApplication(IConfig config) {
        this.config = config;
    }

    public static void main(String[] args) {

        SpringApplication.run(MattmdbApplication.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("****************************************************************************************");
        System.out.printf("*                         %s Server listening on port %s :D                          *%n", config.getVersion(), config.getServerPort());
        System.out.printf("*            SERVER URL: https://localhost:%s%s            *%n", config.getServerPort(), config.getApplicationPath());
        System.out.printf("*  API Documentation: https://localhost:%s/mattmdb-1.0-SNAPSHOT/api/v2/swagger-ui/#/  *%n", config.getServerPort());
        System.out.println("****************************************************************************************");
    }
}
