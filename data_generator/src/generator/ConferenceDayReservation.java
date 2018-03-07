package generator;

import java.util.*;

public class ConferenceDayReservation {
    static int lastId = 1;
    static int participantsReserved = 1;

    int id;
    int places;
    String date;
    int cancelled;
    ConferenceDay conferenceDay;
    Customer customer;
    Map<Integer, Participant> participants = new HashMap<>();
    Payment payment;

    public ConferenceDayReservation(ConferenceDay conferenceDay, Customer customer, List<Participant> allParticipants, List<Participant> forbiddenParticipants) {
        id = lastId++;
        this.conferenceDay = conferenceDay;
        this.customer = customer;

        Random rand = new Random();
        places = rand.nextInt(conferenceDay.freePlaces) + 1;
        int[] conferenceDate = DateConverter.toInt(conferenceDay.date);

        Calendar now = Calendar.getInstance();

        if (conferenceDate[0] == now.get(Calendar.YEAR)) {
            date = DateConverter.toString(conferenceDate[0], now.get(Calendar.MONTH) + 1, rand.nextInt(now.get(Calendar.DAY_OF_MONTH)) + 1,
                    rand.nextInt(9) + 8, rand.nextInt(60), rand.nextInt(60));
        } else {
            date = DateConverter.toString(conferenceDate[0], conferenceDate[1], rand.nextInt(conferenceDate[2]) + 1,
                    rand.nextInt(9) + 8, rand.nextInt(60), rand.nextInt(60));
        }


        if (rand.nextInt() % 30 == 0)
            cancelled = 1;
        else if (rand.nextInt() % 3 == 0 && conferenceDate[0] == now.get(Calendar.YEAR) && conferenceDate[1] > now.get(Calendar.MONTH)) {
            cancelled = 0;
        } else {
            cancelled = 0;
            addParticipants(allParticipants, forbiddenParticipants);
        }
    }

    private void addParticipants(List<Participant> allParticipants, List<Participant> forbiddenParticipants) {
        Random rand = new Random();
        for (int i = 0; i < places; i++) {
            Participant participant = allParticipants.get(rand.nextInt(allParticipants.size()));
            if (participants.values().contains(participant) || forbiddenParticipants.contains(participant))
                i--;
            else
                participants.put(participantsReserved++, participant);
        }
    }

    public void generatePayment() {
        payment = new Payment(this);
    }

    public double getCost() {
        if (cancelled == 1)
            return 0;

        double price = conferenceDay.getPrice(date);

        double value = price * participants.size();

        int studentCounter = 0;
        for (Participant p : participants.values()) {
            if (p.expirationDate != null) {
                if (DateConverter.dateComparator(p.expirationDate, date) == -1)
                    studentCounter++;
            }
        }
        value -= conferenceDay.studentDiscount * price * studentCounter;

        //koszt warsztatów
        for (Workshop workshop : conferenceDay.workshops) {
            for (WorkshopReservation workshopReservation : workshop.reservations) {
                if (workshopReservation.conferenceDayReservation == this)
                    value += workshopReservation.getCost();
            }
        }

        return value;
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder("INSERT INTO" +
                " ConferenceDayReservations ( ConferenceDayID, CustomerID, Places, ReservationDate, Cancelled)" +
                " VALUES(" +
                //id +
                "" + conferenceDay.id +
                "," + customer.id +
                "," + places +
                ",'" + date + "'" +
                "," + cancelled +
                ");\n");

        for (Integer conferenceDayParticipantID : participants.keySet()) {
            insert.append("INSERT INTO" +
                    " ConferenceDayParticipants (ConferenceDayReservationID, ParticipantID)" +
                    " VALUES(" +
                    //conferenceDayParticipantID +
                    "" + id +
                    "," + participants.get(conferenceDayParticipantID).id +
                    ");\n");
        }

        if (cancelled != 1)
            insert.append(payment.toString());

        return insert.toString();
    }
}
