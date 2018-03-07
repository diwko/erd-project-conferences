package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Conference {
    static int lastId = 1;

    int id;
    String name;
    String street;
    String city;
    String postalCode;
    String country;
    String description;
    List<ConferenceDay> conferenceDays = new ArrayList<>();

    public Conference(String name, String street, String city, String postalCode, String country, String description) {
        id = lastId++;
        this.name = name;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.description = description;

        addConferenceDays();
    }

    public void generateWorkshops(List<String> details) {
        for (ConferenceDay day : conferenceDays)
            day.generateWorkshops(details);
    }

    public void generateReservations(List<Customer> customers, List<Participant> participants) {
        for (ConferenceDay day : conferenceDays)
            day.generateConferenceDayReservations(customers, participants);
    }

    private void addConferenceDays() {
        Random rand = new Random();
        int daysNumber = rand.nextInt(4) + 1;

        int year = rand.nextInt(4) + 2013;
        int month = rand.nextInt(12) + 1;
        int day = rand.nextInt(24) + 1;

        if (rand.nextInt() % 10 == 0) {
            year = 2017;
            month = rand.nextInt(6) + 1;
        }

        for (; daysNumber > 0; daysNumber--) {
            String date = year + "-" + month + "-" + day++ + " " + (rand.nextInt(3) + 8) + ":" + "00" + ":" + "00";
            conferenceDays.add(new ConferenceDay(date, this));
        }
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder(
                "INSERT INTO" +
                        " Conferences (Name, Street, City, PostalCode, Country, Description)" +
                        " VALUES(" +
                        //id +
                        "'" + name + "'" +
                        ",'" + street + "'" +
                        ",'" + city + "'" +
                        ",'" + postalCode + "'" +
                        ",'" + country + "'" +
                        ",'" + description + "'" +
                        ");\n");

        for (ConferenceDay conferenceDay : conferenceDays) {
            insert.append(conferenceDay.toString());
        }

        return insert.toString();
    }

}
