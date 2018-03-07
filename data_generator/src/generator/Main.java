package generator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        try {
            List<Customer> customers = createCustomers();
            List<Participant> participants = createParticipants();
            List<Conference> conferences = createConferences(customers, participants);


            StringBuilder text = new StringBuilder();

            text.append(
                    "USE Konferencje\n" +
                            "GO\n" +
                            "ALTER TABLE Students NOCHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE WorkshopReservations NOCHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferenceDayReservations NOCHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferenceDays NOCHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE WorkshopParticipants NOCHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferencePrice NOCHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE Workshops NOCHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE Payments NOCHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferenceDayParticipants NOCHECK CONSTRAINT ALL\n" +

                            "ALTER TABLE ConferenceDayReservations DISABLE TRIGGER all\n" +
                            "ALTER TABLE WorkshopReservations DISABLE TRIGGER all\n" +
                            "ALTER TABLE WorkshopParticipants DISABLE TRIGGER all");


            for (Customer c : customers)
                text.append(c.toString());

            for (Participant p : participants)
                text.append(p.toString());

            for (Conference c : conferences)
                text.append(c.toString());

            text.append(
                    "ALTER TABLE Students CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE WorkshopReservations CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferenceDayReservations CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferenceDays CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE WorkshopParticipants CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferencePrice CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE Workshops CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE Payments CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferenceDayParticipants CHECK CONSTRAINT ALL\n" +
                            "ALTER TABLE ConferenceDayReservations ENABLE TRIGGER all\n" +
                            "ALTER TABLE WorkshopReservations ENABLE TRIGGER all\n" +
                            "ALTER TABLE WorkshopParticipants ENABLE TRIGGER all");


            File file = new File("insert.sql");
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(text.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Customer> createCustomers() throws IOException {
        List<Customer> customers = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader("resources/Customers.csv"));

        for (String line; (line = reader.readLine()) != null; ) {
            String[] arguments = line.split(",");
            Customer customer = new Customer(arguments[0], arguments[1], arguments[2]);
            customers.add(customer);
        }
        reader.close();
        reader = new BufferedReader(new FileReader("resources/CustomersFirm.csv"));
        for (String line; (line = reader.readLine()) != null; ) {
            String[] arguments = line.split(",");
            Customer customer = new Customer(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5], arguments[6], arguments[7], 1);
            customers.add(customer);
        }
        reader.close();

        return customers;
    }

    private static List<Participant> createParticipants() throws IOException {
        List<Participant> participants = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/Participants.csv"))) {
            Random rand = new Random();
            for (String line; (line = reader.readLine()) != null; ) {
                String[] arguments = line.split(",");
                Participant participant = new Participant(arguments[1], arguments[0], arguments[2]);

                int student = rand.nextInt();
                if (student % 7 == 0) {
                    int cardNumber = rand.nextInt(99999999) + 100000000;
                    String expirationDate = (rand.nextInt(4) + 2014) + "-" + (rand.nextInt(12) + 1) + "-" + (rand.nextInt(28) + 1);
                    participant.addStudent(cardNumber, expirationDate);
                }
                participants.add(participant);
            }
        }
        return participants;
    }

    private static List<Conference> createConferences(List<Customer> customers, List<Participant> participants) throws IOException {
        List<Conference> conferences = new ArrayList<>();
        List<String> workshopsDetails;
        try (Stream<String> stream = Files.lines(Paths.get("resources/Workshops.csv"))) {
            workshopsDetails = stream.collect(Collectors.toList());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("resources/Conferences.csv"))) {
            for (String line; (line = reader.readLine()) != null; ) {
                String[] arguments = line.split(",");
                Conference conference = new Conference(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]);
                conference.generateReservations(customers, participants);
                conference.generateWorkshops(workshopsDetails);
                for (ConferenceDay day : conference.conferenceDays) {
                    for (ConferenceDayReservation res : day.reservations) {
                        res.generatePayment();
                    }
                }
                conferences.add(conference);
            }
        }
        return conferences;
    }


}
