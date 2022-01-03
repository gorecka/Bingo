package example;

import jade.core.behaviours.CyclicBehaviour;

import java.util.concurrent.TimeUnit;

public class MyCyclicBehaviour extends CyclicBehaviour {

    int counter = 0;

    @Override
    public void action() {
        System.out.println("wykonywanie myCyclicBehaviour " + counter);
        ++counter;
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("koniec action " );
    }
}