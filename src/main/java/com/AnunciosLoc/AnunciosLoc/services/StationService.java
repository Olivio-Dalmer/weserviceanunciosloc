package com.AnunciosLoc.AnunciosLoc.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AnunciosLoc.AnunciosLoc.bd.station.StationRepository;
import com.AnunciosLoc.AnunciosLoc.utils.Coordinates;
import com.AnunciosLoc.AnunciosLoc.utils.GeoLocationDTO;
import com.AnunciosLoc.AnunciosLoc.utils.Utils;
import com.AnunciosLoc.AnunciosLoc.wsAsCliente.ofJOpenCage.ServiceGeoLocation;
import com.AnunciosLoc.AnunciosLoc.wsAsCliente.ofJuddi.JuddiService;
//import com.AnunciosLoc.AnunciosLoc.wsAsCliente.ofReplica.WSReplica;
import com.AnunciosLoc.AnunciosLoc.wsAsCliente.ofStation.WSstation;
import com.AnunciosLoc.AnunciosLoc.bd.station.StationModel;
import xml.soap.station.*;
import xml.soap.GetStationResponse;

@Service
public class StationService {

    @Autowired
    private WSstation stationClient;
    @Autowired
    private AuthenticationService auth;
    @Autowired
    private Utils utils;
    @Autowired(required = true)
    private StationRepository stationRepo;
    @Autowired(required = true)
    private ServiceGeoLocation geoLocationService;
    @Autowired
    private JuddiService juddiService;
    //@Autowired(required = true)
    //private WSReplica serverWriter;

    public TestStationResponse testStation (TestStationRequest request) {

        System.out.println("Dentro do Serviço de checkin de estação: " + request.getBody().getId());
        TestStationResponse response = new TestStationResponse();
        if (!auth.sessionIsValid(request.getHeader().getAuthToken())) {
            //this.DestilaHeadResponse(session);
            response.setEstado(false);
            response.setMensagem("Token inválido, undefined!");
            response.setStateCode(401);
        }else {
            Optional<StationModel> station = stationRepo.findById(request.getBody().getId());
            System.out.println(578698797);
            if(station.isPresent()) {
                try{
                    GetStationResponse gsr = stationClient.getStationState(station.get().getEndpoint());
                    if (gsr != null) {
                        station.get().setState(1);
                        stationRepo.save(station.get());
                        response.setEstado(true);
                        response.setMensagem("Estação activa!!!");
                    } else {
                        station.get().setState(0);
                        stationRepo.save(station.get()); 
                        response.setEstado(false);
                        response.setMensagem("Estação inactiva!!!");
                    }
                } catch (Exception e) { // Catch any unexpected exceptions
                    station.get().setState(0);
                    stationRepo.save(station.get());
                    //serverWriter.writeInReplica("station", stationRepo.save(station.get()).returnString());
                    response.setEstado(false);
                    response.setMensagem("Estação inactiva!!!");
                }
            } else {
                response.setEstado(false);
                response.setMensagem("Estação não foi encontrada");
            }
        }
        return response;
    }

