package at.synchess.boardsoftware.front.model.timers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

import java.util.concurrent.Callable;

public abstract class Timer {
    private final IntegerProperty secondsLeft = new SimpleIntegerProperty();
    private Callable onTimeout;
    private final Timeline tl;

    public Timer(int startingSecs, Callable onTimeout){
        secondsLeft.setValue(startingSecs);
        this.onTimeout = onTimeout;

        tl = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsLeft.add(1);
        }));
    }

    public void tick() throws Exception {
        secondsLeft.subtract(1);
        if (secondsLeft.getValue() == 0)
            onTimeout.call();
    }

    public int startTicking(
            Timeline t
    ){return 0;}

    public void setOnTimeout(Callable c){
        onTimeout = c;
    }





}
