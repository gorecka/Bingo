package receiver;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForReviewForm extends CyclicBehaviour {
    ReceiverAgent receiver;

    WaitForReviewForm(ReceiverAgent agent) {
        receiver = agent;
    }

    @Override
    public void action() {
        System.out.println("Agent " + receiver.getAID().getName() + " czekam na ankietę");

        // czekanie na wiadomość, która pasuje do wzorca
        MessageTemplate mtPerformative = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mtOntology = MessageTemplate.MatchOntology("Reviewing-form-ontology");
        MessageTemplate mt = MessageTemplate.and(mtPerformative, mtOntology);
        ACLMessage message = receiver.receive(mt);

        if (message != null) {
            System.out.println("Agent " + receiver.getAID().getName() + " otrzymal wiadomość ");

            receiver.addBehaviour(new SendFilledInReview(receiver, message));

        } else {
            System.out.println("Agent " + receiver.getAID().getName() + " nie dostal wiadomosci - blokada");
            block();
        }
    }
}

