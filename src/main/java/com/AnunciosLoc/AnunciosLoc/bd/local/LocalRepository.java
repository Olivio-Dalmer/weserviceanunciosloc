package com.AnunciosLoc.AnunciosLoc.bd.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalRepository extends  JpaRepository<Local, Long>{

}
