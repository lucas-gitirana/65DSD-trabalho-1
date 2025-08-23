package server;

import server.controller.PessoaController;
import server.model.Pessoa;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static PessoaController controller = new PessoaController();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(65000);
        server.setReuseAddress(true);
        System.out.println("Servidor iniciado. Aguardando conexões...");

        while (true) {
            // aceita um cliente
            Socket conn = server.accept();
            System.out.println("Novo cliente conectado: " + conn.getInetAddress());

            // cria uma thread para tratar esse cliente
            new Thread(() -> handleClient(conn)).start();
        }
    }

    private static void handleClient(Socket conn) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            PrintWriter writer = new PrintWriter(conn.getOutputStream(), true)
        ) {
            String mensagem;
            while ((mensagem = reader.readLine()) != null) {
                System.out.println("Mensagem recebida: " + mensagem);

                String response;
                try {
                    response = handleMessageFromClient(mensagem);
                } catch (Exception e) {
                    response = e.getMessage();
                }

                // envia resposta para o cliente
                writer.println(response);
            }
            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            System.err.println("Erro com cliente: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String handleMessageFromClient(String mensagem) throws Exception {
        String[] partes = mensagem.split(";");
        String operacao = partes[0];

        Pessoa p = null;

        switch (operacao) {
            case "INSERT":
                p = new Pessoa(partes[1], partes[2], partes[3]);
                controller.createPessoa(p);
                return "";

            case "UPDATE":
                p = new Pessoa(partes[1], partes[2], partes[3]);
                controller.updatePessoa(p);
                return "Pessoa atualizada com sucesso";

            case "GET":
                p = controller.getPessoa(partes[1]);
                return p.toString();
        }

        return "";
    }
}
