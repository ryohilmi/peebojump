package Utility;

import java.util.Timer;
import java.util.TimerTask;

public class Time {
    private int curr_time;
    private int second;
    private int milisecond;
    public Time() {
        curr_time = 0;
        second = 0;
        milisecond = 0;
    }

    public void start() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask()
                {
                    public void run()
                    {
                        ++curr_time;
                        milisecond = curr_time % 100;
                        if(curr_time % 100 == 0){
                            second = curr_time / 100;
                        }
                    }
                },0, 10);
    }

    public int getSecond() {
        return second;
    }

    public int getMilisecond() {
        return milisecond;
    }

    public void bonusTime(int t) {
        if (second < t) second = 0;
        else second -= t;
    }
}
