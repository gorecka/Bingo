package example;

import jade.core.Agent;

public class ExampleAgent extends Agent {
    protected void setup() {
        System.out.println("Cześć, tu " + getAID().getName() + " !");

//        addBehaviour(new example.MyCyclicBehaviour());
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        addBehaviour(new example.MyCyclicBehaviour());
        //doDelete();

        addBehaviour(new MyOneShotBehaviour(this));
//        addBehaviour(new example.MyOneShotBeaviour(this));
//        addBehaviour(new example.MyOneShotBeaviour(this));
        System.out.println("Koniec setup");
    }

    protected void takeDown() {
        System.out.println("Do widzenia (agent " + getAID().getName() + " )");
    }
}

