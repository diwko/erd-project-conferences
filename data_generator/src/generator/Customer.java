package generator;

public class Customer {
    static int lastId = 1;
    int id;
    String name;
    String email;
    String phone;
    String street = null;
    String city = null;
    String postalCode = null;
    String country = null;
    String nip = null;
    int firm = 0;

    public Customer(String name, String email, String phone) {
        id = lastId++;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Customer(String name, String email, String phone, String street, String city, String postalCode, String country, String nip, int firm) {
        id = lastId++;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.nip = nip;
        this.firm = firm;
    }

    @Override
    public String toString() {
        StringBuilder insert = new StringBuilder("INSERT INTO" +
                " Customers (Name, Email, Phone)" +
                " VALUES(" +
                //id +
                "'" + name + "'" +
                ",'" + email + "'" +
                ",'" + phone + "'" +
                ");\n");

        if (street != null)
            insert.append("INSERT INTO" +
                    " CustomerDetails (CustomerID, Name, Street, City, PostalCode, Country, NIP, Firm)" +
                    " VALUES(" +
                    id +
                    ",'" + name + "'" +
                    ",'" + street + "'" +
                    ",'" + city + "'" +
                    ",'" + postalCode + "'" +
                    ",'" + country + "'" +
                    ",'" + nip + "'" +
                    "," + firm +
                    ");\n");

        return insert.toString();
    }
}
