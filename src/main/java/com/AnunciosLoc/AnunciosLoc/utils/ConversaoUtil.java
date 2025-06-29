package com.AnunciosLoc.AnunciosLoc.utils;

import org.springframework.stereotype.Component;

import com.AnunciosLoc.AnunciosLoc.bd.interesses.Interesses;
import com.AnunciosLoc.AnunciosLoc.bd.local.Local;
import com.AnunciosLoc.AnunciosLoc.bd.user.User;

import xml.soap.anuncios.LocalType; // Só um LocalType pode ser importado diretamente
import xml.soap.anuncios.UserType; // Só um UserType pode ser importado diretamente
import xml.soap.interesses.InteressesType;

@Component
public class ConversaoUtil {

    // Conversão para xml.soap.anuncios.LocalType
    public static LocalType toAnunciosLocalType(Local local) {
        if (local == null)
            return null;
        LocalType localType = new LocalType();
        localType.setId(local.getId());
        localType.setNome(local.getNome());
        localType.setLatitude(local.getLatitude());
        localType.setLongitude(local.getLongitude());
        return localType;
    }

    public InteressesType mapToInteressesType(Interesses interesse) {
        if (interesse == null)
            return null;

        InteressesType type = new InteressesType();
        type.setId(interesse.getId());
        type.setName(interesse.getName());
        return type;
    }

    // Conversão para xml.soap.local.LocalType
    public static xml.soap.local.LocalType toLocalLocalType(Local local) {
        if (local == null)
            return null;
        xml.soap.local.LocalType localType = new xml.soap.local.LocalType();
        localType.setId(local.getId());
        localType.setNome(local.getNome());
        localType.setLatitude(local.getLatitude());
        localType.setLongitude(local.getLongitude());
        return localType;
    }

    // Conversão para xml.soap.anuncios.UserType
    public static UserType toAnunciosUserType(User user) {
        if (user == null)
            return null;
        UserType userType = new UserType();
        userType.setId(user.getId());
        userType.setEmail(user.getEmail());
        userType.setUsername(user.getUsername());
        userType.setGenero(user.getGenero());
        userType.setTelefone(user.getTelefone());

        return userType;
    }

    // Conversão para xml.soap.user.UserType
    public static xml.soap.user.UserType toUserUserType(User user) {
        if (user == null)
            return null;
        xml.soap.user.UserType userType = new xml.soap.user.UserType();
        userType.setId(user.getId());
        userType.setEmail(user.getEmail());
        userType.setUsername(user.getUsername());
        userType.setGenero(user.getGenero());
        userType.setTelefone(user.getTelefone());
        return userType;
    }

}
