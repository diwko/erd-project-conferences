package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Workshop {
    static int lastId = 1;

    int id;
    String startTime;
    String endTime;
    String name;
    int placeLimit;
    double price;
    String description;
    ConferenceDay conferenceDay;
    List<WorkshopReservation> reservations = new ArrayList<>();

    public Workshop(String startTime, String endTime, String name, String description, ConferenceDay conferenceDay) {
        id = lastId++;
        this.name = name;
        this.description = description;
        this.conferenceDay = conferenceDay;

        this.startTime = startTime;
        this.endTime = endTime;

        Random rand = new Random();
        placeLimit = (rand.nextInt(conferenceDay.placeLimit / 10) + 1) * 10;
        price = rand.nextInt(150);

        generateReservations();
    }

    private void generateReservations() {
        int freePlaces = placeLimit;
        for (ConferenceDayReservation reservation : conferenceDay.reservations) {
            WorkshopReservation res = new WorkshopReservation(reservation, this, freePlaces);
            freePlaces -= res.places;
            if (freePlaces <= 0)
                break;
            reservations.add(res);
        }
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder("INSERT INTO" +
                " Workshops ( ConferenceDayID, StartTime, EndTime, Name, PlaceLimit, Price, Description)" +
                " VALUES(" +
                //id +
                "" + conferenceDay.id +
                ",'" + startTime + "'" +
                ",'" + endTime + "'" +
                ",'" + name + "'" +
                "," + placeLimit +
                "," + price +
                ",'" + description + "'" +
                ");\n");
        for (WorkshopReservation reservation : reservations)
            insert.append(reservation.toString());

        return insert.toString();
    }
}
