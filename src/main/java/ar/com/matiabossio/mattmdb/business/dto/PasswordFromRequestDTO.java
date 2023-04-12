package ar.com.matiabossio.mattmdb.business.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Password From Request", description = "Password received in the request from the client")

public class PasswordFromRequestDTO {

    @NotBlank(message = "Can't be blank")
    @NotNull(message = "Can't be null")
    @NotEmpty(message = "Can't be empty")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "Password must have more than 7 characters and have at least one uppercase, one lowercase and one number")
    private String password;

}
