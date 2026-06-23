package br.com.sgc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        // Tenta pegar o usuário tanto em português quanto em inglês
        String username = dados.get("username") != null ? dados.get("username") : dados.get("usuario");
        
        // Tenta pegar a senha tanto como 'senha' quanto como 'password'
        String senha = dados.get("senha") != null ? dados.get("senha") : dados.get("password");

        // Simulação de login para teste local funcionar imediatamente
        if ("admin".equals(username) && "123".equals(senha)) {
            // Retorna o formato de JSON exato com o token que a tela Swing espera receber
            return ResponseEntity.ok(Map.of("token", "token-ficticio-jwt-sucesso"));
        }

        return ResponseEntity.status(401).body("Usuário ou senha inválidos!");
    }
}