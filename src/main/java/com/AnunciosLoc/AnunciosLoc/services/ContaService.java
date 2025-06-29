package com.AnunciosLoc.AnunciosLoc.services;

import com.AnunciosLoc.AnunciosLoc.bd.conta.Conta;
import com.AnunciosLoc.AnunciosLoc.bd.conta.ContaRepository;
import com.AnunciosLoc.AnunciosLoc.bd.user.UserRepository;
import com.AnunciosLoc.AnunciosLoc.bd.user.*;

import xml.soap.conta.ConsultarSaldoRequest;
import xml.soap.conta.ConsultarSaldoResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xml.soap.conta.UsuarioConta;
import com.AnunciosLoc.AnunciosLoc.utils.conta.ContaUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContaUtil contaUtil;

    public ConsultarSaldoResponse consultarSaldo(ConsultarSaldoRequest request) {
    ConsultarSaldoResponse response = new ConsultarSaldoResponse();

    Long userId = request.getBody().getUserId();

    Optional<Conta> contaOpt = contaRepository.findByUserId(userId);
    Optional<User> userOptional = userRepository.findById(userId);

    // Verifica se a conta e o usuário existem
    System.out.println("Consultando saldo para o usuário com ID: " + userId);
    System.out.println("Conta encontrada: " + contaOpt.isPresent());
    System.out.println("Usuário encontrado: " + userOptional.isPresent());

    if (contaOpt.isPresent() && userOptional.isPresent()) {
        Conta conta = contaOpt.get();
        User user = userOptional.get();

        response.setStatus(true);
        response.setMensagem("Saldo recuperado com sucesso.");
        response.setSaldo(conta.getSaldo());

        UsuarioConta usuarioConta = contaUtil.UsuarioDaConta(user);
        response.getUsuario().add(usuarioConta);
    } else {
        response.setStatus(false);
        response.setMensagem("Conta ou usuário não encontrado.");
    }

    return response;
}
    // Função para criar uma conta
    public boolean criarConta(Long userId, String titular) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            Conta contaExistente = contaRepository.findByUserId(userId).orElse(null);
            if (contaExistente != null) {
                // Conta já existe
                return false;
            }

            Conta novaConta = new Conta();
            novaConta.setTitular(titular);
            novaConta.setSaldo(10);
            novaConta.setDataInicio(LocalDateTime.now());
            novaConta.setUser(userOpt.get());

            contaRepository.save(novaConta);
            return true;
        }
        return false; // Usuário não encontrado
    }

    // Adicionar saldo à conta
    public boolean adicionarSaldo(Long userId, double valor) {
        System.out.println("Adicionando saldo: " + valor + " para o usuário com ID: " + userId);
        Optional<Conta> contaOpt = contaRepository.findByUserId(userId);
        if (contaOpt.isPresent()) {
            System.out.println("Conta encontrada para o usuário com ID: " + userId);
            Conta conta = contaOpt.get();
            conta.setSaldo(conta.getSaldo() + valor);
            contaRepository.save(conta);
            return true;
        }
        System.out.println("Conta não encontrada para o usuário com ID: " + userId);
        return false;
    }

    // Debiter saldo da conta
    public boolean debitarSaldo(Long userId, double valor) {
        Optional<Conta> contaOpt = contaRepository.findByUserId(userId);
        if (contaOpt.isPresent()) {
            Conta conta = contaOpt.get();
            if (conta.getSaldo() >= valor) {
                conta.setSaldo(conta.getSaldo() - valor);
                contaRepository.save(conta);
                return true;
            }
        }
        return false;
    }

    // Consultar saldo da conta
    public Double consultarSaldo(Long userId) {
        Optional<Conta> contaOpt = contaRepository.findByUserId(userId);
        return contaOpt.map(Conta::getSaldo).orElse(null);
    }

}
