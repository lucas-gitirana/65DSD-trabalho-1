package sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Máquina A
 *
 * <p>
 * Inicia um ServerSocket e aguarda conexão do cliente </p>
 * <p>
 * Ao receber a conexão, entra no modo de conversação no chat: </p>
 * <ul>
 * <li>Transmite a sentença digitada no prompt.</li>
 * <li>Aguarda o cliente enviar uma sentença de resposta e imprime.</li>
 * <li>Termina quando o usuário digitar sentença “exit”.</li>
 * </ul>
 *
 * @author Fernando Santos
 */
public class MaquinaA {

    public static void main(String[] args) throws IOException {
        /*
        * O Fernando está usando Linux. Neste sistema operacional, 
        * usuários que não não root/admin não conseguem fazer o bind 
        * em portas abaixo de 1024. 
        * Por este motivo, nesta implementação está utilizando 
        * a porta 65000 (uma porta aleatóriamente alta, que não deve
        * interferir em outros sistemas rodando na máquina).
         */
        ServerSocket server = new ServerSocket(65000);
        server.setReuseAddress(true);

        Scanner scan = new Scanner(System.in);
        scan.useDelimiter("\n"); // rodar via NetBeans precisa disso para identificar o <enter>

        Socket conn = null;

        try {
            System.out.println("Servidor iniciado. Aguardando conexão...");
            conn = server.accept();
            System.out.println("Conexão recebida.");
            OutputStream out = conn.getOutputStream();
            InputStream in = conn.getInputStream();

            boolean continuar = true;
            // ler sentença do usuário
            System.out.println("Digite mensagem para enviar: ");
            String sentenca = scan.next();
             // loop para continuar a execução enquanto
            // usuário não digitar 'exit' ou a conexão não for encerrada pelo outro usuário
            while (!sentenca.equals("exit") && continuar) {
                // enviar a sentença digitada pelo usuário
                out.write(sentenca.getBytes());

                // ler sentença recebida
                String mensagem = "";
                byte[] dados = new byte[1024];
                System.out.println("Aguardando mensagem.");
                do {
                    int qtdBytesLidos = in.read(dados);
                    if (qtdBytesLidos >= 0) {
                        // if necessário pois se a outra máquina fechar a conexão
                        // então in.read() vai retornar -1
                        String dadosStr = new String(dados, 0, qtdBytesLidos);
                        mensagem += dadosStr;
                    } else {
                        continuar = false;
                    }
                } while (in.available() > 0);
                // usando in.available() para repetir a leitura de bytes
                // enquanto houverem bytes disponíveis na stream.

                if (continuar) {
                    System.out.println("Mensagem recebido: " + mensagem);
                    System.out.println("Digite mensagem para enviar ('exit' para sair): ");
                    sentenca = scan.next();
                } else {
                    System.out.println("Chat encerrado pelo outro usuário.");
                }
            }
        } catch (Exception e) {
            System.out.println("Deu exception");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
                System.out.println("Socket encerrado.");
            }
            server.close();
            System.out.println("ServerSocket encerrado.");
        }
    }
}
