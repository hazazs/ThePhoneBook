package PhoneBook;

import javafx.beans.property.SimpleStringProperty;

public class Person {
//<editor-fold defaultstate="collapsed" desc="Class variables">
    private SimpleStringProperty id;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
//</editor-fold>
    public Person() {   }
    public Person(String firstNameFx, String lastNameFx, String emailFx) {
        this.firstName = new SimpleStringProperty(firstNameFx);
        this.lastName = new SimpleStringProperty(lastNameFx);
        this.email = new SimpleStringProperty(emailFx);
    }
    public Person(int idFx, String firstNameFx, String lastNameFx, String emailFx) {
        this.id = new SimpleStringProperty(String.valueOf(idFx));
        this.firstName = new SimpleStringProperty(firstNameFx);
        this.lastName = new SimpleStringProperty(lastNameFx);
        this.email = new SimpleStringProperty(emailFx);
    }
    public String getFirstName() {
        return this.firstName.get();
    }
    public void setFirstName(String firstNameFx) {
        this.firstName.set(firstNameFx);
    }
    public String getLastName() {
        return this.lastName.get();
    }
    public void setLastName(String lastNameFx) {
        this.lastName.set(lastNameFx);
    }
    public String getEmail() {
        return this.email.get();
    }
    public void setEmail(String emailFx) {
        this.email.set(emailFx);
    }
    public String getID() {
        return this.id.get();
    }
    public void setID(int idFx) {
        this.id.set(String.valueOf(idFx));
    }
}