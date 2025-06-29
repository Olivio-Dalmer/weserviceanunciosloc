package com.AnunciosLoc.AnunciosLoc.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.AnunciosLoc.AnunciosLoc.bd.anuncio.Anuncio;
import com.AnunciosLoc.AnunciosLoc.bd.anuncio.AnuncioRepository;
import com.AnunciosLoc.AnunciosLoc.bd.condicaoPerfil.CondicaoPerfil;
import com.AnunciosLoc.AnunciosLoc.bd.local.Local;
import com.AnunciosLoc.AnunciosLoc.bd.local.LocalRepository;
import com.AnunciosLoc.AnunciosLoc.bd.politicaEntrega.PoliticaEntrega;
import com.AnunciosLoc.AnunciosLoc.bd.politicaEntrega.PoliticaEntregaRepository;
import com.AnunciosLoc.AnunciosLoc.bd.user.User;
import com.AnunciosLoc.AnunciosLoc.bd.user.UserRepository;
import com.AnunciosLoc.AnunciosLoc.utils.anuncio.AnuncioUtil;

import xml.soap.anuncios.*;

@Service
public class AnunciosService {

    private final AnuncioRepository anuncioRepository;
    private final UserRepository userRepository;
    private final LocalRepository localizacaoRepository;
    private final PoliticaEntregaRepository politicaEntregaRepository;

    @Autowired
    private AnuncioUtil anuncioUtil;

    @Autowired
    private ContaService contaService;

    public AnunciosService(
            AnuncioRepository anuncioRepository,
            UserRepository userRepository,
            PoliticaEntregaRepository politicaEntregaRepository,
            LocalRepository localizacaoRepository) {
        this.anuncioRepository = anuncioRepository;
        this.userRepository = userRepository;
        this.politicaEntregaRepository = politicaEntregaRepository;
        this.localizacaoRepository = localizacaoRepository;
    }

