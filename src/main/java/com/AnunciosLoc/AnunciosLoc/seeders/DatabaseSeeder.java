package com.AnunciosLoc.AnunciosLoc.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final InteresseSeeder interesseSeeder;

    public DatabaseSeeder(InteresseSeeder interesseSeeder) {
        this.interesseSeeder = interesseSeeder;
    }

    @Override
    public void run(String... args) {
        interesseSeeder.seed();
    }
}


