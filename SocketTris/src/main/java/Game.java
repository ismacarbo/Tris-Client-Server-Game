import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private char[][] board;
    private String nome;
    private int turno;
    private Ranked rank;

    public Game(String name) throws Exception {
        board = new char[3][3];
        inizializzaGriglia();
        System.out.println(toString());
        turno = new Random().nextInt(2);
        rank = new Ranked(name);
    }

    private void inizializzaGriglia() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = '-';
            }
        }
    }

    public String stampaGriglia() {
        String out = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                out += board[i][j] + "\t";
            }
            out += "\n";
        }
        return out;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    private boolean checkWinner(char value) {
        Boolean win = null;
        //righe
        for (int i = 0; i < board.length; i++) {
            win = true;
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != value) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return win;
            }
        }

        //colonne
        for (int i = 0; i < board.length; i++) {
            win = true;
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != value) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return win;
            }
        }

        //diagonali
        win = true;
        for (int i = 0; i < board.length; i++) {
            if (board[i][i] != value) {
                win = false;
                break;
            }
        }
        if (win) {
            return win;
        }
        win = true;
        for (int i = 0; i < board.length; i++) {
            if (board[i][board.length - 1 - i] != value) {
                win = false;
                break;
            }
        }
        if (win) {
            return win;
        }
        return false;
    }

    private boolean checkCell(char value, int x, int y) {
        return board[x][y] == value;
    }

    public void ricominciaClient(GestisciSocket giocatore1,boolean comp) throws IOException, Exception {
        inizializzaGriglia();
        giocaClient(giocatore1,comp);
    }

    public void ricominciaServer(GestisciSocket giocatore1,boolean comp) throws IOException, Exception {
        inizializzaGriglia();
        giocaServer(giocatore1,comp);
    }

    public void giocaClient(GestisciSocket client,boolean comp) throws IOException, ArrayIndexOutOfBoundsException, Exception {
        Scanner scan = new Scanner(System.in);
        boolean win = false;
        int cont = 0;
        while (!win) {
            turno = 0;
            if (turno == 0) {
                System.out.println(stampaGriglia());
                int x = 0;
                int y = 0;
                if (cont != 0) {
                    String[] split = reciveCoordinate(client).split("\\,");
                    board[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 'X';
                }

                if (checkWinner('X')) {
                    System.out.println("Hai perso!");
                    if(comp){
                        rank.diminuisciRank();
                    }
                    
                    System.out.println("Volete ricominciare(si/no)");
                    String risposta = scan.next();
                    if (risposta.equals("si")) {
                        while (risposta.equals("si")) {
                            ricominciaClient(client,comp);
                            giocaClient(client,comp);
                        }
                    } else {
                        System.out.println("Arrivederci!");
                        System.exit(0);
                    }

                } else if (draw()) {
                    System.out.println("Avete pareggiato!");
                    System.out.println("Volete ricominciare?(si/no)");
                    String risposta = scan.next();
                    if (risposta.equals("si")) {
                        while (risposta.equals("si")) {
                            ricominciaClient(client,comp);
                            giocaClient(client,comp);
                        }
                    } else {
                        System.out.println("Arrivederci!");
                        System.exit(0);
                    }

                }

                System.out.println(stampaGriglia());
                System.out.println("Inserisci la riga per il tuo turno: ");
                x = scan.nextInt();
                System.out.println("Inserisci la colonna per il tuo turno: ");
                y = scan.nextInt();

                if (checkCell('X', x, y)) {
                    while (checkCell('X', x, y)) {
                        System.out.println("In quella cella c'è già l'avversario, selezionane un'altra!");
                        System.out.println("Inserisci la riga per il tuo turno: ");
                        x = scan.nextInt();
                        System.out.println("Inserisci la colonna per il tuo turno: ");
                        y = scan.nextInt();
                    }
                } else if (checkCell('O', x, y)) {
                    while (checkCell('O', x, y)) {
                        System.out.println("Hai già selezionato quella cella,provane un'altra!");
                        System.out.println("Inserisci la riga per il tuo turno: ");
                        x = scan.nextInt();
                        System.out.println("Inserisci la colonna per il tuo turno: ");
                        y = scan.nextInt();
                    }
                }

                board[x][y] = 'O';
                sendCoordinate(x, y, client);
                if (checkWinner('O')) {
                    System.out.println("Hai vinto!");
                    if(comp){
                       rank.aumentaRank(); 
                    }
                    
                    System.out.println(stampaGriglia());
                    win = true;
                } else if (draw()) {

                    System.out.println("Avete pareggiato!");
                    System.out.println("Volete ricominciare?(si/no)");

                    String risposta = scan.next();
                    if (risposta.equals("si")) {
                        while (risposta.equals("si")) {
                            ricominciaClient(client,comp);
                            giocaClient(client,comp);
                        }
                    } else {
                        System.out.println("Arrivederci!");
                        System.exit(0);
                    }

                } else {
                    if (checkWinner('X')) {
                        System.out.println("Hai perso!");
                        if(comp){
                            rank.diminuisciRank();
                        }
                        
                        System.out.println("Volete ricominciare(si/no)");
                        String risposta = scan.next();
                        while (risposta.equals("si")) {
                            ricominciaClient(client,comp);
                            giocaClient(client,comp);
                        }
                    }
                }

                if (!win) {
                    System.out.println(stampaGriglia());
                    System.out.println("E' il turno del tuo avversario!");
                    turno = 1;

                } else {
                    System.out.println("Volete ricominciare(si/no)");
                    String risposta = scan.next();
                    if (risposta.equals("si")) {
                        while (risposta.equals("si")) {
                            ricominciaClient(client,comp);
                            giocaClient(client,comp);
                        }
                    } else {
                        System.out.println("Arrivederci!");
                        System.exit(0);
                    }

                }
            }
            cont++;
        }
    }

    private void sendCoordinate(int x, int y, GestisciSocket op) throws IOException {
        op.scrivi(String.valueOf(x) + "," + String.valueOf(y));
    }

    private String reciveCoordinate(GestisciSocket op) throws IOException {
        return op.leggi();
    }

    public void giocaServer(GestisciSocket server,boolean comp) throws IOException, ArrayIndexOutOfBoundsException, Exception {
        Scanner scan = new Scanner(System.in);
        boolean win = false;
        while (!win) {
            turno = 1;
            if (turno == 1) {
                System.out.println(stampaGriglia());
                int x = 0;
                int y = 0;
                String[] split = reciveCoordinate(server).split("\\,");
                board[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = 'O';

                if (checkWinner('O')) {
                    System.out.println("Hai perso!");
                    if(comp){
                        rank.diminuisciRank();
                    }
                    
                    System.out.println("Volete ricominciare(si/no)");
                    String risposta = scan.next();
                    if (risposta.equals("si")) {
                        while (risposta.equals("si")) {
                            ricominciaServer(server,comp);
                            giocaServer(server,comp);
                        }
                    } else {
                        System.out.println("Arrivederci!");
                        System.exit(0);
                    }

                } else if (draw()) {
                    System.out.println("Avete pareggiato!");
                    System.out.println("Volete ricominciare?(si/no)");
                    String risposta = scan.next();
                    if (risposta.equals("si")) {
                        while (risposta.equals("si")) {
                            ricominciaServer(server,comp);
                            giocaServer(server,comp);
                        }
                    } else {
                        System.out.println("Arrivederci!");
                        System.exit(0);
                    }

                }

                System.out.println(stampaGriglia());
                System.out.println("Inserisci la riga per il tuo turno: ");
                x = scan.nextInt();
                System.out.println("Inserisci la colonna per il tuo turno: ");
                y = scan.nextInt();
                if (checkCell('O', x, y)) {
                    while (checkCell('O', x, y)) {
                        System.out.println("In quella cella c'è già l'avversario, selezionane un'altra!");
                        System.out.println("Inserisci la riga per il tuo turno: ");
                        x = scan.nextInt();
                        System.out.println("Inserisci la colonna per il tuo turno: ");
                        y = scan.nextInt();
                    }
                } else if (checkCell('X', x, y)) {
                    while (checkCell('X', x, y)) {
                        System.out.println("Hai già selezionato quella cella,provane un'altra!");
                        System.out.println("Inserisci la riga per il tuo turno: ");
                        x = scan.nextInt();
                        System.out.println("Inserisci la colonna per il tuo turno: ");
                        y = scan.nextInt();
                    }
                }
                board[x][y] = 'X';
                sendCoordinate(x, y, server);
                if (checkWinner('X')) {
                    System.out.println("Hai vinto!");
                    if(comp){
                        rank.aumentaRank();
                    }
                    
                    System.out.println(stampaGriglia());
                    win = true;
                } else if (draw()) {
                    System.out.println("Avete pareggiato!");
                    System.out.println("Volete ricominciare?(si/no)");
                    String risposta = scan.next();
                    if (risposta.equals("si")) {
                        while (risposta.equals("si")) {
                            ricominciaServer(server,comp);
                            giocaServer(server,comp);
                        }
                    } else {
                        System.out.println("Arrivederci!");
                        System.exit(0);
                    }

                } else {
                    if (checkWinner('O')) {
                        System.out.println("Hai perso!");
                        if(comp){
                            rank.diminuisciRank();
                        }
                        
                        System.out.println("Volete ricominciare(si/no)");
                        String risposta = scan.next();
                        if (risposta.equals("si")) {
                            while (risposta.equals("si")) {
                                ricominciaServer(server,comp);
                                giocaServer(server,comp);
                            }
                        } else {
                            System.out.println("Arrivederci!");
                            System.exit(0);
                        }

                    }
                }

                if (!win) {
                    System.out.println(stampaGriglia());
                    System.out.println("E' il turno del tuo avversario!");
                    turno = 0;
                } else {
                    System.out.println("Volete ricominciare(si/no)");
                    String risposta = scan.next();
                    if (risposta.equals("si")) {
                        while (risposta.equals("si")) {
                            ricominciaServer(server,comp);
                            giocaServer(server,comp);
                        }
                    } else {
                        System.out.println("Arrivederci!");
                        System.exit(0);
                    }

                }
            }
        }
    }

    private boolean draw() {
        int conta = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != '-') {
                    conta++;
                }
            }
        }
        if (conta == 9) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "turno: " + turno;
    }
}