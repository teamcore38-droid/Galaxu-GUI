package model;

public class SalesRepresentative extends User {
    public SalesRepresentative(String username, String passwordHash) {
        super(username, passwordHash, "Sales Representative");
    }
}
