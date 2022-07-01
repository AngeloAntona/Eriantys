package it.polimi.ingsw.am54;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite implements movable{
    double x, y;
    String fname;
    ImageView imageView;

    public Sprite(double x, double y, String fname) {
        this.x = x;
        this.y = y;
        this.fname = fname;
        this.imageView = new ImageView(new Image(fname));
    }

    void updatePos(){
        imageView.setX(x);
        imageView.setY(y);
    }

    @Override
    public void moveDown() {
        this.y += 5;
        updatePos();
    }

    @Override
    public void moveLeft() {
        this.x -= 5;
        updatePos();
    }

    @Override
    public void moveRight() {
        this.x += 5;
        updatePos();
    }

    @Override
    public void moveUP() {
        this.x -= 5;
        updatePos();
    }
}