    public AddAnuncioResponse addAnuncio(AddAnuncioRequest request) {
        AddAnuncioResponse response = new AddAnuncioResponse();

        try {
            // Validar usuário
            Optional<User> userOptional = userRepository.findById(request.getBody().getUserId());
            if (!userOptional.isPresent()) {
                response.setStatus(false);
                response.setMensagem("Usuário não encontrado.");
                return response;
            }

            // Validar local
            Optional<Local> localOptional = localizacaoRepository.findById(request.getBody().getLocalId());
            if (!localOptional.isPresent()) {
                response.setStatus(false);
                response.setMensagem("Local não encontrado.");
                return response;
            }

            User user = userOptional.get();
            Local local = localOptional.get();
            LocalDateTime inicio = LocalDateTime.now();

            // Validar datas
            if (request.getBody().getDataExpiracao() == null) {
                response.setStatus(false);
                response.setMensagem("Data de início ou expiração não informada.");
                return response;
            }

            LocalDateTime expiracao = request.getBody().getDataExpiracao().toGregorianCalendar()
                    .toZonedDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            if (inicio.isAfter(expiracao)) {
                response.setStatus(false);
                response.setMensagem("A data de início não pode ser posterior à data de expiração.");
                return response;
            }

            // Validar política de entrega
            if (request.getBody().getPoliticaEntrega() == null) {
                response.setStatus(false);
                response.setMensagem("Erro: política de entrega não informada corretamente.");
                return response;
            }

            // Processar política de entrega
            List<PoliticaEntregaType> politicas = request.getBody().getPoliticaEntrega();
            PoliticaEntregaType politicaDeEntregaType = (politicas != null && !politicas.isEmpty()) ? politicas.get(0)
                    : null;

            PoliticaEntrega politica = new PoliticaEntrega();
            List<CondicaoPerfil> condicoes = new ArrayList<>();
            PoliticaTipo tipoPoliticaEnum = PoliticaTipo.WHITELIST; // padrão

            boolean temCondicaoValida = false;

            if (politicaDeEntregaType != null) {
                System.out
                        .println("Processando política de entrega&%&%&%%: " + politicaDeEntregaType.getTitulo().name());
                String titulo = politicaDeEntregaType.getTitulo().name();
                try {
                    tipoPoliticaEnum = PoliticaTipo.valueOf(titulo.toUpperCase());
                } catch (IllegalArgumentException e) {
                    response.setStatus(false);
                    response.setMensagem("Tipo de política inválido: " + titulo);
                    return response;
                }

                politica.setTitulo(
                        com.AnunciosLoc.AnunciosLoc.bd.politicaEntrega.PoliticaTipo.valueOf(tipoPoliticaEnum.name()));

                if (politicaDeEntregaType.getCondicoes() != null) {
                    for (CondicaoPerfilType cond : politicaDeEntregaType.getCondicoes()) {
                        List<String> chaves = cond.getChave();
                        List<String> valores = cond.getValor();

                        // Validar que chave e valor não estejam vazios ou nulos
                        if (chaves != null && !chaves.isEmpty() &&
                                valores != null && !valores.isEmpty() &&
                                !chaves.get(0).trim().isEmpty() &&
                                !valores.get(0).trim().isEmpty()) {

                            CondicaoPerfil cp = new CondicaoPerfil();
                            cp.setChave(chaves.get(0));
                            cp.setValor(valores.get(0));
                            cp.setPoliticaEntrega(politica);
                            condicoes.add(cp);
                            temCondicaoValida = true;
                        }
                    }
                }

                // Validação para impedir política BLACKLIST sem condições válidas
                if (tipoPoliticaEnum == PoliticaTipo.BLACKLIST && !temCondicaoValida) {
                    response.setStatus(false);
                    response.setMensagem("A política BLACKLIST exige ao menos uma condição válida.");
                    return response;
                }

                if (tipoPoliticaEnum == PoliticaTipo.WHITELIST && !temCondicaoValida) {
                    condicoes.clear();
                }

            } else {

                tipoPoliticaEnum = PoliticaTipo.WHITELIST;
                politica.setTitulo(com.AnunciosLoc.AnunciosLoc.bd.politicaEntrega.PoliticaTipo
                        .valueOf(PoliticaTipo.WHITELIST.name()));
                condicoes.clear(); // ← NENHUMA CONDIÇÃO
            }

            // Verificação da política DEPOIS de extrair os dados
            if (tipoPoliticaEnum == PoliticaTipo.BLACKLIST && !temCondicaoValida) {
                response.setStatus(false);
                response.setMensagem("A política BLACKLIST exige ao menos uma condição válida.");
                return response;
            }

            politica.setCondicoes(condicoes);

            // Resposta - Política
            PoliticaEntregaType politicaResponse = new PoliticaEntregaType();
            politicaResponse.setTitulo(tipoPoliticaEnum);
            for (CondicaoPerfil cp : condicoes) {
                CondicaoPerfilType cpt = new CondicaoPerfilType();
                if (cp.getChave() != null)
                    cpt.getChave().add(cp.getChave());
                if (cp.getValor() != null)
                    cpt.getValor().add(cp.getValor());
                politicaResponse.getCondicoes().add(cpt);
            }

            // Criar anúncio
            Anuncio anuncio = new Anuncio();
            anuncio.setTitulo(request.getBody().getTitulo());
            anuncio.setDescricao(request.getBody().getDescricao());
            anuncio.setDataInicio(inicio);
            anuncio.setDataExpiracao(expiracao);
            anuncio.setUser(user);
            anuncio.setLocalizacao(local);
            anuncio = anuncioRepository.save(anuncio);
            
            System.out.println("Anúncio criado: " + anuncio.getId());

            politica.setAnuncio(anuncio);
            politicaEntregaRepository.save(politica);
            response.setPoliticaEntrega(politicaResponse);

            boolean contaCriada = contaService.adicionarSaldo(user.getId(), 2.0);
            System.out.println("Conta criada: " + contaCriada);

            if (!contaCriada) {
                response.setStatus(false);
                response.setMensagem("Erro ao adicionar saldo à conta do usuário.");
                return response;
            } else {
                response.setMensagem("Saldo adicionado com sucesso à conta do usuário.");
            }

            // Resposta - Usuário
            UserType userType = new UserType();
            userType.setId(user.getId());
            userType.setEmail(user.getEmail());
            userType.setUsername(user.getUsername());
            userType.setGenero(user.getGenero());
            userType.setTelefone(user.getTelefone());
            response.getUsuario().add(userType);

            // Resposta - Local
            LocalType localType = new LocalType();
            localType.setId(local.getId());
            localType.setNome(local.getNome());
            localType.setLatitude(local.getLatitude());
            localType.setLongitude(local.getLongitude());
            response.getLocalType().add(localType);

            // Sucesso
            response.setStatus(true);
            response.setMensagem("Anúncio adicionado com sucesso.");
            response.setAnuncioId(anuncio.getId());
            response.setUserId(user.getId());
            response.setLocalId(local.getId());

        } catch (Exception e) {
            e.printStackTrace();

            String mensagemErro;
            if (e instanceof IllegalArgumentException) {
                mensagemErro = "Houve um problema com os dados fornecidos.";
            } else if (e instanceof java.time.format.DateTimeParseException) {
                mensagemErro = "Formato de data inválido.";
            } else if (e.getCause() != null && e.getCause().toString().contains("constraint")) {
                mensagemErro = "Conflito nos dados informados.";
            } else {
                mensagemErro = "Erro inesperado. Tente novamente.";
            }

            response.setStatus(false);
            response.setMensagem(mensagemErro);
        }

        return response;
    }

