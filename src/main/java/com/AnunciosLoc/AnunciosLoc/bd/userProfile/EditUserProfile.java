package com.AnunciosLoc.AnunciosLoc.bd.userProfile;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserProfile implements Serializable{
    
    private Long userId;
    private String chaveAntiga;
    private String novaChave; // Pode ser null ou igual à chaveAntiga se não quiser mudar
    private String novoValor; // Pode ser null se não quiser mudar

}
