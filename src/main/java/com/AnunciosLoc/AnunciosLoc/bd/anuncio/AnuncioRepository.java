package com.AnunciosLoc.AnunciosLoc.bd.anuncio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnuncioRepository extends  JpaRepository<Anuncio, Long>{
 @Query("SELECT a FROM Anuncio a " +
                  "LEFT JOIN FETCH a.politicaEntrega p " +
                  "LEFT JOIN FETCH p.condicoes " +
                  "WHERE a.user.id = :userId")
      List<Anuncio> findByUserIdWithRelations(@Param("userId") Long userId);

      List<Anuncio> findByUserId(Long userId);

}
