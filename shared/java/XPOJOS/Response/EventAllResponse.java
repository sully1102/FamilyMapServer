package XPOJOS.Response;

import XPOJOS.Model.Event;

public class EventAllResponse extends SuccessMessageResponse {

    private Event[] data;

    public Event[] getData() { return data; }

    public void setData(Event[] data) {
        this.data = data;
    }

    public EventAllResponse() {

    }
}
