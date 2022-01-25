package giver;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class SendProposal extends OneShotBehaviour {

    GiverAgent giver;
    //TODO
    AID ChosenReceiverID;
    int offerID;

    public SendProposal(GiverAgent agent) {
        giver = agent;
        ChosenReceiverID = new AID("R1", AID.ISLOCALNAME);
    }
    public SendProposal(GiverAgent agent, AID chosenReceiverID) {
        giver = agent;
        this.ChosenReceiverID = chosenReceiverID;
    }
    public SendProposal(GiverAgent agent, AID chosenReceiverID, int offerID) {
        giver = agent;
        this.ChosenReceiverID = chosenReceiverID;
        this.offerID = offerID;
    }



    @Override
    public void action() {
        System.out.println("Agent " + giver.getAID().getName() + " wysyłam propozycje terminu do agenta " + ChosenReceiverID.getName());
        // przygotowanie wiadomości

        ACLMessage message;

        JSONObject content = prepareProposal();

        message = new ACLMessage(ACLMessage.PROPOSE);
        message.setOntology(OntologyNames.COLLECTION_DETAILS_ONTOLOGY);
        message.setContent(content.toString());
        message.addReceiver(ChosenReceiverID);
        giver.send(message);

        giver.addBehaviour(new WaitForProposalAnswear(giver));
    }

    JSONObject prepareProposal() {
        JSONObject content = new JSONObject();
        content.put("offerID", offerID);
        content.put("place", "miejsce A");
        content.put("date", "20.01.2022 05:00:00 PM");
        return content;
    }
}
