package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * The Inspect Window. Provides information about the contact being inspected
 * and takes commands to manage deliveries under this contact.
 */
public class InspectWindow extends UiPart<Stage> {

    private static final String FXML = "InspectWindow.fxml";

    private static Person person;

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private DeliveryListPanel deliveryListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @javafx.fxml.FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personInfoPlaceholder;

    @FXML
    private StackPane deliveryListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public InspectWindow(Stage primaryStage, Logic logic, Person person) {
        super(FXML, primaryStage);

        //this.person = person;
        InspectWindow.person = person;

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        HBox splitLayout = new HBox();
        splitLayout.setSpacing(20);

        VBox personInfoBox = new VBox();
        personInfoBox.setSpacing(10);

        Label nameLabel = new Label("Name: " + InspectWindow.person.getName());
        Label phoneLabel = new Label("Phone: " + InspectWindow.person.getPhone());
        Label emailLabel = new Label("Email: " + InspectWindow.person.getEmail());
        Label addressLabel = new Label("Address: " + InspectWindow.person.getAddress());
        Label tagsLabel = new Label("Tags: " + InspectWindow.person.getTags());

        personInfoBox.getChildren().addAll(nameLabel, phoneLabel, emailLabel, addressLabel, tagsLabel);

        DeliveryListPanel deliveryListPanel = new DeliveryListPanel(InspectWindow.person.getDeliveryList());
        VBox deliveryListBox = new VBox(deliveryListPanel.getRoot());

        splitLayout.getChildren().addAll(personInfoBox, deliveryListBox);

        personInfoPlaceholder.getChildren().add(splitLayout);

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    private void handleList(CommandResult commandResult) {
        logger.info("Changing UI back to main window...");

        MainWindow mainWindow = new MainWindow(primaryStage, logic);
        mainWindow.show();
        mainWindow.fillInnerParts();
        mainWindow.getResultDisplay().setFeedbackToUser(commandResult.getFeedbackToUser());
        AddressBookParser.setInspect(false);
    }

    public ResultDisplay getResultDisplay() {
        return resultDisplay;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            /* commandResult stores 'isInspect' boolean for whether we are in inspect mode.
            *  However, to get this commandResult in the first place, we need to parse the correct add command
            *  (i.e. window vs inspect).
            *  Thus, another boolean for 'isInspect' is tracked in AddressBookParser. These 2 booleans
            *  will always have the same value. */

            // This commandResult should store the result after executing add.
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            } else if (commandResult.isExit()) {
                handleExit();
            } else if (commandResult.isList()) {
                handleList(commandResult);
            } else if (commandResult.isDeliveryAdded()) {
                return commandResult;
            } else {
                throw new CommandException("Not yet implemented");
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    public static Person getInspectedPerson() {
        return InspectWindow.person;
    }
}
