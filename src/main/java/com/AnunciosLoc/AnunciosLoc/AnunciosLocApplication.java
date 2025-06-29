package com.AnunciosLoc.AnunciosLoc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.AnunciosLoc.AnunciosLoc.bd.station.StationModel;
import com.AnunciosLoc.AnunciosLoc.bd.station.StationRepository;
import com.AnunciosLoc.AnunciosLoc.bd.user.UserRepository;
import com.AnunciosLoc.AnunciosLoc.wsAsCliente.ofJuddi.JuddiService;
import com.AnunciosLoc.AnunciosLoc.wsAsCliente.ofStation.WSstation;

import xml.soap.station.GetStationResponse;


@SpringBootApplication
public class AnunciosLocApplication {

	@Autowired
	JuddiService juddiService;
	@Autowired
    private WSstation stationClient;
    @Autowired(required = true)
    private UserRepository userRepo;
    @Autowired(required = true)
    private StationRepository stationRepo;

	public static void main(String[] args) {
		SpringApplication.run(AnunciosLocApplication.class, args);
		System.out.println("ESTA ON");
		//System.out.println(station);
	}
	@Bean
	@Profile("!test")
	public CommandLineRunner executar () {
		return args -> {
			xml.soap.GetStationResponse gsr = null;
			StationModel station = null;
			boolean pmr = false; //esta variável confirma que pelo menos uma estação foi encontrada no uddi e registrada
			List<String> servicos = juddiService.searchStationService();
			if (servicos != null) {
				for(String servico : servicos){
					try{
						gsr = stationClient.getStationState(servico);
						if (gsr != null) {
							if (stationRepo.existsByEndpoint(servico)) {
								station = stationRepo.findByEndpoint(servico);
							} else { 
								pmr = true;
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
								stationRepo.save(station);
							} catch (Exception exc) {
								System.out.println("Não deu paara salvar o registro!!!");
							} 
							System.out.println("cadabra");
							System.out.println(station);
							stationRepo.save(station);
						} else {
							System.out.println("Estação inactivaaaa!!!");
						}
						System.out.println(servico);
					} catch (Exception e) {
						System.out.println("Estação inactiva!!!");
					}
				}
				if (pmr) {
					System.out.println("Novas estações foram encontradas e adicionadas!!!");
				} else {
					System.out.println("Existem estações disponíveis!!!");
				}
			} else {
				System.out.println("Serviços de estações indisponíveis");
			}
		};
	}


}
