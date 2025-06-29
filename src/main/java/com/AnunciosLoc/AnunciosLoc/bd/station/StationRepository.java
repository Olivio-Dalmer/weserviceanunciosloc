package com.AnunciosLoc.AnunciosLoc.bd.station;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<StationModel, Long> {

    StationModel findByEndpoint (String endpoint);
    StationModel findByName(String name);
    boolean existsByName(String name);
    boolean existsByEndpoint(String enpoint);

}
