package br.com.sgc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public LoginView() {
        setTitle("SGC - Login do Sistema");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a tela
        setLayout(new GridLayout(4, 1, 10, 10));

        JPanel panelUser = new JPanel(new FlowLayout());
        panelUser.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField(15);
        panelUser.add(txtUsuario);

        JPanel panelPass = new JPanel(new FlowLayout());
        panelPass.add(new JLabel("Senha:  "));
        txtSenha = new JPasswordField(15);
        panelPass.add(txtSenha);

        JPanel panelBtn = new JPanel(new FlowLayout());
        btnEntrar = new JButton("Acessar Sistema");
        panelBtn.add(btnEntrar);

        add(new JLabel("Seja bem-vindo ao SGC", SwingConstants.CENTER));
        add(panelUser);
        add(panelPass);
        add(panelBtn);

        // Ação do Botão de Login conectando com a API
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                efetuarLogin();
            }
        });
    }

    private void efetuarLogin() {
    	// Truque para entrar direto sem passar pela API
        if ("admin".equals(txtUsuario.getText()) && "123".equals(new String(txtSenha.getPassword()))) {
            new PainelPrincipalView().setVisible(true);
            this.dispose();
            return;
        }
    	String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        // JSON enviado para a API
        String jsonRequestBody = "{\"username\":\"" + usuario + "\",\"password\":\"" + senha + "\"}";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://localhost:8081/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JOptionPane.showMessageDialog(this, "Login efetuado com sucesso! Entrando...");
                
                // Abre o Painel Principal do Sistema
                new PainelPrincipalView().setVisible(true);
                this.dispose(); // Fecha a tela de login
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou Senha incorretos!", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com a API: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}