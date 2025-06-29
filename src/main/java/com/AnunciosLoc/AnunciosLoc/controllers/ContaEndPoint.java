package com.AnunciosLoc.AnunciosLoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import com.AnunciosLoc.AnunciosLoc.services.ContaService;

import xml.soap.conta.ConsultarSaldoRequest;
import xml.soap.conta.ConsultarSaldoResponse;


@Endpoint
public class ContaEndPoint {       

    private static final String NAMESPACE_URI = "http://conta.soap.xml";

    @Autowired
    private final ContaService contaService;

    public ContaEndPoint(ContaService contaService) {
        this.contaService = contaService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarSaldoRequest")
    @ResponsePayload
    public ConsultarSaldoResponse consultarSaldo(@RequestPayload ConsultarSaldoRequest request) {
        return this.contaService.consultarSaldo(request);
    }
    
}
