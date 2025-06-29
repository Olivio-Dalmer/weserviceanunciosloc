package com.AnunciosLoc.AnunciosLoc.controllers;

import com.AnunciosLoc.AnunciosLoc.services.LocalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import xml.soap.local.*;

@Endpoint
@Component
public class LocalEndPoint {

    private static final String NAMESPACE_URI = "http://local.soap.xml";

    @Autowired
private final LocalService localService;

    public LocalEndPoint(LocalService localService) {
        this.localService = localService;
    }

    // Adicionar Localização
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddLocalRequest")
    @ResponsePayload
    public AddLocalResponse adicionarLocal(@RequestPayload AddLocalRequest request) {        
        return this.localService.adicionarLocal(request);
    }

    // Listar Localizações
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AllLocalRequest")
    @ResponsePayload
    public AllLocalResponse listarTodosOsLocais(@RequestPayload AllLocalRequest request) {
        return this.localService.listarTodosOsLocais(request);
    }

    // Pegar um Local
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetLocalRequest")
    @ResponsePayload
    public GetLocalResponse pegarLocal(@RequestPayload GetLocalRequest request) {
        return this.localService.pegarLocal(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RemoveLocalRequest")
    @ResponsePayload
    public RemoveLocalResponse removerLocal(@RequestPayload RemoveLocalRequest request) {
        return this.localService.removerLocal(request);
    }
}
