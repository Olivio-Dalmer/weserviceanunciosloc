package com.AnunciosLoc.AnunciosLoc.seeders;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.AnunciosLoc.AnunciosLoc.bd.interesses.Interesses;
import com.AnunciosLoc.AnunciosLoc.bd.interesses.InteressesRepository;

@Component
public class InteresseSeeder {

    private final InteressesRepository interessesRepository;

    public InteresseSeeder(InteressesRepository interessesRepository) {
        this.interessesRepository = interessesRepository;
    }

    public void seed() {
        if (interessesRepository.count() == 0) {
            List<String> interesses = Arrays.asList(
                    "Desporto",
                    "Ciências",
                    "Engenharia",
                    "Móveis",
                    "Imóvel",
                    "Automóvel",
                    "Tecnologias",
                    "Gastronomia",
                    "Política",
                    "Beleza",
                    "Arte",
                    "Cultura",
                    "Moda");

            for (String nome : interesses) {
                Interesses interesse = new Interesses();
                interesse.setName(nome);
                interessesRepository.save(interesse);
            }

            System.out.println("✔ InteressesSeeder: " + interesses.size() + " interesses inseridos.");
        } else {
            System.out.println("ℹ InteressesSeeder: Tabela já contém dados.");
        }
    }

}
