package ui;

import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.exceptions.MazeAlreadyExistsException;
import ui.exceptions.MazeDoesNotExistException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GuiInterface extends JFrame implements ActionListener {
    private Game game;

    private final JPanel cards;
    private final JTextField createField = new JTextField(15);
    private final JTextField openField = new JTextField(15);

    private static final String JSON_STORE = "./data/workspace.json";
    private static final JsonReader reader = new JsonReader(JSON_STORE);
    private static final JsonWriter writer = new JsonWriter(JSON_STORE);

    //Effects: Creates a GUI and links it with the game
    public GuiInterface() {
        super("Blind Maze");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleClosing();
            }
        });

        setPreferredSize(new Dimension(450, 450));
        setLayout(new FlowLayout());

        this.game = new Game();

        cards = new JPanel(new CardLayout());
        cards.setFocusable(true);
        assignNewKeyListener(cards);

        cards.add(initializeMain(), "main");
        cards.add(initializeCreate(), "create");
        cards.add(initializeOpen(), "open");
        cards.add(new JPanel(), "game");
        add(cards);

        ((CardLayout)(cards.getLayout())).show(cards, "main");

        pack();
        setVisible(true);
    }

    //Modifies: component, game
    //Effects:  attaches a keyListener to the component which modifies the game appropriately
    private void assignNewKeyListener(Component component) {
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e);
            }
        });
    }

    //Modifies: game
    //Effects:  updates game appropriately,
    @SuppressWarnings("methodlength")
    private void handleKeyPressed(KeyEvent e) {
        if (game.isRunning()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    game.updateMaze("r");
                    break;
                case KeyEvent.VK_LEFT:
                    game.updateMaze("l");
                    break;
                case KeyEvent.VK_UP:
                    game.updateMaze("u");
                    break;
                case KeyEvent.VK_DOWN:
                    game.updateMaze("d");
                    break;
                case KeyEvent.VK_O:
                    game.updateMaze("obstacle");
                    break;
                case KeyEvent.VK_P:
                    game.updateMaze("player");
                    break;
                case KeyEvent.VK_M:
                    game.updateMaze("monster");
                    break;
                case KeyEvent.VK_Q:
                    game.updateMaze("q");
                    ((CardLayout)(cards.getLayout())).show(cards, "main");
                    return;
            }
            drawGame();
        }
    }

    //Effects: handles saving of file when closed
    private void handleClosing() {
        String[] buttonLabels = {"Yes", "No", "Cancel"};
        String defaultOption = buttonLabels[0];
        int choice =  JOptionPane.showOptionDialog(this,
                "Do you want to save before exiting?",
                "Warning",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                buttonLabels,
                defaultOption);

        if (choice == JOptionPane.YES_OPTION) {
            save();
            System.exit(1);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    //Effects: initializes the Main page
    private JPanel initializeMain() {
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(13, 3));

        JLabel title = new JLabel("     Blind Maze");
        title.setBorder(BorderFactory.createBevelBorder(0));
        main.add(title);
        main.add(new JPanel());

        String[] btnNameArr = {"NewGame", "OpenGame", "ToggleMode", "LoadGame", "ExitGame"};

        for (String btnName: btnNameArr) {
            JButton newGame = new JButton(btnName);
            newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
            newGame.setActionCommand(btnName);
            newGame.addActionListener(this);
            main.add(new JPanel());
            main.add(newGame);
        }
        return main;
    }

    //Effects: initializes the Create page
    private JPanel initializeCreate() {
        return initializeTextAndButton(createField, "Enter a name for the maze", "CreateMaze");
    }

    //Effects: initializes the Open page
    private JPanel initializeOpen() {
        return initializeTextAndButton(openField, "Enter the name of the maze you want to open", "OpenMaze");
    }

    //Effects: initializes a JPanel with a textField, prompt and button
    private JPanel initializeTextAndButton(JTextField textField, String prompt, String btnName) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(13, 3));

        panel.add(new JPanel());
        JLabel title = new JLabel(prompt);
        panel.add(title);

        panel.add(new JPanel());
        panel.add(textField);

        JButton open = new JButton(btnName);
        open.setActionCommand(btnName);
        open.addActionListener(this);
        panel.add(new JPanel());
        panel.add(open);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "NewGame":
                ((CardLayout)(cards.getLayout())).show(cards, "create");
                break;
            case "CreateMaze":
                createMaze();
                break;
            case "OpenGame":
                openGame();
                break;
            case "OpenMaze":
                openMaze();
                break;
            case "ToggleMode":
                toggleMode();
                break;
            case "LoadGame":
                loadSession();
                break;
            case "ExitGame":
                handleClosing();
                break;
        }
    }

    //Modifies: this, game
    //Effects:  toggles the game mode and shows the current mode
    private void toggleMode() {
        game.toggleMode();
        JOptionPane.showMessageDialog(this, "Game currently in " + game.getMode());
    }

    //Modifies: this, game
    //Effects: creates a maze and returns the user to the main screen
    //         throws an error prompt if the maze name already exists
    private void createMaze() {
        try {
            game.createMaze(createField.getText());
            ((CardLayout)(cards.getLayout())).show(cards, "main");
        } catch (MazeAlreadyExistsException e) {
            JOptionPane.showMessageDialog(this, "Maze already exists", "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Modifies: this
    //Effects: takes the user to the open maze page
    //         throws an error prompt if no mazes exists
    private void openGame() {
        if (game.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Create a Maze First", "Warning", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ((CardLayout)(cards.getLayout())).show(cards, "open");
    }

    //Modifies: this, game
    //Effects:  creates a maze and returns the user to the main screen
    //          throws an error prompt if maze within given name doesn't exist
    private void openMaze() {
        try {
            game.selectMaze(openField.getText());
            ((CardLayout)(cards.getLayout())).show(cards, "game");
            drawGame();
        } catch (MazeDoesNotExistException e) {
            JOptionPane.showMessageDialog(this, "Maze Doesn't Exist", "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Requires: the game should be running
    //Modifies: this
    //Effects:  draws the maze on the screen
    private void drawGame() {
        String[][] gridToBeDisplayed =  game.getGrid();
        int gridSize = gridToBeDisplayed.length;
        JPanel gamePanel = (JPanel) cards.getComponent(3);
        gamePanel.removeAll();
        gamePanel.setLayout(new GridLayout(gridSize, gridSize));

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++)  {
                String type = gridToBeDisplayed[gridSize - (row + 1)][column];
                gamePanel.add(new JLabel(getIcon(type)), row, column);
            }
        }

        gamePanel.revalidate();
        gamePanel.repaint();
        ((CardLayout)(cards.getLayout())).show(cards, "game");
    }

    //Effects: returns the icon for the given Element type
    private ImageIcon getIcon(String type) {
        String path = "/data/" + type + ".png";
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    //Modifies: this
    //Effects: loads the game from JSON_STORE
    private void loadSession() {
        try {
            game = reader.loadGame();
            System.out.println("Loaded Previous Session from " + JSON_STORE);
            if (game.isRunning()) {
                ((CardLayout)(cards.getLayout())).show(cards, "game");
                drawGame();
            }
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //Effects: saves the game to JSON_STORE
    public void save() {
        try {
            writer.open();
            writer.writeGame(game);
            writer.close();
            System.out.println("Saved game to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}
