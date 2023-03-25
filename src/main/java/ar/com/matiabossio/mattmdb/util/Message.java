package ar.com.matiabossio.mattmdb.util;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor

public class Message {
    @NonNull
    private String title;
    @NonNull
    private String message;
    @NonNull
    private Integer statusCode;
    @NonNull
    private Boolean success;
    private Object data;
}
