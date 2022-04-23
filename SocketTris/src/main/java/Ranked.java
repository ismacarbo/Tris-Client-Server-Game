/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author ismaele.carbonari
 */
public class Ranked {

    private String[] rank = {"ferro 1", "ferro 2", "ferro 3", "bronzo 1", "bronzo 2", "bronzo 3", "argento 1"
    ,"argento 2","argento 3","oro 1","oro 2","oro 3","PLATINO"};
    private int nVittorie;
    private String rankAttuale;
    private int index;
    private String nome;
    private ArrayList<String> rankes;

    public Ranked(String nome) throws Exception {
        nVittorie = 0;
        rankAttuale = "";
        this.nome = nome;
        rankes = new ArrayList<>();
        creaFile();
        inserisciInArrayList();
        String[] split = rankes.get(trovaNome()).split("\\.");
        index = Integer.parseInt(split[0]);
        if(index==rank.length-1){
            index=rank.length-1;
        }
    }

    public void aumentaRank() throws Exception {
        nVittorie++;
        if (nVittorie == 3) {
            index++;
            System.out.println(index);
            rankAttuale = rank[index];
            nVittorie = 0;
            scrivi();
            System.out.println("Congratulazioni hai raggiunto il rank: " + rankAttuale + "!");
        }
    }

    private void inserisciInArrayList() throws FileNotFoundException, Exception {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(creaFile()));
            String line = bf.readLine();
            while (line != null) {
                rankes.add(line);
                line = bf.readLine();
            }
            bf.close();
        } catch (IOException ex) {
            throw new Exception("Ricontrollare il file!");
        }
    }

    public void diminuisciRank() {
        if (nVittorie > 0) {
            nVittorie--;
        } else if (nVittorie == 0) {
            nVittorie = 0;
        }

    }

    public void leggi() throws Exception {
        String out = "";
        try {
            BufferedReader lettore = new BufferedReader(new FileReader(creaFile()));
            String riga = lettore.readLine();
            while (riga != null) {
                riga = lettore.readLine();
            }
            lettore.close();
        } catch (IOException ex) {
            throw new Exception("File non trovato!");
        }
    }

    private String creaFile() throws Exception {
        String direzione = "";
        try {
            direzione = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
            File f = new File(direzione.concat("\\output.txt"));
            f.createNewFile();
        } catch (IOException ex) {
            throw new Exception("Percorso inesistente!");
        }
        return direzione.concat("\\output.txt");
    }

    private void scrivi() throws Exception {
        try {
            BufferedWriter g = new BufferedWriter(new FileWriter(creaFile(), true));
            g.write(index + ".index,Nome: " + nome + ", Rank: " + rankAttuale);
            g.newLine();
            rankes.add(index + ".index Nome: " + nome + " Rank: " + rankAttuale);
            g.close();
        } catch (IOException ex) {
            throw new Exception("File non trovato!");
        }
    }

    private int trovaNome() throws Exception {
        int out = 0;
        for (int i = 0; i < rankes.size(); i++) {
            if (rankes.get(i).contains(nome)) {
                out = i;
            }
        }
        return out;
    }

}
