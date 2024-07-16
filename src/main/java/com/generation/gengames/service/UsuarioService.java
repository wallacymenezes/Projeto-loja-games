package com.generation.gengames.service;

import com.generation.gengames.model.Usuario;
import com.generation.gengames.model.UsuarioLogin;
import com.generation.gengames.repository.UsuarioRepository;
import com.generation.gengames.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Verifica se o usuário já existe no banco de dados, se não existir ele criptografa a senha e salva no banco de dados
    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            return Optional.empty();

        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        // Ele salva o objeto usuário no banco de dados e retorna o objeto salvo
        return Optional.of(usuarioRepository.save(usuario));
    }

    // Verifica se o usuário existe no banco de dados, se existir ele criptografa a senha e sobrescreve o usuário no banco
    public Optional<Usuario> atualizarUsuario(Usuario usuario) {

        if(usuarioRepository.findById(usuario.getId()).isPresent()) {

            Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());

            if ( (buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
                return Optional.empty();

            usuario.setSenha(criptografarSenha(usuario.getSenha()));

            return Optional.ofNullable(usuarioRepository.save(usuario));
        }

        return Optional.empty();
    }

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

        if (usuario.isPresent()) {
            if (encoder.matches(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {

                String token = gerarToken(usuario.get().getUsuario());
                usuarioLogin.get().setToken(token);
                usuarioLogin.get().setNome(usuario.get().getNome());

                return usuarioLogin;
            }
        }

        return Optional.empty();
    }

    // Recebe a senha como parâmetro e criptografa a senha
    private String criptografarSenha(String senha) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(senha);
    }

    // Função pega o usuário e unifica com o Bearer gerando um token com todas as informações do usuário
    private String gerarToken(String usuario) {
        return "Bearer " + jwtService.generateToken(usuario);
    }
}
