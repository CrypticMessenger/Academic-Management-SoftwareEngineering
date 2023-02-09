package studentmanagement;

// an abstract class named Person with name and email
public abstract class Person {
    private String email;

    public Person(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

// a class named Student that extends Person
