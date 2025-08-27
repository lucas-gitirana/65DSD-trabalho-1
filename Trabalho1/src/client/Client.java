package client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
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
                "1 - Gerenciar alunos\n" +
                "2 - Gerenciar professores\n" +
                "3 - Gerenciar turma\n" +
                "exit - Sair"
            );

            String operacao = scan.next();
            String context = operacao;
            if (!operacao.equals("exit")) {
                System.out.println(
                    "================================================ \n" +
                    "MENU \n" +
                    "================================================ \n" +
                    "Digite a operação que deseja realizar: \n" +
                    getOptionsFromMenu(Integer.parseInt(operacao))
                );

                operacao = scan.next();

                try {
                    handleOperation(Integer.parseInt(operacao), Integer.parseInt(context));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                continuar = false;
            }
        }
    }

    private static String getOptionsFromMenu(int operacao) {
        switch (operacao) {
            case 1:
                return "1 - Inserir aluno\n" +
                    "2 - Atualizar aluno\n" +
                    "3 - Consultar aluno\n" +
                    "4 - Remover aluno\n" +
                    "5 - Listar alunos\n";
            case 2:
                return  "1 - Inserir professor\n" +
                    "2 - Atualizar professor\n" +
                    "3 - Consultar professor\n" +
                    "4 - Remover professor\n" +
                    "5 - Listar professores\n";
            case 3:
                return "1 - Inserir turma\n" +
                    "2 - Atualizar turma\n" +
                    "3 - Consultar turma\n" +
                    "4 - Remover turma\n" +
                    "5 - Listar turmas\n" +
                    "6 - Adicionar aluno na turma\n" +
                    "7 - Remover aluno da turma\n" +
                    "8 - Adicionar professor na turma\n" +
                    "9 - Remover professor da turma\n";
            default:
                return "";
        }
    }

    private static void handleOperation(int operacao, int context) throws Exception {
        String[] dados = null;
        String identificador = null;
        String msg = null;

        String prefixo = "";
        if (context == 1) {
            prefixo = "PESSOA_ALUNO";
        } else if (context == 2) {
            prefixo = "PESSOA_PROFESSOR";
        } else {
            prefixo = "TURMA";
        }

        switch (operacao) {
            case 1: //INSERT
                dados = bindDataFromConsole(prefixo, "");
                sendInsertMessage(prefixo, dados);
                break;
            case 2: //UPDATE
                identificador = getSelectedRegisterFromConsole(prefixo);
                dados = bindDataFromConsole(prefixo, identificador);
                sendUpdateMessage(prefixo, dados);
                break;
            case 3: //GET
                identificador = getSelectedRegisterFromConsole(prefixo);
                sendGetMessage(prefixo, identificador);
                break;
            case 4: //DELETE
                identificador = getSelectedRegisterFromConsole(prefixo);
                sendDeleteMessage(prefixo, identificador);
                break;
            case 5: //LIST
                sendListMessage(prefixo);
                break;
            case 6: //ADD_ALUNO
                dados = getAlunoTurmaFromConsole();
                msg =  "TURMA;ADD_ALUNO;" + dados[0] + ";" + dados[1];
                sendMessageServer(msg);
                break;
            case 7: //DELETE_ALUNO
                dados = getAlunoTurmaFromConsole();
                msg = "TURMA;DELETE_ALUNO;" + dados[0] + ";" + dados[1];
                sendMessageServer(msg);
                break;
            case 8: //ADD_PROFESSOR
                dados = getAlunoTurmaFromConsole();
                msg =  "TURMA;ADD_PROFESSOR;" + dados[0] + ";" + dados[1];
                sendMessageServer(msg);
                break;
            case 9: //DELETE_PROFESSOR
                dados = getAlunoTurmaFromConsole();
                msg = "TURMA;DELETE_PROFESSOR;" + dados[0] + ";" + dados[1];
                sendMessageServer(msg);
                break;

        }
    }

    private static void sendInsertMessage(String prefixo, String[] dados) throws IOException {
        StringBuilder msg = new StringBuilder(prefixo.toUpperCase() + ";INSERT");
        for (int i = 0; i < dados.length; i++) {
            msg.append(";").append(dados[i]);
        }
        sendMessageServer(msg.toString());
    }

    private static void sendUpdateMessage(String prefixo, String[] dados) throws IOException {
        StringBuilder msg = new StringBuilder(prefixo.toUpperCase() + ";UPDATE");
        for (int i = 0; i < dados.length; i++) {
            msg.append(";").append(dados[i]);
        }
        sendMessageServer(msg.toString());
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
            case "PESSOA_ALUNO":
            case "PESSOA_PROFESSOR":
                return getPessoaSelecionadaFromConsole();
            case "TURMA": return getTurmaSelecionadaFromConsole();
            default: return null;
        }
    }

    private static String getPessoaSelecionadaFromConsole() {
        System.out.println("Digite o CPF da pessoa: ");
        return scan.next();
    }

    private static String getTurmaSelecionadaFromConsole() {
        System.out.println("Digite o código da turma: ");
        return scan.next();
    }

    private static String[] getAlunoTurmaFromConsole() {
        String cpf = getPessoaSelecionadaFromConsole();
        String idTurma = getTurmaSelecionadaFromConsole();
        return new String[]{cpf, idTurma};
    }

    private static String[] bindDataFromConsole(String prefixo, String identificador) throws Exception {
        switch (prefixo) {
            case "PESSOA_ALUNO": return bindAlunoFromConsole(identificador);
            case "PESSOA_PROFESSOR": return bindProfessorFromConsole(identificador);
            case "TURMA": return bindTurmaFromConsole(identificador);
            default: return null;
        }
    }

    private static String[] bindAlunoFromConsole(String cpf) throws Exception {
        String[] dadosPessoa = bindPessoaFromConsole(cpf);

        System.out.println("Matrícula: ");
        String matricula = scan.next();

        String[] dados = Arrays.copyOf(dadosPessoa, dadosPessoa.length + 1);
        dados[dadosPessoa.length] = matricula;
        return dados;
    }

    private static String[] bindProfessorFromConsole(String cpf) throws Exception {
        String[] dadosPessoa = bindPessoaFromConsole(cpf);

        System.out.println("Área de Estudo: ");
        String areaEstudo = scan.next();

        String[] dados = Arrays.copyOf(dadosPessoa, dadosPessoa.length + 1);
        dados[dadosPessoa.length] = areaEstudo;
        return dados;
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

    private static String[] bindTurmaFromConsole(String codigo) throws Exception {
        if (codigo.isBlank()) {
            System.out.println("Codigo: ");
            codigo = scan.next();
        }

        System.out.println("Quantidade de alunos: ");
        String qtdAlunos = scan.next();

        if (codigo.isBlank() || qtdAlunos.isBlank()) {
            throw new Exception("Dados incompletos de Pessoa!");
        }

        return new String[]{codigo, codigo, qtdAlunos};
    }

}
