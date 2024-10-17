package seedu.address.testutil;

import seedu.address.model.delivery.Cost;
import seedu.address.model.delivery.Date;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.delivery.Eta;
import seedu.address.model.delivery.Id;
import seedu.address.model.delivery.ItemName;
import seedu.address.model.delivery.Status;
import seedu.address.model.delivery.Time;
import seedu.address.model.person.Address;


/**
 * A utility class to help with building Delivery objects.
 */
public class DeliveryBuilder {

    public static final String DEFAULT_COST = "$100";
    public static final String DEFAULT_DATE = "2024-10-16";
    public static final String DEFAULT_ETA = "2103-12-31";
    public static final String DEFAULT_ID = "0";
    public static final String DEFAULT_ITEM_NAME = "Laptop";
    public static final String DEFAULT_STATUS = "not delivered";
    public static final String DEFAULT_TIME = "00:00:00";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111, S120300";

    private Cost cost;
    private Date date;
    private Eta eta;
    private Id id;
    private ItemName itemName;
    private Status status;
    private Time time;
    private Address address;

    /**
     * Creates a {@code DeliveryBuilder} with the default details.
     */
    public DeliveryBuilder() {
        cost = new Cost(DEFAULT_COST);
        date = new Date(DEFAULT_DATE);
        eta = new Eta(DEFAULT_ETA);
        id = new Id(DEFAULT_ID);
        itemName = new ItemName(DEFAULT_ITEM_NAME);
        status = new Status(DEFAULT_STATUS);
        time = new Time(DEFAULT_TIME);
        address = new Address(DEFAULT_ADDRESS);
    }

    /**
     * Initialises the PersonBuilder with the data of {@code personToCopy}
     * @param deliveryToCopy Delivery object to be copeid
     */
    public DeliveryBuilder(Delivery deliveryToCopy) {
        cost = deliveryToCopy.getCost();
        date = deliveryToCopy.getDate();
        eta = deliveryToCopy.getEta();
        id = deliveryToCopy.getId();
        itemName = deliveryToCopy.getItemName();
        status = deliveryToCopy.getStatus();
        time = deliveryToCopy.getTime();
        address = deliveryToCopy.getAddress();
    }

    /**
     * Sets the {@code Cost} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withCost(String cost) {
        this.cost = new Cost(cost);
        return this;
    }

    /**
     * Sets the {@code Date} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Sets the {@code Eta} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withEta(String eta) {
        this.eta = new Eta(eta);
        return this;
    }

    /**
     * Sets the {@code Id} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withId(String id) {
        this.id = new Id(id);
        return this;
    }

    /**
     * Sets the {@code Id} of hte {@code Delivery} that we are building.
     * Id is not specified but left to Id class to determine
     */
    public DeliveryBuilder withUndeclaredId() {
        this.id = new Id();
        return this;
    }

    /**
     * Sets the {@code ItemName} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withItemName(String itemName) {
        this.itemName = new ItemName(itemName);
        return this;
    }

    /**
     * Sets the {@code Status} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withStatus(String status) {
        this.status = new Status(status);
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withTime(String time) {
        this.time = new Time(time);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Delivery} that we are building.
     */
    public DeliveryBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Builds the {@code Delivery} object.
     *
     * @return The built Delivery object.
     */
    public Delivery build() {
        return new Delivery(id, itemName, address, cost, date, time, eta, status);
    }

}
