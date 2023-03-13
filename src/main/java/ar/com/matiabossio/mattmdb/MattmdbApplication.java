package ar.com.matiabossio.mattmdb;

import ar.com.matiabossio.mattmdb.config.IConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MattmdbApplication implements CommandLineRunner {

    @Autowired
    private IConfig config;

    public static void main(String[] args) {

        SpringApplication.run(MattmdbApplication.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("**********************************************************************");
        System.out.println(String.format("*                %s Server listening on port %s :D                 *", config.getVersion(), config.getServerPort()));
        System.out.println(String.format("*  SERVER URL: http://localhost:%s%s    *", config.getServerPort(), config.getApplicationPath()));
        System.out.println("**********************************************************************");
    }
}
