import controller.AlunoController;
import controller.ProfessorController;
import controller.TurmaController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static AlunoController alunoController = new AlunoController();
    static ProfessorController professorController = new ProfessorController();
    static TurmaController turmaController = new TurmaController(alunoController, professorController);

    public static void main(String[] args) throws IOException {
        alunoController.setTurmaController(turmaController);
        professorController.setTurmaController(turmaController);

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
        switch (partes[0]) {
            case "PESSOA_ALUNO":
                return alunoController.handleMessage(partes);
            case "PESSOA_PROFESSOR":
                return professorController.handleMessage(partes);
            case "TURMA":
                return turmaController.handleMessage(partes);
            default:
                return "";
        }
    }


}
