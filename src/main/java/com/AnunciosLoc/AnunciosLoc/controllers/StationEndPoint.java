package com.AnunciosLoc.AnunciosLoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.AnunciosLoc.AnunciosLoc.services.StationService;
import xml.soap.station.*;

@Endpoint
@Component
public class StationEndPoint {

    private static final String NAMESPACE_URI = "http://station.soap.xml";

    @Autowired(required = true)
    private StationService stationService;

    @PayloadRoot(namespace= NAMESPACE_URI, localPart = "TestStationRequest")
	@ResponsePayload
    public TestStationResponse testStation (@RequestPayload TestStationRequest request) {
        System.out.println("Entrando no serviço de checkin de estação");
		return stationService.testStation(request);
    }

    @PayloadRoot(namespace= NAMESPACE_URI, localPart = "GetStationDetailsRequest")
	@ResponsePayload
    public GetStationDetailsResponse getStation (@RequestPayload GetStationDetailsRequest request) {
        System.out.println("Entrando no serviço get Station");
		return stationService.getStation(request);
    }

    @PayloadRoot(namespace= NAMESPACE_URI, localPart = "AddStationRequest")
	@ResponsePayload
    public StationResponse addStation (@RequestPayload AddStationRequest request) {
        System.out.println("Entrando no serviço add Station");
		return stationService.addStation(request);
    }

    @PayloadRoot(namespace= NAMESPACE_URI, localPart = "AllStationRequest")
    @ResponsePayload
    public AllStationResponse getAllStations (@RequestPayload AllStationRequest request) {
        System.out.println("Entrando no serviço all Station");
        return stationService.getAllStations(request);
    }

    @PayloadRoot(namespace= NAMESPACE_URI, localPart = "AllStationMoreProximeRequest")
    @ResponsePayload
    public AllStationResponse getAllStationsMoreProxime (@RequestPayload AllStationMoreProximeRequest request) {
        System.out.println("Entrando no serviço all Station");
        return stationService.getAllStationMoreProxime(request);
    }

}
