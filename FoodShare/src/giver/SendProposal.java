package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendProposal extends OneShotBehaviour {

    GiverAgent giver;
    //TODO
    AID ChosenReceiverID;

    public SendProposal(GiverAgent agent) {
        giver = agent;
        ChosenReceiverID = new AID("R1", AID.ISLOCALNAME);
    }
    public SendProposal(GiverAgent agent, AID chosenReceiverID) {
        giver = agent;
        this.ChosenReceiverID = chosenReceiverID;
    }


    @Override
    public void action() {
        System.out.println("Agent " + giver.getAID().getName() + " wysyłam propozycje terminu do agenta " + ChosenReceiverID.getName());
        // przygotowanie wiadomości
        ACLMessage message;
        String content = "Propozycja terminu i miejsca odbioru";
        message = new ACLMessage(ACLMessage.PROPOSE);
        message.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        message.setContent(content);
        message.addReceiver(ChosenReceiverID);
        giver.send(message);

        giver.addBehaviour(new WaitForProposalAnswear(giver));
    }
}
