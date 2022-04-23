import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe che gestisce il socket per la comunicazione client - server in pratica
 * se viene inserito un ip non corretto viene creato un server al momento e la
 * prossima connessione avverrà su quell'ip
 *
 * @author Ismaele Carbonari
 */
public class GestisciSocket {

    private ServerSocket serverSocket;
    private static DataOutputStream output;
    private static DataInputStream input;
    private BufferedReader br;
    private Socket socket;
    private int porta;
    private String ip;

    /**
     * Costruttore non parametrizzato della classe per la gestione dei socket
     */
    public GestisciSocket() {
        serverSocket = null;
        output = null;
        input = null;
        br = null;
        porta = 0;
        ip = "";
        socket = null;
    }

    /**
     * Costruttore parametrizzato della classe per la gestione dei socket
     *
     * @param ip
     * @param porta
     */
    public GestisciSocket(String ip, int porta) throws IOException {
        this.ip = ip;
        this.porta = porta;
        this.serverSocket = null;
        this.input = null;
        this.output = null;
    }

    public GestisciSocket(int porta) {
        this.ip = "";
        this.porta = porta;
        this.serverSocket = null;
        this.input = null;
        this.output = null;
    }

    /**
     * Metodo che permette al server di accettare la richiesta di un client che
     * vuole connettersi
     */
    public void accettaRichiesta() {
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("In attesa della richiesta...");
            socket = serverSocket.accept();
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            System.out.println("CLIENT ACCETTATO!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo che permette di connettere un client a un server, nel caso esso
     * non venisse trovato diventa esso stesso un server ed è in grado di
     * accettare comunicazioni
     *
     * @return
     */
    public Boolean connetti() {
        try {
            socket = new Socket(ip, porta);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            return false;
        }
        System.out.println("Connesso col server");
        return true;
    }

    /**
     * Metodo che permette di inizializzare un server
     */
    public void inizializzaServer() {
        try {

            System.out.println("Server inizializzato correttamente!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che permette di leggere le informazioni in arrivo
     *
     * @return
     * @throws IOException
     */
    public String leggi() throws IOException {
        return input.readUTF();
    }

    /**
     * Metodo che permette di scrivere le informazioni
     *
     * @param message
     * @throws IOException
     */
    public void scrivi(String message) throws IOException {
        output.writeUTF(message);
        output.flush();
    }

}