    @Transactional
    public AllAnuncioResponse getAllAnuncios(AllAnuncioRequest request) {
        AllAnuncioResponse response = new AllAnuncioResponse();
        List<AnuncioType> anunciosDisponiveis = new ArrayList<>();

        try {
            Long userId = request.getBody().getUserId();
            Long localId = request.getBody().getLocalId();

            // Buscar usuário e local informado
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Local> optionalLocal = localizacaoRepository.findById(localId);

            if (!optionalUser.isPresent() || !optionalLocal.isPresent()) {
                response.setEstado(false);
                response.setMensagem("Usuário ou Local inválido.");
                return response;
            }

            User user = optionalUser.get();
            Local localDoUsuario = optionalLocal.get();
            LocalDateTime agora = LocalDateTime.now();

            List<Anuncio> anuncios = anuncioRepository.findAll();

            for (Anuncio anuncio : anuncios) {

                if (anuncio.getDataInicio().isAfter(agora) || anuncio.getDataExpiracao().isBefore(agora)) {
                    continue;
                }

                Local localAnuncio = anuncio.getLocalizacao();
                if (localAnuncio == null || localAnuncio.getLatitude() == null || localAnuncio.getLongitude() == null) {
                    continue;
                }

                // Verifica política de entrega
                PoliticaEntrega politica = anuncio.getPoliticaEntrega();
                boolean visivel = true;

                if (politica != null && politica.getCondicoes() != null && !politica.getCondicoes().isEmpty()) {
                    System.out.println("Verificando política de entrega: " + politica.getTitulo());
                    switch (politica.getTitulo()) {

                        case WHITELIST:
                            visivel = anuncioUtil.usuarioPertenceAWhiteList(userId, politica.getCondicoes());
                            break;
                        case BLACKLIST:
                            visivel = !anuncioUtil.usuarioEstaNaBlacklist(userId, politica.getCondicoes());
                            break;
                    }
                }

                if (!visivel)
                    continue;

                // Adiciona à resposta
                AnuncioType anuncioType = anuncioUtil.MapAnuncioType(anuncio);
                anunciosDisponiveis.add(anuncioType);
            }

            response.setEstado(true);
            response.setMensagem(anunciosDisponiveis.isEmpty()
                    ? "Nenhum anúncio disponível com os filtros aplicados."
                    : "Anúncios disponíveis retornados com sucesso.");
            response.getAnuncios().addAll(anunciosDisponiveis);

            if (!anunciosDisponiveis.isEmpty()) {
                // Adiciona usuário
                UserType userType = anuncioUtil.mapUserToUserType(user);
                if (userType != null)
                    response.getUsuario().add(userType);

                // Adiciona local
                response.getLocalType().add(anuncioUtil.mapLocalToLocalType(localDoUsuario));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setEstado(false);
            response.setMensagem("Erro ao buscar anúncios: " + e.getMessage());
        }

        return response;
    }

    public AllAnuncioResponse getAllAnunciosTodos(@RequestPayload AllAnuncioTodosRequest request) {
        AllAnuncioResponse response = new AllAnuncioResponse();

        try {
            List<Anuncio> anuncios = anuncioRepository.findAll();

            if (anuncios.isEmpty()) {
                response.setMensagem("Nenhum anúncio encontrado.");
                response.setEstado(false);
                return response;
            }

            for (Anuncio anuncio : anuncios) {
                AnuncioType item = new AnuncioType();
                item.setId(anuncio.getId().intValue());
                item.setTitulo(anuncio.getTitulo());
                item.setDescricao(anuncio.getDescricao());
                item.setDataExpiracao(anuncio.getDataExpiracao().toString());

                // --- User ---
                User user = anuncio.getUser();
                if (user != null) {
                    UserType userType = new UserType();
                    userType.setId(user.getId().intValue());
                    userType.setUsername(user.getUsername());// ou user.getName()
                    userType.setEmail(user.getEmail());
                    userType.setGenero(user.getGenero());
                    userType.setTelefone(user.getTelefone());
                    // adicione outros campos conforme sua definição do UserType

                    item.setUsuario(userType);
                }

                // --- Localização ---
                Local local = anuncio.getLocalizacao();
                if (local != null) {
                    LocalType localType = new LocalType();
                    localType.setId(local.getId().intValue());
                    localType.setNome(local.getNome()); // ou local.getDescricao()
                    localType.setLatitude(local.getLatitude());
                    localType.setLongitude(local.getLongitude());
                    // adicione outros campos conforme sua definição do LocalType

                    item.setLocal(localType);
                }

                response.getAnuncios().add(item);
            }

            response.setMensagem("Anúncios listados com sucesso.");
            response.setEstado(true);

        } catch (Exception e) {
            response.setMensagem("Erro ao listar os anúncios: " + e.getMessage());
            response.setEstado(false);
        }

        return response;
    }

    public ListarAnuncioCriadosResponse ListarAnuncioCriados(ListarAnuncioCriadosRequest request) {
        ListarAnuncioCriadosResponse response = new ListarAnuncioCriadosResponse();

        try {
            Long userId = request.getBody().getUserId();
            Optional<User> userOpt = userRepository.findById(userId);

            if (!userOpt.isPresent()) {
                response.setEstado(false);
                response.setMensagem("Usuário não encontrado.");
                return response;
            }

            User user = userOpt.get();

            List<Anuncio> anuncios = anuncioRepository.findByUserIdWithRelations(userId);

            if (anuncios.isEmpty()) {
                response.setEstado(true);
                response.setMensagem("O usuário não criou nenhum anúncio.");
                response.setQtdAnuncio(0L);
                return response;
            }

            // Montar resposta
            ListarAnuncioCriadosResponse.Anuncios xmlAnuncios = new ListarAnuncioCriadosResponse.Anuncios();

            for (Anuncio anuncio : anuncios) {

                AnuncioType anuncioType = anuncioUtil.MapAnuncioType(anuncio);

                // Mapear local
                Local local = anuncio.getLocalizacao();
                if (local != null) {
                    LocalType localType = anuncioUtil.mapLocalToLocalType(local);
                    anuncioType.setLocal(localType);
                }

                // Mapear política
                PoliticaEntrega politica = anuncio.getPoliticaEntrega();
                if (politica != null) {
                    PoliticaEntregaType politicaType = mapPoliticaToType(politica);
                    anuncioType.setPoliticaEntrega(politicaType);
                }

                xmlAnuncios.getAnuncio().add(anuncioType);
            }

            // Resposta final
            UserType userType = anuncioUtil.mapUserToUserType(user);
            if (userType != null) {
                response.getUsuario().add(userType);
            }

            response.setAnuncios(xmlAnuncios);
            response.setQtdAnuncio((long) xmlAnuncios.getAnuncio().size());
            response.setMensagem("Anúncios carregados com sucesso.");
            response.setEstado(true);

        } catch (Exception e) {
            e.printStackTrace();
            response.setEstado(false);
            response.setMensagem("Erro ao listar anúncios: " + e.getMessage());
        }
        return response;
    }

    public RemoveAnuncioResponse removeAnuncio(RemoveAnuncioRequest request) {
        RemoveAnuncioResponse response = new RemoveAnuncioResponse();

        long anuncioId = request.getBody().getId();
        long userId = request.getBody().getUserId();

        Optional<Anuncio> anuncioOptional = anuncioRepository.findById(anuncioId);

        if (anuncioOptional.isPresent()) {
            Anuncio anuncio = anuncioOptional.get();

            if (anuncio.getUser().getId() == userId) {
                anuncioRepository.delete(anuncio);
                response.setMensagem("Anúncio removido com sucesso!");
                response.setStatus(true);
            } else {
                response.setMensagem("Você não tem permissão para remover este anúncio.");
                response.setStatus(false);
            }
        } else {
            response.setMensagem("Anúncio não encontrado.");
            response.setStatus(false);
        }

        return response;
    }

    private PoliticaEntregaType mapPoliticaToType(PoliticaEntrega politica) {
        if (politica == null)
            return null;

        PoliticaEntregaType type = new PoliticaEntregaType();
        type.setTitulo(PoliticaTipo.valueOf(politica.getTitulo().name()));

        if (politica.getCondicoes() != null) {
            for (CondicaoPerfil condicao : politica.getCondicoes()) {
                CondicaoPerfilType condType = new CondicaoPerfilType();
                condType.getChave().add(condicao.getChave());
                condType.getValor().add(condicao.getValor());
                type.getCondicoes().add(condType);
            }
        }

        return type;
    }

}
