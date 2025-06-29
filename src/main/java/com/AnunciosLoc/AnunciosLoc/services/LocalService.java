package com.AnunciosLoc.AnunciosLoc.services;

import java.util.List;
import java.util.Optional;

import com.AnunciosLoc.AnunciosLoc.bd.local.Local;
import com.AnunciosLoc.AnunciosLoc.bd.local.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xml.soap.local.*;

@Service

public class LocalService {
    @Autowired
    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    // Adicionar Localização    
    public AddLocalResponse adicionarLocal(AddLocalRequest request) {
        // AddLocalResponse response = new AddLocalResponse();
        AddLocalResponse response = new AddLocalResponse();
        AddLocalResponse.Body body = new AddLocalResponse.Body();

        try {
            Local local = new Local();
            local.setNome(request.getBody().getNome());
            local.setLatitude(request.getBody().getLatitude());
            local.setLongitude(request.getBody().getLongitude());

            localRepository.save(local);

            body.setMensagem("Localização adicionada com sucesso!");
            body.setStatus(true);
        } catch (Exception e) {
            body.setMensagem("Erro ao adicionar localização: " + e.getMessage());
            body.setStatus(false);
        }

        response.setBody(body);

        return response;
    }

    // Listar Localizações

    public AllLocalResponse listarTodosOsLocais(AllLocalRequest request) {
        AllLocalResponse response = new AllLocalResponse();

        try {
            List<Local> locais = localRepository.findAll();

            for (Local local : locais) {
                LocalType localType = new LocalType();
                localType.setId(local.getId());
                localType.setNome(local.getNome());
                localType.setLatitude(local.getLatitude());
                localType.setLongitude(local.getLongitude());
                response.getLocal().add(localType);
            }

            // Setando status e mensagem no caso de sucesso
            response.setStatus(true);
            response.setMensagem("Localizações listadas com sucesso.");
        } catch (Exception e) {
            // Setando status e mensagem no caso de erro
            response.setStatus(false);
            response.setMensagem("Erro ao listar localizações: " + e.getMessage());

        }

        return response;
    }

    // Pegar Local
   public GetLocalResponse pegarLocal(GetLocalRequest request) {
    GetLocalResponse response = new GetLocalResponse();

    try {
        Long localId = request.getId(); // CORRETO

        Optional<Local> localEncontrado = localRepository.findById(localId);

        if (localEncontrado.isPresent()) {
            Local local = localEncontrado.get();

            LocalType localType = new LocalType();
            localType.setId(local.getId());
            localType.setNome(local.getNome());
            localType.setLatitude(local.getLatitude());
            localType.setLongitude(local.getLongitude());

            response.setId(local.getId());
            response.setNome(local.getNome());
            response.setLatitude(local.getLatitude());
            response.setLongitude(local.getLongitude());
            response.setStatus(true);
            response.setMensagem("Local encontrado com sucesso.");
        } else {
            response.setStatus(false);
            response.setMensagem("Local com ID " + localId + " não encontrado.");
        }

    } catch (Exception e) {
        response.setStatus(false);
        response.setMensagem("Erro ao buscar local: " + e.getMessage());
    }

    return response;
}


    public RemoveLocalResponse removerLocal(RemoveLocalRequest request) {
        RemoveLocalResponse response = new RemoveLocalResponse();

        try {
            Long id = request.getBody().getId();

            // Verificação de entrada válida
            if (id == null || id <= 0) {
                response.setStatus(false);
                response.setMensagem("ID inválido para remoção.");
                response.setId(-1);
                return response;
            }

            // Verifica se o local existe antes de tentar remover
            Optional<Local> optionalLocal = localRepository.findById(id);

            if (!optionalLocal.isPresent()) {
                response.setStatus(false);
                response.setMensagem("Esse Local não existe ");
                response.setId(id.intValue());
                return response;
            }

            // Realiza a remoção
            localRepository.deleteById(id);

            // Confirmação de sucesso na remoção
            response.setStatus(true);
            response.setMensagem("Local removido com sucesso.");
            response.setId(id.intValue());

        } catch (Exception e) {
            // Tratamento de exceções inesperadas
            response.setStatus(false);
            response.setMensagem("Erro ao remover local: " + e.getMessage());
            response.setId(-1);

            // Log do erro para auditoria/debug
            System.err.println("Erro ao remover local: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
