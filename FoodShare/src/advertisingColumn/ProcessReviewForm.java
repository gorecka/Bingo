package advertisingColumn;

import communicationConstants.OntologyNames;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

public class ProcessReviewForm extends OneShotBehaviour {
    AdvertisingColumnAgent advertisingColumn;
    ACLMessage message;

    ProcessReviewForm(AdvertisingColumnAgent agent, ACLMessage msg)
    {
        advertisingColumn = agent;
        message = msg;
    }

    @Override
    public void action() {
        // pobranie obecnej oceny wystawiajacego
        //TODO
        double avg = 3.8;
        int numberOfReviews = 5;

        String content = message.getContent();
        JSONObject obj = new JSONObject(content);
        AID giver = new AID(obj.getString("giver"));
        double score = Double.parseDouble(obj.getString("review"));
        // wyliczenie sredniej oceny
        avg = ((avg * numberOfReviews) + score)/++numberOfReviews;
        boolean isBlocked = false;
        if (avg < 2.0 && numberOfReviews > 3) isBlocked = true;
        //zapisanie nowej sredniej
        //TODO


        //wyslanie informacji o nowej ocenie i potencjalnej blokadzie
        ACLMessage reply;
        String replyContent;
        JSONObject json = new JSONObject();
        json.put("review", Double.toString(score));
        json.put("reviewer", message.getSender());
        json.put("average", Double.toString(avg));
        json.put("isBlocked", Boolean.toString(isBlocked));
        replyContent = json.toString();

        reply = new ACLMessage(ACLMessage.INFORM);
        reply.setContent(replyContent);
        reply.setOntology(OntologyNames.REVIEWING_FORM_ONTOLOGY);
        reply.addReceiver(giver);
        advertisingColumn.send(reply);
    }

}
