package it.polimi.ingsw.am54;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class animationTimer extends AnimationTimer {
    private GraphicsContext gc;
    private double width, height;
    private final long startNanoTime;

    public animationTimer(GraphicsContext gc, double width, double height) {
        this.gc = gc;
        this.width = height;
        this.height = width;
        this.startNanoTime = System.nanoTime();
    }

    @Override
    public void handle(long now) {
        double timeSec = (now - startNanoTime) >> 19;
        double start = Math.sin(timeSec/2000)*300, length = Math.sin(timeSec/7000)*300;
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.BLUE);
        gc.fillArc(width/2 - 50, height/2 - 50, 100, 100, start, length, ArcType.ROUND);
        gc.setFill(Color.BLACK);
        gc.fillArc(width/2 - 45, height/2 - 45, 90, 90, 0, 360, ArcType.ROUND);
    }
}
