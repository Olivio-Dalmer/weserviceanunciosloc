package com.AnunciosLoc.AnunciosLoc.bd.condicaoPerfil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CondicaoPerfilRepository extends JpaRepository<CondicaoPerfil, Long> {
}
