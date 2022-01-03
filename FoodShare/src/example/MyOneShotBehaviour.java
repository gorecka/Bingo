package example;

import jade.core.behaviours.OneShotBehaviour;

public class MyOneShotBehaviour extends OneShotBehaviour {
    int counter = 0;
    static int globalCounter = 0;
    ExampleAgent exampleAgent;

    MyOneShotBehaviour(ExampleAgent receiverAgent){
        this.exampleAgent = receiverAgent;
    }

    @Override
    public void action() {
        System.out.println("wykonywanie example.MyOneShotBeaviour " + counter + ", global: " + globalCounter);
        ++counter;
        ++globalCounter;
        if (globalCounter == 3){
            exampleAgent.addBehaviour(new MyCyclicBehaviour());
        }
    }
}
