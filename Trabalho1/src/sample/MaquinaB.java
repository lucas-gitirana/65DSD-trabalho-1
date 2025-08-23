package sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Máquina B
 *
 * <p>
 * Cria um Socket para conectar com a Máquina A </p>
 * <p>
 * Ao estabelecer conexão, entra no modo de conversação no chat: </p>
 * <ul>
 * <li>Recebe a sentença enviada pelo servidor.</li>
 * <li>Transmite a sentença digitada no prompt.</li>
 * <li>Termina quando o usuário digitar sentença “exit”.</li>
 * </ul>
 *
 * @author Fernando Santos
 */
public class MaquinaB {

    public static void main(String[] args) throws IOException {

        Scanner scan = new Scanner(System.in);
        scan.useDelimiter("\n"); // rodar via NetBeans precisa disso para identificar o <enter>

        Socket conn = null;

        try {
            conn = new Socket("127.0.0.1", 65000);
            System.out.println("Conexão estabelecida.");
            OutputStream out = conn.getOutputStream();
            InputStream in = conn.getInputStream();

            boolean continuar = true;
            // loop para continuar a execução enquanto
            // usuário não digitar 'exit'
            while (continuar) {
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
                        // para encerrar a execução caso a outra máquina
                        // fechou a conexão
                    }
                } while (in.available() > 0);
                // usando in.available() para repetir a leitura de bytes
                // enquanto houverem bytes disponíveis na stream.

                if (continuar) {
                    System.out.println("Recebido: " + mensagem);

                    // ler sentença do usuário
                    System.out.println("Digite mensagem para enviar (exit para sair): ");
                    String sentenca = scan.next();
                    if (!sentenca.equals("exit")) {
                        // enviar a sentença digitada pelo usuário
                        out.write(sentenca.getBytes());
                    } else {
                        continuar = false;
                    }
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
        }
    }
}
