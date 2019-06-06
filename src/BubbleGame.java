// the main file for the game; where everything is launched
package bubblegame;
import sun.audio.*;

public class BubbleGame {

    public BubbleGame(int level) {
        // level1: 3*7, 3; level2: 4*7, 4; level3: 5*7, 5 colors
    }

    public static void main(String[] args) {
        Audios audios = new Audios();
        Index indexPage = new Index(audios);
        // always play this music
        try {
            AudioStream audioBgm = new AudioStream(BubbleGame.class.getResourceAsStream("sound/bgm.wav"));
            AudioPlayer.player.start(audioBgm);
        } catch(Exception e) {
            e.printStackTrace();
        }
        //GameInterface gameInterface = new GameInterface(0, indexaPage);
    }
}