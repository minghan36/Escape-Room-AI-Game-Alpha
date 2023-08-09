package nz.ac.auckland.se206.Labels;

import javafx.animation.Timeline;

public class Timer {

    private Timeline timeline;
    private int minutes;
    private int seconds;

    public Timer (){
        minutes = 2;
        seconds = 0;
    }

    public String countdown(){
        if (seconds == 0){
            minutes--;
            seconds = 59;
        } else if (seconds > 0){
            seconds--;
        }
        return String.valueOf(minutes)+":"+String.valueOf(seconds);
    }

    

    
}
