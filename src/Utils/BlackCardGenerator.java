package Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BlackCardGenerator {

    private ArrayList<String> cards;
    private static final String BLACK_CARDS = "blackCards.txt";
    private Random random;

    public BlackCardGenerator(){
        this.cards = new ArrayList<>();
        this.random = new Random(System.currentTimeMillis());
        addCards();
    }

    private void addCards(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(BLACK_CARDS));
        }catch (IOException io){
            io.printStackTrace();
        }

        while (scanner.hasNextLine()){
            cards.add(scanner.nextLine());
        }
        System.out.println(cards.size());
    }

    /**
     * Gets a random card.
     * @return
     */
    public String getRandomCard(){
        return cards.get(random.nextInt(cards.size()));
    }





}
