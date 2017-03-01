package Sudoku;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {

    private static final int TILE_SIZE = 500 / 8;
    private static final int X_TILES = 4;
    private static final int Y_TILES = 4;
    private static final List<Integer> numbers = new ArrayList<>();
    private static List<Tile> cells = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        numbers.add(0);
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);

        primaryStage.setTitle("Sudoku generator");
        Pane root = new Pane();
        root.setPrefSize(500, 500);
        Tile[][] grid = new Tile[X_TILES][Y_TILES];
        for (int x = 0; x < Y_TILES; x++) {
            for (int y = 0; y < X_TILES; y++) {

                Tile tile = new Tile(x, y, 0);
                grid[x][y] = tile;
                tile.text.setText("x:" + x + " y:" + y);

                root.getChildren().add(tile);
                if (x <= 1 && y <= 1) {
                    tile.setGroup("sw");
                } else if (x >= 2 && y >= 2) {
                    tile.setGroup("ne");
                } else if (x <= 1 && y >= 2) {
                    tile.setGroup("nw");
                } else if (x >= 2 && y <= 1) {
                    tile.setGroup("se");
                }
                cells.add(tile);

            }
        }

        while (generateBoard()) {
            cells.stream().forEach(cellText -> cellText.text.setText("?"));
        }

        removeNumbersOfFinishedBoard();

        primaryStage.setScene(new Scene(root, 500, 500));

        primaryStage.show();
    }

    private void removeNumbersOfFinishedBoard() {

    }

    private boolean generateBoard() {
        for (Tile cell : cells) {
            boolean loop = true;
            int number = -1;
            List<Integer> loopNumbers = new ArrayList<>(numbers);
            while (loop) {
                if (loopNumbers.size() > 0) {
                    int randomIndex = ThreadLocalRandom.current().nextInt(loopNumbers.size());
                    number = loopNumbers.get(randomIndex);
                    loop = checkIfAllowed(number, cell);
                    if (loop == true) {
                        loopNumbers.remove(loopNumbers.indexOf(number));
                    }
                } else {
                    System.out.println("något gick snett på cell x: " + cell.x + " y: " + cell.y);
                    loop = false;
                    return true;
                }
            }
            cell.text.setText(String.valueOf(number));
        }
        return false;
    }

    private boolean checkIfAllowed(int number, Tile cell) {
        if (cells.stream().filter(sw -> sw.group.equalsIgnoreCase(cell.group)).anyMatch(x -> x.text.getText().equalsIgnoreCase(String.valueOf(number)))) {
            return true;
        }
        //check X row
        if (cells.stream().filter(cellX -> cellX.x == cell.x).anyMatch(myCell -> myCell.text.getText().equalsIgnoreCase(String.valueOf(number)))) {
            return true;
        }
        //check Y row
        if (cells.stream().filter(cellY -> cellY.y == cell.y).anyMatch(myCell -> myCell.text.getText().equalsIgnoreCase(String.valueOf(number)))) {
            return true;
        }

        return false;
    }


    public static void main(String[] args) {
        launch(args);
    }

    private class Tile extends StackPane {
        private int x, y;
        private Text text = new Text();
        private String group;

        private Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);

        public Tile(int x, int y, int val) {
            this.x = x;
            this.y = y;
            this.text.setFill(Color.BLACK);
            this.text.setText(String.valueOf(val));
            this.border.setFill(Color.WHITE);
            this.border.setStroke(Color.LIGHTGRAY);

            getChildren().addAll(border, text);

            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }
}
