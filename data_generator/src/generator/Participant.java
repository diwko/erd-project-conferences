package generator;

public class Participant {
    static int lastId = 1;
    int id;
    String lastName;
    String firstName;
    String email;
    int cardNumber = -1;
    String expirationDate = null;

    public Participant(String lastName, String firstName, String email) {
        id = lastId++;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    public void addStudent(int cardNumber, String expirationDate) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder("INSERT INTO" +
                " Participants ( LastName, FirstName, Email)" +
                " VALUES(" +
                //id +
                "'" + lastName + "'" +
                ",'" + firstName + "'" +
                ",'" + email + "'" +
                ");\n");

        if (cardNumber != -1)
            insert.append("INSERT INTO" +
                    " Students (ParticipantID, CardNumber, ExpirationDate)" +
                    " VALUES(" +
                    id +
                    ",'" + cardNumber + "'" +
                    ",'" + expirationDate + "'" +
                    ");\n");

        return insert.toString();
    }

}
