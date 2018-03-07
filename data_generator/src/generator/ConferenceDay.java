package generator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ConferenceDay {
    static int lastId = 1;

    int id;
    String date;
    Double studentDiscount;
    int placeLimit;
    int freePlaces;
    Conference conference;
    List<Workshop> workshops = new ArrayList<>();
    List<ConferenceDayReservation> reservations = new ArrayList<>();
    List<ConferencePrice> prices = new ArrayList<>();

    public ConferenceDay(String date, Conference conference) {
        id = lastId++;
        this.date = date;
        this.conference = conference;

        Random rand = new Random();
        studentDiscount = rand.nextInt(6) / 10.0;
        placeLimit = (rand.nextInt(23) + 8) * 10;
        freePlaces = placeLimit;

        generatePrices();
    }

    public void generateConferenceDayReservations(List<Customer> customers, List<Participant> participants) {
        List<Participant> usedParticipants = new LinkedList<>();
        Random rand = new Random();

        while (freePlaces > 0) {
            Customer customer = customers.get(rand.nextInt(customers.size()));
            ConferenceDayReservation reservation = new ConferenceDayReservation(this, customer, participants, usedParticipants);
            freePlaces -= reservation.places;
            usedParticipants.addAll(reservation.participants.values());
            reservations.add(reservation);

            if (rand.nextInt() % 30 == 0)
                break;
        }
    }

    public void generateWorkshops(List<String> details) {
        Random rand = new Random();
        int workshopsNumber = rand.nextInt(3) + 2;
        int startHour = DateConverter.toInt(date)[3];
        int minutes[] = {00, 10, 15, 20, 30, 40, 45, 50};
        String conferenceDate = date.split(" ")[0];

        for (; workshopsNumber > 0; workshopsNumber--) {
            startHour += rand.nextInt(1) + 1;
            String startTime = conferenceDate + " " + startHour + ":" + minutes[rand.nextInt(minutes.length)] + ":" + "00";
            startHour += rand.nextInt(2) + 1;
            String endTime = conferenceDate + " " + startHour + ":" + minutes[rand.nextInt(minutes.length)] + ":" + "00";
            String[] detailSelected = details.get(rand.nextInt(details.size())).split(",");
            String desciption = detailSelected[1] + ", " + detailSelected[2] + ", " + detailSelected[3];
            workshops.add(new Workshop(startTime, endTime, detailSelected[0], desciption, this));
        }

    }

    private void generatePrices() {
        int[] dateVal = DateConverter.toInt(date);
        int month = dateVal[1];
        int day = dateVal[2];
        Random rand = new Random();
        double value = 150;

        for (int i = 5; i > 0; i--) {
            day -= rand.nextInt(4) + 8;
            if (day < 1 && month > 1) {
                day = rand.nextInt(5) + 24;
                month--;
            } else if (day < 1)
                break;

            value -= rand.nextInt((int) value / 4);
            String datePrice = DateConverter.toString(dateVal[0], month, day, 0, 0, 0);
            prices.add(new ConferencePrice(this, datePrice, value));
        }
        if (prices.size() == 0) {
            prices.add(new ConferencePrice(this, DateConverter.toString(dateVal[0], 1, 1, 0, 0, 0), value));
        }
    }


    public double getPrice(String date) {
        prices.sort((a, b) -> DateConverter.dateComparator(a.datePriceStart, b.datePriceStart));

        double value = 0;
        for (ConferencePrice price : prices) {
            value = price.price;
            if (DateConverter.dateComparator(date, price.datePriceStart) == -1)
                break;
        }

        return value;
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder("INSERT INTO" +
                " ConferenceDays (ConferenceID, Date, StudentDiscount, PlaceLimit)" +
                " VALUES(" +
                //id +
                "" + conference.id +
                ",'" + date + "'" +
                "," + studentDiscount +
                "," + placeLimit +
                ");\n");

        for (ConferencePrice price : prices) {
            insert.append(price.toString());
        }

        for (Workshop workshop : workshops) {
            insert.append(workshop.toString());
        }

        for (ConferenceDayReservation reservation : reservations) {
            insert.append(reservation.toString());
        }

        return insert.toString();
    }
}
