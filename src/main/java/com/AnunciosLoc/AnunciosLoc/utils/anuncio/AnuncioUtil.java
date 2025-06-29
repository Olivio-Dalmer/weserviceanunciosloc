package com.AnunciosLoc.AnunciosLoc.utils.anuncio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.AnunciosLoc.AnunciosLoc.bd.anuncio.Anuncio;
import com.AnunciosLoc.AnunciosLoc.bd.condicaoPerfil.CondicaoPerfil;
import com.AnunciosLoc.AnunciosLoc.bd.local.Local;
import com.AnunciosLoc.AnunciosLoc.bd.user.User;
import com.AnunciosLoc.AnunciosLoc.bd.user.UserRepository;
import com.AnunciosLoc.AnunciosLoc.bd.userProfile.UserProfile;
import com.AnunciosLoc.AnunciosLoc.bd.userProfile.UserProfileRepository;

import xml.soap.anuncios.AnuncioType;
import xml.soap.anuncios.LocalType;
import xml.soap.anuncios.UserType;

@Component
public class AnuncioUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public boolean usuarioPertenceAWhiteList(Long userId, List<CondicaoPerfil> condicoes) {
        List<UserProfile> perfis = userProfileRepository.findByUserId(userId);

        for (CondicaoPerfil cond : condicoes) {
            String valor = cond.getValor();

            boolean encontrado = perfis.stream()
                    .anyMatch(p -> p.getValor().equalsIgnoreCase(valor));

            if (encontrado)
                return true;
        }

        return false;
    }

    /**
     * Verifica se o usuário está BLOQUEADO por corresponder a alguma condição da
     * blacklist.
     * Retorna TRUE se o usuário deve ser bloqueado.
     */
    public boolean usuarioEstaNaBlacklist(Long userId, List<CondicaoPerfil> condicoes) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            System.out.println("Usuário com ID " + userId + " não encontrado.");
            return false;
        }

        List<UserProfile> perfis = userProfileRepository.findByUserId(userId);

        if (perfis.isEmpty()) {
            System.out.println("Nenhum perfil encontrado para o usuário com ID: " + userId);
            return false; // Sem perfil → não bloqueia
        }

        for (CondicaoPerfil cond : condicoes) {
            String valor = cond.getValor();

            boolean encontrado = perfis.stream()
                    .anyMatch(p -> p.getValor().equalsIgnoreCase(valor));

            if (encontrado)
                return true; // Achou uma condição → usuário bloqueado
        }

        return false; // Não achou nenhuma → pode ver
    }

    public UserType mapUserToUserType(User user) {
        if (user == null)
            return null;

        UserType userType = new UserType();
        userType.setId(user.getId());
        userType.setUsername(user.getUsername());
        return userType;
    }

    public AnuncioType MapAnuncioType(Anuncio anuncio) {
        if (anuncio == null)
            return null;

        AnuncioType type = new AnuncioType();
        type.setId(Math.toIntExact(anuncio.getId()));
        type.setTitulo(anuncio.getTitulo());
        type.setDataInicio(anuncio.getDataInicio().toString());
        type.setDescricao(anuncio.getDescricao());
        type.setDataExpiracao(anuncio.getDataExpiracao().toString());

        // Se quiser incluir usuário e local também:
        if (anuncio.getUser() != null) {
            UserType userType = mapUserToUserType(anuncio.getUser());
            type.setUsuario(userType);
        }

        if (anuncio.getLocalizacao() != null) {
            LocalType localType = mapLocalToLocalType(anuncio.getLocalizacao());
            type.setLocal(localType);
        }

        return type;
    }

    public LocalType mapLocalToLocalType(Local local) {
        LocalType localType = new LocalType();
        localType.setId(local.getId());
        localType.setNome(local.getNome());
        localType.setLatitude(local.getLatitude());
        localType.setLongitude(local.getLongitude());
        return localType;
    }

}
