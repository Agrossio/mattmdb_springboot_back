package ar.com.matiabossio.mattmdb;

import ar.com.matiabossio.mattmdb.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MattmdbApplication implements CommandLineRunner {

    @Autowired
    private Config serverPort;

    public static void main(String[] args) {

        SpringApplication.run(MattmdbApplication.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("***********************************************");
        System.out.println("*       Server listening on port " + serverPort + " :D      *");
        System.out.println("***********************************************");
    }
}
