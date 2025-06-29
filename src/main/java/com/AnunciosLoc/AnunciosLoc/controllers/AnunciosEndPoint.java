package com.AnunciosLoc.AnunciosLoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.AnunciosLoc.AnunciosLoc.services.AnunciosService;
import xml.soap.anuncios.*;

@Endpoint
@Component
public class AnunciosEndPoint {
    private static final String NAMESPACE_URI = "http://anuncios.soap.xml";

    @Autowired
    private final AnunciosService anunciosService;

    public AnunciosEndPoint(AnunciosService anunciosService) {
        this.anunciosService = anunciosService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddAnuncioRequest")
    @ResponsePayload
    public AddAnuncioResponse addAnuncio(@RequestPayload AddAnuncioRequest request) {
        return this.anunciosService.addAnuncio(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AllAnuncioRequest")
    @ResponsePayload
    public AllAnuncioResponse getAllAnuncios(@RequestPayload AllAnuncioRequest request) {
        return this.anunciosService.getAllAnuncios(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AllAnuncioTodosRequest")
    @ResponsePayload
    public AllAnuncioResponse getAllAnunciosTodos(@RequestPayload AllAnuncioTodosRequest request) {
        return this.anunciosService.getAllAnunciosTodos(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RemoveAnuncioRequest")
    @ResponsePayload
    public RemoveAnuncioResponse removeAnuncio(@RequestPayload RemoveAnuncioRequest request) {

        return this.anunciosService.removeAnuncio(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListarAnuncioCriadosRequest")
    @ResponsePayload
    public ListarAnuncioCriadosResponse ListarAnuncioCriados(@RequestPayload ListarAnuncioCriadosRequest request) {
        return this.anunciosService.ListarAnuncioCriados(request);
    }

}