    public StationResponse addStation (AddStationRequest request) {
        System.out.println("Dentro do Serviço de registro de novas estações!!! ");
        StationResponse response = new StationResponse();
        GetStationResponse gsr = null;
        StationModel station = null;
        boolean pmr = false; //esta variável confirma que pelo menos uma estação foi encontrada no uddi e registrada
        List<String> servicos = juddiService.searchStationService();
        if (servicos != null) {
            for(String servico : servicos){
                try{
                    gsr = stationClient.getStationState(servico);
                    if (gsr != null) {
                        response.setEstado(true);
                        System.out.println(gsr.getName());
                        if (stationRepo.existsByEndpoint(servico)) {
                            station = stationRepo.findByEndpoint(servico);
                        } else {
                            pmr = true;
                            System.out.println("ok2x");
                            station = new StationModel();
                            station.setEndpoint(servico);
                        }
                        station.setBonus(gsr.getBonus());
                        station.setLatitude(gsr.getLatitude());
                        station.setLongitude(gsr.getLongitude());
                        station.setLocalName(gsr.getLocalName());
                        station.setName(gsr.getName());
                        station.setQtdDocks(gsr.getDockItem().size());
                        station.setState(1);
                        int qtdDispo = 0;
                        for (xml.soap.DockType dock : gsr.getDockItem()) {
                            if (dock.getState() == 0) qtdDispo++;
                        }
                        station.setQtdDocksDispo(qtdDispo);
                        System.out.println("abra");
                        try{
                            station = stationRepo.save(station);
                            //serverWriter.writeInReplica("station", station.returnString());
                        } catch (Exception exc) {
                            System.out.println("Não deu para salvar o registro!!!");
                        }
                        System.out.println("cadabra");
                    } else {
                        System.out.println("Estação inactiva!!!");
                    }
                } catch (Exception e) {
                    System.out.println("Estação inactiva!!!");
                }
            }
            if (pmr) {
                response.setMensagem("Novas estações foram encontradas e adicionadas!!!");
            } else {
                response.setMensagem("Estações foram encontradas!!!");
            }
        } else {
            response.setMensagem("Serviços de estações indisponíveis");
        }
        return response;
    }

    public GetStationDetailsResponse getStation (GetStationDetailsRequest request) {

        System.out.println("Dentro do Serviço e ID da estação: " + request.getBody().getId());
        GetStationDetailsResponse response = new GetStationDetailsResponse();

        if (!auth.sessionIsValid(request.getHeader().getAuthToken())) {
            //this.DestilaHeadResponse(session);
            response.setEstado(false);
            response.setMensagem("Token inválido, undefined!");
            response.setStateCode(401);
        }else {
            Optional<StationModel> stationModel = stationRepo.findById(request.getBody().getId());
            System.out.println(5786);
            if(stationModel.isPresent()) {

                GetStationResponse gsr = stationClient.getStationState(stationModel.get().getEndpoint());

                if (gsr != null) {

                    int qtdDocks = gsr.getDockItem().size();
                    int qtdDocksDispo = 0;
                    for (xml.soap.DockType dock : gsr.getDockItem()) {
                        if (dock.getState()==0) qtdDocksDispo++;
                    }
                    stationModel.get().setQtdDocks(qtdDocks);
                    stationModel.get().setQtdDocksDispo(qtdDocksDispo);
                    stationRepo.save(stationModel.get());
                    //serverWriter.writeInReplica("station", stationRepo.save(stationModel.get()).returnString());

                    BeanUtils.copyProperties(gsr, response);
                    response.setEstado(true);
                    response.setMensagem("Station encontrada com sucesso!!!");

                    gsr.getDockItem().forEach( dock -> {
                        DockType d = new DockType();
                        d.setState(dock.getState());
                        d.setIdDock(dock.getIdDock());
                        d.setReference(dock.getReference());
                        response.getDockItem().add(d);
                    });

                    GeoLocationDTO geoLocation = geoLocationService.getGeoLocation(String.valueOf(stationModel.get().getLatitude()), String.valueOf(stationModel.get().getLongitude()));

                    if(geoLocation!=null){
            
                        System.out.println("País: "+geoLocation.getPais());
                        System.out.println("Província: "+geoLocation.getProvincia());
                        System.out.println("Município: "+geoLocation.getMunicipio());
                        System.out.println("Distrito: "+geoLocation.getDistrito());
                        System.out.println("Avenida: "+geoLocation.getAvenida());
        
                        response.setPais(geoLocation.getPais());
                        response.setProvincia(geoLocation.getProvincia());
                        response.setMunicipio(geoLocation.getMunicipio());
                        response.setDistrito(geoLocation.getDistrito());
                        response.setAvenida(geoLocation.getAvenida());

                    }

                    return response;
                }
                response.setEstado(false);
                response.setMensagem("Station data corrupted!!!");
                return response;
            }
            else {
                response.setEstado(false);
                response.setMensagem("Estação não foi encontrada");
            }
        }
        return response;
    }

