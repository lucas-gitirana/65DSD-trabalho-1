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
                "4 - Remover pessoa\n" +
                "5 - Listar pessoas\n" +
                "6 - Inserir turma\n" +
                "7 - Adicionar aluno na turma\n" +
                "8 - Atualizar turma\n" +
                "9 - Consultar turma\n" +
                "10 - Remover turma\n" +
                "11 - Listar turmas\n" +
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
        String[] dados = null;
        String identificador = null;
        String prefixo = operacao <= 5 ? "PESSOA" : "TURMA";

        switch (operacao) {
            case 1: //INSERT
            case 6:
                dados = bindDataFromConsole(prefixo, "");
                sendInsertMessage(prefixo, dados);
                break;
            case 2: //UPDATE
            case 8:
                identificador = getSelectedRegisterFromConsole(prefixo);
                dados = bindDataFromConsole(prefixo, identificador);
                sendUpdateMessage(prefixo, dados);
                break;
            case 7: //ADD_PESSOA
                dados = getAlunoTurmaFromConsole();
                String msg =  "TURMA;ADD_PESSOA;" + dados[0] + ";" + dados[1];
                sendMessageServer(msg);
                break;
            case 3: //GET
            case 9:
                identificador = getSelectedRegisterFromConsole(prefixo);
                sendGetMessage(prefixo, identificador);
                break;
            case 4: //DELETE
            case 10:
                identificador = getSelectedRegisterFromConsole(prefixo);
                sendDeleteMessage(prefixo, identificador);
                break;
            case 5: //LIST
            case 11:
                sendListMessage(prefixo);
                break;
        }
    }

    private static void sendInsertMessage(String prefixo, String[] dados) throws IOException {
        String msg = prefixo.toUpperCase() + ";INSERT;" + dados[0] + ";" + dados[1] + ";" + dados[2];
        sendMessageServer(msg);
    }

    private static void sendUpdateMessage(String prefixo, String[] dados) throws IOException {
        String msg = prefixo.toUpperCase() + ";UPDATE;" + dados[0] + ";" + dados[1] + ";" + dados[2];
        sendMessageServer(msg);
    }

    private static void sendGetMessage(String prefixo, String cpf) throws IOException {
        String msg = prefixo.toUpperCase() + ";GET;" + cpf;
        sendMessageServer(msg);
    }

    private static void sendDeleteMessage(String prefixo, String cpf) throws IOException {
        String msg = prefixo.toUpperCase() + ";DELETE;" + cpf;
        sendMessageServer(msg);
    }

    private static void sendListMessage(String prefixo) throws IOException {
        String msg = prefixo.toUpperCase() + ";LIST";
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

    private static String getSelectedRegisterFromConsole(String prefixo) {
        switch (prefixo) {
            case "PESSOA": return getPessoaSelecionadaFromConsole();
            case "TURMA": return getTurmaSelecionadaFromConsole();
            default: return null;
        }
    }

    private static String getPessoaSelecionadaFromConsole() {
        System.out.println("Digite o CPF da pessoa: ");
        return scan.next();
    }

    private static String getTurmaSelecionadaFromConsole() {
        System.out.println("Digite o ID da turma: ");
        return scan.next();
    }

    private static String[] getAlunoTurmaFromConsole() {
        String cpf = getPessoaSelecionadaFromConsole();
        String idTurma = getTurmaSelecionadaFromConsole();
        return new String[]{cpf, idTurma};
    }

    private static String[] bindDataFromConsole(String prefixo, String identificador) throws Exception {
        switch (prefixo) {
            case "PESSOA": return bindPessoaFromConsole(identificador);
            case "TURMA": return bindTurmaFromConsole(identificador);
            default: return null;
        }
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

    private static String[] bindTurmaFromConsole(String id) throws Exception {
        System.out.println("Codigo: ");
        String codigo = scan.next();

        System.out.println("Quantidade de alunos: ");
        String qtdAlunos = scan.next();

        if (codigo.isBlank() || qtdAlunos.isBlank()) {
            throw new Exception("Dados incompletos de Pessoa!");
        }

        return new String[]{id, codigo, qtdAlunos};
    }

}
