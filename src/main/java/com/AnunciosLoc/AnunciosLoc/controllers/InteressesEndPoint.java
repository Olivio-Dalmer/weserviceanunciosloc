package com.AnunciosLoc.AnunciosLoc.controllers;

import com.AnunciosLoc.AnunciosLoc.services.InteressesService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import xml.soap.interesses.*;

@Endpoint
@Component
public class InteressesEndPoint{

    private static final String NAMESPACE_URI = "http://interesses.soap.xml";

    @Autowired
    private final InteressesService interessesService;

    public InteressesEndPoint(InteressesService interessesService) {
        this.interessesService = interessesService;
    }
        private List<InteressesType> interesses = new ArrayList<>();

        public List<InteressesType> getInteresses() {
            return interesses;
        }
        
    // Listar Interesses
        @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AllInteressesRequest") // Corrigido 'localPart'
        @ResponsePayload
        public AllInteressesResponse listarTodosOsInteresses(@RequestPayload AllInteressesRequest request) {
            
            return interessesService.listarTodosOsInteresses(request);
        }


}
