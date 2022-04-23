
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ismaele Carbonari
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        //SERVER
        try {
            GestisciSocket g = new GestisciSocket(6789);
            g.accettaRichiesta();

            System.out.println("Inserisci il tuo nome: ");
            String nome = scan.nextLine();
            System.out.println("INSERISCI COORDINATE DA 0 A 2!");
            System.out.println("TU HAI LA X");
            Game game = new Game(nome);

            System.out.println("Volete giocare la modalita competiva(1) o normale(2)?");
            int scelta=scan.nextInt();
            if(scelta==1){
                game.giocaServer(g,true);
            }else{
                game.giocaServer(g,false);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
