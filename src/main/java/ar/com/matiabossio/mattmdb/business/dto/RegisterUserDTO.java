package ar.com.matiabossio.mattmdb.business.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Request User", description = "User received in the request from the client")

public class RegisterUserDTO {
    private Integer userId;
    @NotBlank(message = "Can't be blank")
    @NotNull(message = "Can't be null")
    @NotEmpty(message = "Can't be empty")
    private String username;
    @NotBlank(message = "Can't be blank")
    @NotNull(message = "Can't be null")
    @NotEmpty(message = "Can't be empty")
    @Email(message = "Must be a valid email")
    private String email;
    @NotBlank(message = "Can't be blank")
    @NotNull(message = "Can't be null")
    @NotEmpty(message = "Can't be empty")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "Your password must have more than 7 characters and have at least one uppercase, one lowercase and one number")
    private String password;

}
