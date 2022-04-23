
import java.util.Scanner;

public class Main2 {
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //CLIENT
        try {
            GestisciSocket g = new GestisciSocket("localhost", 6789);
            if (g.connetti()) {
                System.out.println("connesso");
            }
            

            System.out.println("INSERISCI COORDINATE DA 0 A 2!");
            System.out.println("TU HAI LA O");
            
            System.out.println("Inserisci il tuo nome: ");
            String nome=scan.nextLine();
            Game game = new Game(nome);
            
            System.out.println("Volete giocare la modalita competiva(1) o normale(2)?");
            int scelta=scan.nextInt();
            if(scelta==1){
                game.giocaClient(g,true);
            }else{
                game.giocaClient(g,false);
            }
            


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
