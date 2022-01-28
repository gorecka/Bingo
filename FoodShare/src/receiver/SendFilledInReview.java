package receiver;

import communicationConstants.JsonKeys;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SendFilledInReview extends OneShotBehaviour {
    ReceiverAgent receiver;
    ACLMessage message;

    SendFilledInReview(ReceiverAgent agent, ACLMessage msg) {
        receiver = agent;
        message = msg;
    }

    @Override
    public void action() {
        // przetworzenie otrzymanej wiadomości
        String ontology = message.getOntology();
        AID advertisingColumnAgentName = message.getSender();
        System.out.println("Treść wiadomości: " + message.getContent());
        String content = message.getContent();

        JSONObject obj = new JSONObject(content);
        System.out.println("What do you think about " + obj.getString(JsonKeys.OFFER_ID) + " from " + obj.getString(JsonKeys.OFFER_AUTHOR) + obj.getString(JsonKeys.OFFER_REVIEW));
        String timestamp = obj.getString(JsonKeys.OFFER_SENDING_REVIEW_TIMESTAMP);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(timestamp, dtf);

        LocalDateTime currentDate = LocalDateTime.now();
        long timeDifference = ChronoUnit.HOURS.between(startDate, currentDate);
        //sprawdzenie czy nie uplynal czas odpowiedzi na ankiete
        if (timeDifference < 24) {

            ACLMessage reply;
            String replyContent;
            JSONObject json = new JSONObject();
            json.put(JsonKeys.OFFER_REVIEW, "4");
            json.put(JsonKeys.OFFER_AUTHOR, obj.getString(JsonKeys.OFFER_AUTHOR));

            replyContent = json.toString();
            System.out.println(receiver.getAID().getName() + " zaraz wysle wypelniona ankiete do slupa");
            reply = new ACLMessage(ACLMessage.INFORM);
            reply.addReceiver(advertisingColumnAgentName);
            reply.setOntology(ontology);
            reply.setContent(replyContent);
            receiver.send(reply);
        }
        else {
            System.out.println(receiver.getAID().getName()+" przekroczono czas na ankiete " + timeDifference);
        }
    }
}