    public AllStationResponse getAllStations (AllStationRequest request) {
        System.out.println(request.getHeader().getAuthToken());
        AllStationResponse response = new AllStationResponse();
        if (!auth.sessionIsValid(request.getHeader().getAuthToken())) {
            //this.DestilaHeadResponse(session);
            response.setEstado(false);
            response.setMensagem("Token inválido, undefined!");
            response.setStateCode(401);
        }else {
            //SessionModel session = sessionRepo.findByToken(request.getHeader().getAuthToken());
            //if (ciclistaRepo.existsByUserId(session.getUser())) {
                List<StationModel> stationList = stationRepo.findAll();
                if (!stationList.isEmpty()) {
                    response.setEstado(true);
                    response.setMensagem("Estações encontradas com sucesso!");
                    for(StationModel station : stationList) {
                        if(juddiService.checkStationService(station.getName())!=null){
                            System.out.println(juddiService.checkStationService(station.getName()));
                            StationResponseType stationType = new StationResponseType();
                            BeanUtils.copyProperties(station, stationType);
                            stationType.setDocks(station.getQtdDocks());
                            stationType.setDocksDisp(station.getQtdDocksDispo());
        
                            GeoLocationDTO geoLocation = geoLocationService.getGeoLocation(String.valueOf(station.getLatitude()), String.valueOf(station.getLongitude()));
        
                            if(geoLocation!=null){
        
                                stationType.setPais(geoLocation.getPais());
                                stationType.setProvincia(geoLocation.getProvincia());
                                stationType.setMunicipio(geoLocation.getMunicipio());
                                stationType.setDistrito(geoLocation.getDistrito());
                                stationType.setAvenida(geoLocation.getAvenida());
        
                            }
                            response.getStationItem().add(stationType);
                        }
                        
                    }
                } else {
                    response.setEstado(false);
                    response.setMensagem("Não há estações ainda!");
                }
        }
        return response;
    }

    public AllStationResponse getAllStationMoreProxime (AllStationMoreProximeRequest request) {
        AllStationResponse response = new AllStationResponse();
        if (!auth.sessionIsValid(request.getHeader().getAuthToken())) {
            //this.DestilaHeadResponse(session);
            response.setEstado(false);
            response.setMensagem("Token inválido, undefined!");
            response.setStateCode(401);
        }else {
            List<StationModel> stationList = stationRepo.findAll();
            if (!stationList.isEmpty()) {
                response.setEstado(true);
                stationList.forEach( station -> {
                    if(juddiService.checkStationService(station.getName())!=null){
                        Double distancia = geoLocationService.
                            calcDistanceHaversine(
                                new Coordinates(request.getBody().getLatitude(), request.getBody().getLongitude()), 
                                new Coordinates(station.getLatitude().doubleValue(), station.getLongitude().doubleValue()), 
                                request.getBody().getRadius()
                            );
                        
                        if(distancia>0){
                            StationResponseType stationType = new StationResponseType();
                            BeanUtils.copyProperties(station, stationType);
                            stationType.setDocks(station.getQtdDocks());
                            stationType.setDocksDisp(station.getQtdDocksDispo());
                            stationType.setDistancia(distancia.intValue());

                            GeoLocationDTO geoLocation = geoLocationService.getGeoLocation(String.valueOf(station.getLatitude()), String.valueOf(station.getLongitude()));

                            if(geoLocation!=null){

                                stationType.setPais(geoLocation.getPais());
                                stationType.setProvincia(geoLocation.getProvincia());
                                stationType.setMunicipio(geoLocation.getMunicipio());
                                stationType.setDistrito(geoLocation.getDistrito());
                                stationType.setAvenida(geoLocation.getAvenida());

                            }
                            response.getStationItem().add(stationType);
                        }
                    }
                });
                if (response.getStationItem().isEmpty()) response.setMensagem("Não há estações num raio de "+request.getBody().getRadius()+" metros");
            } else {
                response.setEstado(false);
                response.setMensagem("Não há estações no sistema ainda!");
            }
        }
        return response;
    }
}
