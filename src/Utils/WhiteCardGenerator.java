package Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WhiteCardGenerator {

    private ArrayList<String> cards;
    private static final String WHITE_CARDS = "whiteCards.txt";
    private Random random;

    public WhiteCardGenerator(){
        this.cards = new ArrayList<>();
        this.random = new Random(System.currentTimeMillis());
        addCards();
    }

    private void addCards(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(WHITE_CARDS));
        }catch (IOException io){
            io.printStackTrace();
        }

        while (scanner.hasNext()){
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
