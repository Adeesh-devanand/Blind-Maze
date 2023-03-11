package ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import model.Game;

import java.io.IOException;

public class Controller {
    private Game game;
    private Screen screen;
    private boolean inMenu;
    private WindowBasedTextGUI textGUI;

    public Controller(Screen screen) {
        inMenu = true;
        game = new Game();
        this.screen = screen;
    }

    public boolean isGameRunning() {
        return game.isGameRunning();
    }

    public void updateGame(KeyStroke stroke) {
        if (stroke == null) {
            return;
        }

        String ch = stroke.getCharacter().toString().toLowerCase();
        switch (ch) {
            case "c":
            case "o":
            case "q":
            case "t":
                break;
            default:
                game.updateMaze(ch);
        }
    }

    public void updateScreen() throws IOException {
        screen.setCursorPosition(new TerminalPosition(0, 0));
        screen.clear();
        render();
        screen.refresh();
    }

    private void render() {
        if (inMenu) {
            drawMenu();
        } else {
            drawMaze();
        }
    }

    private void drawMenu() {
    }

    private void drawMaze() {
    }
}
