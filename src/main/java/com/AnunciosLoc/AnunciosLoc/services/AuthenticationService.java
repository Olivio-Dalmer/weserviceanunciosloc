package com.AnunciosLoc.AnunciosLoc.services;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.AnunciosLoc.AnunciosLoc.bd.session.SessionModel;
import com.AnunciosLoc.AnunciosLoc.bd.session.SessionRepository;
import com.AnunciosLoc.AnunciosLoc.bd.user.User;
import com.AnunciosLoc.AnunciosLoc.bd.user.UserRepository;
import com.AnunciosLoc.AnunciosLoc.utils.HashPassword;
//import ao.uan.fc.cc4.bikeshared.wsAsCliente.ofReplica.WSReplica;
import com.AnunciosLoc.AnunciosLoc.config.security.JwtToken.Token;
import xml.soap.user.LoginRequest;
import xml.soap.user.LogoutRequest;
import xml.soap.user.UserResponse;
import xml.soap.user.ValidateTokenRequest;

@Service
public class AuthenticationService {

    @Autowired(required = true)
    private UserRepository userRepo;
    @Autowired(required = true)
    private SessionRepository sessionRepo;
    @Autowired(required = true)
    private UserRepository userRepository;
    @Autowired
    private Token jwtToken;

    public UserResponse login(LoginRequest request) {
        System.out.println("Dentro do Serviço de Login");
        UserResponse response = new UserResponse();
        Optional<User> user = userRepo.findByEmail(request.getEmail());
        if (user != null) {
            SessionModel sessionModel = sessionRepo.findByUser(user.get().getId());
            if (sessionModel != null) {
                sessionRepo.deleteById(sessionModel.getId());
            }else if(user.get().getTipo()==2){
                /*sessionModel = sessionRepo.findByFingerprint(request.getMac());
                if (sessionModel != null) {
                    sessionRepo.deleteById(sessionModel.getId());
                }*/
            }
            if (user.get().getPassword().equals(HashPassword.hashing(request.getPassword()))) {
                SessionModel session = new SessionModel();
                session.setToken(jwtToken.generateToken(user.get().getEmail(), user.get().getTipo()));
                session.setUser(user.get().getId());
                if(user.get().getTipo()==2);
                sessionRepo.save(session);
                //serverWriter.writeInReplica("session", sessionRepo.save(session).returnString());

                BeanUtils.copyProperties(user, response);

                response.setToken(session.getToken());
                response.setEstado(true);
                response.setMensagem("Sessao iniciada com sucesso!");
            } else {
                response.setEstado(false);
                response.setMensagem("email ou password errados!");
            }
        } else {
            response.setEstado(false);
            response.setMensagem("email ou password errados!");
        }
        System.out.println("Fez login!!!");
        return response;
    }

    public UserResponse logout(LogoutRequest request) {
        System.out.println("Dentro do Serviço de Logout");
        UserResponse response = new UserResponse();

        SessionModel sessionModel = sessionRepo.findByToken(request.getEmail());
        sessionRepo.deleteById(sessionModel.getId());
        response.setEstado(true);
        response.setMensagem("Sessão terminada com sucesso!");

        return response;
    }

    public UserResponse validationSession(ValidateTokenRequest request) {

        System.out.println("Dentro do Serviço Validate Session");
        UserResponse response = new UserResponse();

        SessionModel sessionModel = sessionRepo.findByToken(request.getAuthToken());
        if (sessionModel == null) response.setEstado(false);
        else {
            response.setEstado(true);
            Optional<User> user = userRepo.findById(sessionModel.getUser());
            if (user.isPresent()) {
                // BeanUtils.copyProperties(user, response);
                response.setEmail(user.get().getEmail());
                response.setUsername(user.get().getUsername());
                response.setDatanascimento(user.get().getDatanascimento());
                response.setGenero(user.get().getGenero());
                response.setTelefone(user.get().getTelefone());
                response.setToken(sessionModel.getToken());
            }
        }
        return response;
    }

    public boolean sessionIsValid (String authToken) {
        SessionModel session = sessionRepo.findByToken(authToken);
        if (session!=null) {
            Optional<User> user = userRepo.findById(session.getUser());
            if (user.isPresent()) {
                if (user.get().getTipo() == 2){
                    System.out.println("tipo 2");
                    return true;
                } else {
                    System.out.println("tipo 1");
                    Integer confirm = (jwtToken.getSubject(session.getToken()).equals(" "))? 0 : 1;
                    switch (confirm) {
                        case 0: 
                            sessionRepo.delete(session);
                            return false;
                        case 1:
                            return true;
                    }
                }
            } else sessionRepo.delete(session);
        }
        return false;
    }

}