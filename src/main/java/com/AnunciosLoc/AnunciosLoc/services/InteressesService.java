package com.AnunciosLoc.AnunciosLoc.services;

import java.util.List;

import com.AnunciosLoc.AnunciosLoc.bd.interesses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AnunciosLoc.AnunciosLoc.bd.interesses.InteressesRepository;
import com.AnunciosLoc.AnunciosLoc.utils.ConversaoUtil;

import xml.soap.interesses.AllInteressesRequest;
import xml.soap.interesses.AllInteressesResponse;


@Service
public class InteressesService {

    @Autowired
    private InteressesRepository interessesRepository;

    @Autowired
    private ConversaoUtil conversaoUtil;

    public InteressesService(InteressesRepository interessesRepository, ConversaoUtil conversaoUtil){
        this.interessesRepository = interessesRepository;
        this.conversaoUtil        = conversaoUtil;
    }

    public AllInteressesResponse listarTodosOsInteresses(AllInteressesRequest request) {
        AllInteressesResponse response = new AllInteressesResponse();

        try {
            // Buscar todos os interesses no banco
            List<Interesses> interesses = interessesRepository.findAll();

            // Se não houver nenhum interesse
            if (interesses.isEmpty()) {
                response.setStatus(false);
                response.setMensagem("Nenhum interesse encontrado.");
                return response;
            }

            // Mapear cada interesse para o tipo SOAP
            for (Interesses interesse : interesses) {
                response.getInteresses().add(conversaoUtil.mapToInteressesType(interesse));
            }

            // Resposta com sucesso
            response.setStatus(true);
            response.setMensagem("Interesses listados com sucesso.");

        } catch (Exception e) {
            response.setStatus(false);
            response.setMensagem("Erro interno ao listar interesses: " + e.getMessage());
        }

        return response;
    }
}