package ar.com.matiabossio.mattmdb.data;

import ar.com.matiabossio.mattmdb.business.domain.Media;
import ar.com.matiabossio.mattmdb.business.domain.User;

import java.util.ArrayList;
import java.util.List;

public class DummyMedia {
    public static Media getMedia1(){

        Media media1 = new Media();

        media1.setMediaId(1);
        media1.setMediaType("movie");
        //media1.setFans(new ArrayList<User>(List.of(DummyUsers.getJazmin())));


        return media1;
    }

    public static Media getMedia2(){
        // Media media2 = new Media(0, "movie", new ArrayList<>());

        Media media2 = new Media();

        media2.setMediaId(2);
        media2.setMediaType("movie");

        return media2;
    }


    public static Media getMedia3(){
        Media media3 = new Media();

        media3.setMediaId(3);
        media3.setMediaType("movie");

        return media3;
    }

    public static Media getNewMedia(){
        Media newMedia = new Media();

        // newMedia.setMediaId(4);
        newMedia.setMediaType("movie");

        return newMedia;
    }



}
