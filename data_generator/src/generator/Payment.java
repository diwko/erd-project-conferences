package generator;

import java.util.Random;

public class Payment {
    static int lastId = 1;

    int id;
    ConferenceDayReservation conferenceDayReservation;
    String date;
    Double value;

    public Payment(ConferenceDayReservation conferenceDayReservation) {
        id = lastId++;
        this.conferenceDayReservation = conferenceDayReservation;

        Random rand = new Random();
        int[] reservationconDate = DateConverter.toInt(conferenceDayReservation.date);
        date = DateConverter.toString(reservationconDate[0], reservationconDate[1], reservationconDate[2] + rand.nextInt(2) + 1,
                rand.nextInt(17) + 6, rand.nextInt(60), rand.nextInt(60));

        value = conferenceDayReservation.getCost();
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder("INSERT INTO" +
                " Payments ( ConferenceDayReservationID, PaymentDate, Value)" +
                " VALUES(" +
                //id +
                "'" + conferenceDayReservation.id + "'" +
                ",'" + date + "'" +
                "," + value +
                ");\n");

        return insert.toString();
    }


}
