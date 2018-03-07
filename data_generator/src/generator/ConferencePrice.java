package generator;

public class ConferencePrice {
    static int lastId = 1;

    int id;
    ConferenceDay conferenceDay;
    String datePriceStart;
    Double price;

    public ConferencePrice(ConferenceDay conferenceDay, String datePriceStart, Double price) {
        id = lastId++;
        this.conferenceDay = conferenceDay;
        this.datePriceStart = datePriceStart;
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder("INSERT INTO" +
                " ConferencePrice ( ConferenceDayID, DatePriceStart, Price)" +
                " VALUES(" +
                //id +
                "" + conferenceDay.id +
                ",'" + datePriceStart + "'" +
                "," + price +
                ");\n");

        return insert.toString();
    }
}
