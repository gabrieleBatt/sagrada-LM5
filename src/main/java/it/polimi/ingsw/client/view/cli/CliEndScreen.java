package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.EndGameInfo;
import it.polimi.ingsw.shared.Message;
import it.polimi.ingsw.client.view.factory.EndScreen;
import javafx.util.Pair;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * CliEndScreen is a concrete class whose aim is to show the final ranking
 * (LeaderBoard) by CLI at the end of the game.
 */
public class CliEndScreen extends EndScreen {

    private static final int CLEAR_SPACE = 20;
    private final Scanner scanner;
    private final PrintStream printStream;

    public CliEndScreen(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.printStream = out;
    }

    /**
     * Shows final LeaderBoard
     * @param endGameInfo info to show at end of game
     */
    public void showRanking(EndGameInfo endGameInfo){
        List<Pair<String,Integer>> ranking = endGameInfo.getRanking();
        clearScreen();
        printStream.println(Message.LEADER_BOARD+":\n");
        ranking.forEach(p->printStream.println(p.getKey()+": "+p.getValue()+"\n"));
        printStream.println("\n" + ranking.get(0).getKey() +" "+Message.WINS+"!\n");
        printStream.println(Message.END_GAME_MESSAGE);
        scanner.nextLine();
    }

    /**
     * Cleans user screen.
     */
    private void clearScreen() {
        for (int i = 0; i < CLEAR_SPACE; i++) {
            printStream.println();
        }
    }

    /**
     * Asks player whether wants to play again or not.
     * @return Boolean, true if payer chooses to play again.
     */
    public boolean playAgain(){
        String answer;
        clearScreen();
        printStream.println(Message.REPLAY+"?\n   **"+Message.YES+"**  **"+Message.NO+"**   \n");
        answer = scanner.nextLine();
        while (!(answer.equalsIgnoreCase(Message.YES.toString()) || answer.equalsIgnoreCase(Message.NO.toString()))){
            printStream.println(Message.INVALID_CHOICE);
            answer = scanner.nextLine();
        }
        return answer.equalsIgnoreCase(Message.YES.toString());
    }
}
