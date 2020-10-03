package XPOJOS.Response;

import XPOJOS.Model.Person;

public class PersonAllResponse extends SuccessMessageResponse {

    private Person[] data;

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public PersonAllResponse() {

    }
}
