package seedu.address.model.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalDeliveries.APPLES;

import org.junit.jupiter.api.Test;

public class DeliveryTest {
    @Test
    public void toStringMethod() {
        String expected = Delivery.class.getCanonicalName() + "{id=" + APPLES.getId() + ", itemName="
                + APPLES.getItemName() + ", date=" + APPLES.getDate() + ", time=" + APPLES.getTime() + ", eta="
                + APPLES.getEta() + ", address=" + APPLES.getAddress() + ", cost=" + APPLES.getCost() + ", status="
                + APPLES.getStatus() + "}";
        assertEquals(expected, APPLES.toString());
    }
}
