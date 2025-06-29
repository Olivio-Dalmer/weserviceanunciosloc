package com.AnunciosLoc.AnunciosLoc.bd.politicaEntrega;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliticaEntregaRepository extends JpaRepository<PoliticaEntrega, Long> {
}
