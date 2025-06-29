package com.AnunciosLoc.AnunciosLoc.bd.userProfile;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUserProfileResponse {
    private boolean status;
    private String mensagem;
    private List<ParChaveValorDTO> profile;

    
}