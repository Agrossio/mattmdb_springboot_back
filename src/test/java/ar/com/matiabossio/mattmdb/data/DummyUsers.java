package ar.com.matiabossio.mattmdb.data;

import ar.com.matiabossio.mattmdb.business.domain.User;

public class DummyUsers {

    public static User getMatias(){
        User matias = new User();

        // matias.setUserId(1);
        matias.setUsername("Matias");
        matias.setEmail("matias@mail.com");
        matias.setPassword("123456Aa");

        return matias;

    }

    public static User getMariangeles(){
        User mariangeles = new User();

        // mariangeles.setUserId(2);
        mariangeles.setUsername("Mariangeles");
        mariangeles.setEmail("mariangeles@mail.com");
        mariangeles.setPassword("123456Aa");

        return mariangeles;

    }

    public static User getJazmin(){
        User jazmin = new User();

        // jazmin.setUserId(3);
        jazmin.setUsername("Jazmin");
        jazmin.setEmail("jazmin@mail.com");
        jazmin.setPassword("123456Aa");

        return jazmin;

    }


}
