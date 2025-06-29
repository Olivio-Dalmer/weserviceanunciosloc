package com.AnunciosLoc.AnunciosLoc.utils.conta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.AnunciosLoc.AnunciosLoc.bd.user.*;

import xml.soap.conta.UsuarioConta;


@Component
public class ContaUtil {
     
    public UsuarioConta UsuarioDaConta(User user) {
        if (user == null)
            return null;

        UsuarioConta UsuarioConta = new UsuarioConta();
        UsuarioConta.setId(user.getId());
        UsuarioConta.setUsername(user.getUsername());
        UsuarioConta.setEmail(user.getEmail());
        UsuarioConta.setGenero(user.getGenero());
        UsuarioConta.setDatanascimento(user.getDatanascimento());
        UsuarioConta.setTelefone(user.getTelefone());
        
        return UsuarioConta;
    }
    

}
