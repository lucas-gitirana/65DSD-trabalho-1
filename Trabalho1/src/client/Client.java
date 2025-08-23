package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    static Scanner scan;

    public static void main(String[] args) throws IOException {
        scan = new Scanner(System.in);
        scan.useDelimiter("\n");

        boolean continuar = true;
        while (continuar) {
            System.out.println(
                "================================================ \n" +
                "MENU \n" +
                "================================================ \n" +
                "Digite a operação que deseja realizar: \n" +
                "1 - Inserir pessoa\n" +
                "2 - Atualizar pessoa\n" +
                "3 - Consultar pessoa\n" +
                "exit - Sair"
            );

            String operacao = scan.next();
            if (!operacao.equals("exit")) {
                try {
                    handleOperation(Integer.parseInt(operacao));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                continuar = false;
            }
        }
    }

    private static void handleOperation(int operacao) throws Exception {
        String[] pessoa = null;
        String cpf = null;
        String message = null;

        switch (operacao) {
            case 1: //INSERT
                pessoa = bindPessoaFromConsole("");
                sendInsertMessage(pessoa);
                break;
            case 2: //UPDATE
                cpf = getPessoaSelecionadaFromConsole();
                pessoa = bindPessoaFromConsole(cpf);
                sendUpdateMessage(pessoa);
                break;
            case 3: //GET
                cpf = getPessoaSelecionadaFromConsole();
                sendGetMessage(cpf);
                break;
        }
    }

    private static void sendInsertMessage(String[] pessoa) throws IOException {
        String msg = "INSERT;" + pessoa[0] + ";" + pessoa[1] + ";" + pessoa[2];
        sendMessageServer(msg);
    }

    private static void sendUpdateMessage(String[] pessoa) throws IOException {
        String msg = "UPDATE;" + pessoa[0] + ";" + pessoa[1] + ";" + pessoa[2];
        sendMessageServer(msg);
    }

    private static void sendGetMessage(String cpf) throws IOException {
        String msg = "GET;" + cpf;
        sendMessageServer(msg);
    }

    private static void sendMessageServer(String message) throws IOException {
        try (Socket conn = new Socket("127.0.0.1", 65000)) {
            PrintWriter writer = new PrintWriter(conn.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            // Envia mensagem
            writer.println(message);

            // Aguarda resposta
            String resposta = reader.readLine();
            System.out.println(
                "================================================ \n" +
                (!resposta.isBlank() ? resposta : "O servidor não retornou mensagem")
            );
        }
    }

    private static String getPessoaSelecionadaFromConsole() {
        System.out.println("Digite o CPF da pessoa: ");
        return scan.next();
    }

    private static String[] bindPessoaFromConsole(String cpf) throws Exception {
        if (cpf.isBlank()) {
            System.out.println("CPF: ");
            cpf = scan.next();
        }

        System.out.println("Nome: ");
        String nome = scan.next();

        System.out.println("Endereço: ");
        String endereco = scan.next();

        if (cpf.isBlank() || nome.isBlank() || endereco.isBlank()) {
            throw new Exception("Dados incompletos de Pessoa!");
        }

        return new String[]{cpf, nome, endereco};
    }

}
