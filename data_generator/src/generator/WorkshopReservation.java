package generator;

import java.util.*;

public class WorkshopReservation {
    static int lastId = 1;
    int id;
    int places;
    String date;
    int cancelled;
    ConferenceDayReservation conferenceDayReservation;
    Workshop workshop;

    Map<Integer, Participant> participants = new HashMap<>();

    public WorkshopReservation(ConferenceDayReservation conferenceDayReservation, Workshop workshop, int freePlaces) {
        id = lastId++;
        this.conferenceDayReservation = conferenceDayReservation;
        this.workshop = workshop;
        int[] confResDate = DateConverter.toInt(conferenceDayReservation.date);
        Random random = new Random();
        date = DateConverter.toString(confResDate[0], confResDate[1], confResDate[2],
                confResDate[3] + random.nextInt(4) + 1, random.nextInt(60), random.nextInt(10) + 10);

        Random rand = new Random();
        places = rand.nextInt(freePlaces) + 1;
        places = places < conferenceDayReservation.places ? places : conferenceDayReservation.places;

        if (conferenceDayReservation.cancelled == 1 || rand.nextInt() % 30 == 0)
            cancelled = 1;
        if (conferenceDayReservation.participants.size() == 0) {
            cancelled = 0;
        } else {
            cancelled = 0;
            addParticipants();
            conferenceDayReservation.generatePayment();
        }

    }

    private void addParticipants() {
        Random rand = new Random();
        for (int i = 0; i < places; i++) {
            List<Integer> keys = new ArrayList<>(conferenceDayReservation.participants.keySet());
            Integer keyParticipant = keys.get(rand.nextInt(keys.size()));
            if (participants.containsKey(keyParticipant))
                i--;
            else
                participants.put(keyParticipant, conferenceDayReservation.participants.get(keyParticipant));
        }
    }

    public double getCost() {
        return workshop.price * participants.size();
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder("INSERT INTO" +
                " WorkshopReservations ( WorkshopID, ConferenceDayReservationID, Places, ReservationDate, Cancelled)" +
                " VALUES(" +
                // id +
                "" + workshop.id +
                "," + conferenceDayReservation.id +
                "," + places +
                ",'" + date + "'" +
                "," + cancelled +
                ");\n");

        for (Integer key : participants.keySet()) {
            insert.append("INSERT INTO" +
                    " WorkshopParticipants (WorkshopReservationID, ConferenceDayParticipantID)" +
                    " VALUES(" +
                    id +
                    "," + key +
                    ");\n");
        }

        return insert.toString();
    }

}


