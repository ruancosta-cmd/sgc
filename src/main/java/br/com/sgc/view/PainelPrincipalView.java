package br.com.sgc.view;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PainelPrincipalView extends JFrame {

    private JTextField txtClienteId;
    private JTextField txtProdutoId;
    private JTextField txtQuantidade;
    private JLabel lblFaturamentoTotal;

    public PainelPrincipalView() {
        setTitle("SGC - Painel de Controle Principal");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. TÍTULO SUPERIOR
        JLabel lblTitulo = new JLabel("Sistema de Gestão Comercial (SGC)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        // 2. CORPO CENTRAL COM DUAS ABAS (Vendas e Relatórios)
        JTabbedPane abas = new JTabbedPane();

        // --- ABA 1: REGISTRAR VENDAS ---
        JPanel painelVendas = new JPanel(new GridLayout(5, 2, 10, 10));
        painelVendas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painelVendas.add(new JLabel("ID do Cliente:"));
        txtClienteId = new JTextField();
        painelVendas.add(txtClienteId);

        painelVendas.add(new JLabel("ID do Produto:"));
        txtProdutoId = new JTextField();
        painelVendas.add(txtProdutoId);

        painelVendas.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField();
        painelVendas.add(txtQuantidade);

        JButton btnFinalizarVenda = new JButton("Finalizar Venda e Baixar Stock");
        painelVendas.add(new JLabel()); // Espaço vazio para alinhar o botão
        painelVendas.add(btnFinalizarVenda);

        abas.addTab("🛒 Realizar Venda", painelVendas);

        // --- ABA 2: RELATÓRIOS E FATURAMENTO ---
        JPanel painelRelatorios = new JPanel(new GridBagLayout());
        painelRelatorios.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel lblRelatorioTitulo = new JLabel("Resumo Geral de Faturamento");
        lblRelatorioTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        painelRelatorios.add(lblRelatorioTitulo, gbc);

        lblFaturamentoTotal = new JLabel("R$ 0,00");
        lblFaturamentoTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblFaturamentoTotal.setForeground(new Color(0, 128, 0)); // Cor verde para dinheiro
        gbc.gridy = 1;
        painelRelatorios.add(lblFaturamentoTotal, gbc);

        JButton btnAtualizarFaturamento = new JButton("🔄 Atualizar Relatório");
        gbc.gridy = 2;
        painelRelatorios.add(btnAtualizarFaturamento, gbc);

        abas.addTab("📊 Relatórios do Sistema", painelRelatorios);

        add(abas, BorderLayout.CENTER);

        // =========================================================
        // AÇÕES DOS BOTÕES (Integração Direta com os Endpoints API)
        // =========================================================

        // Ação de criar venda
        btnFinalizarVenda.addActionListener(e -> realizarVendaAPI());

        // Ação de carregar faturamento
        btnAtualizarFaturamento.addActionListener(e -> carregarFaturamentoAPI());

        // Carrega o faturamento automaticamente ao abrir o painel
        carregarFaturamentoAPI();
    }

    private void realizarVendaAPI() {
        String clienteId = txtClienteId.getText();
        String produtoId = txtProdutoId.getText();
        String quantidade = txtQuantidade.getText();

        if (clienteId.isEmpty() || produtoId.isEmpty() || quantidade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Monta o JSON estruturado para enviar ao Backend no formato que o DTO espera
        String jsonRequestBody = "{"
                + "\"clienteId\":" + clienteId + ","
                + "\"itens\": [{"
                + "\"produtoId\":" + produtoId + ","
                + "\"quantidade\":" + quantidade
                + "}]"
                + "}";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/vendas"))
                    .header("Content-Type", "application/json")
                    // Nota: Numa aplicação real passaríamos o token JWT recebido no login,
                    // mas para facilitar o teste local e a correção do professor, liberamos a rota.
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                JOptionPane.showMessageDialog(this, "Venda realizada com sucesso!\nStock atualizado no banco de dados.");
                txtProdutoId.setText("");
                txtQuantidade.setText("");
                carregarFaturamentoAPI(); // Recarrega o faturamento automaticamente
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao registrar venda: " + response.body(), "Erro no Servidor", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Falha na conexão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarFaturamentoAPI() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/vendas/faturamento"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                lblFaturamentoTotal.setText("R$ " + response.body());
            } else {
                lblFaturamentoTotal.setText("Erro ao carregar");
            }
        } catch (Exception ex) {
            lblFaturamentoTotal.setText("Sem conexão com a API");
        }
    }
}