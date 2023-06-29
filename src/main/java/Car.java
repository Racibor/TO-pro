import java.util.ArrayList;
import java.util.List;

public class Car {
    int gear = 0;
    List<String> luggage = new ArrayList<>();

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public List<String> placeLuggage(String item, Integer count) {
        luggage.add(item);
        return luggage;
    }

    public List<String> getLuggage() {
        return luggage;
    }

    public String threadTesting(String message) {
        return message;
    }

}
