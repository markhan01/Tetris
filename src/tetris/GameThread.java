package tetris;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread {
    
    private GameArea ga;
    private GameForm gf;
    private int score;
    private int level = 1;
    private int scorePerLevel = 30;
    private int pause = 500;
    private int speedUpPerLevel = 50;
    private String difficulty;
    private int scoreAddition = 10;
    private int scoreMultiplier = 15;
    
    public GameThread(GameArea ga, GameForm gf) {
        this.gf = gf;
        this.ga = ga;
    }
    
    @Override
    public void run() {
        while (true) {
            this.difficulty = gf.getDifficulty();
            if (difficulty.equals("Easy")) {
                scoreAddition = 10;
                scoreMultiplier = 15;
                ga.spawnBlock();
            } else if (difficulty.equals("Medium")) {
                scoreAddition = 15;
                scoreMultiplier = 20;
                ga.randomRotationSpawn();
            } else if (difficulty.equals("Hard")) {
                scoreAddition = 20;
                scoreMultiplier = 25;
                ga.randomRotationAndLocationSpawn();
            }
            
            while (ga.moveBlockDown()) {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (ga.isBlockOutOfBounds()) {
                System.out.println("Game Over");
                break;
            }
            
            ga.moveBlockToBackground();
            
            // Update score based on number of lines cleared
            int linesCleared = ga.clearLines();
            if (linesCleared == 1) {
                score += scoreAddition;   
            } else if (linesCleared > 1) {
                score += (linesCleared * scoreMultiplier);
            }
            gf.updateScore(score);
            
            // Increase level if score threshold is reached
            int lvl = score / scorePerLevel + 1;
            if (lvl > level) {
                int difference = lvl - level;
                level = lvl;
                gf.updateLevel(level);
                if (difference > 1) {
                    pause -= (speedUpPerLevel * difference);
                    if (pause <= 100) pause = 100;
                } else {
                    pause -= speedUpPerLevel;
                    if (pause <= 100) pause = 100;
                }
            }
        }
    }
    
}
