package com.dgm.board.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequestBody {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

}


