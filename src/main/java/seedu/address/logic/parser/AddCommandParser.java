package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.delivery.Cost;
import seedu.address.model.delivery.Delivery;
import seedu.address.model.delivery.Eta;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        if (!AddressBookParser.getInspect()) {
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);
            //Check if name is present
            if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                    || !argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            }
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            //Set default values if prefixes not present
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).orElse("00000000"));
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).orElse("example@gmail.com"));
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).orElse(
                    "nullAddress, S000000")
            );
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            Person person = new Person(name, phone, email, address, tagList);

            return new AddCommand(person);
        } else {
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(args, PREFIX_ETA, PREFIX_ADDRESS, PREFIX_COST);
            Eta eta = ParserUtil.parseEta(argMultimap.getValue(PREFIX_ETA).get());
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).orElse(
                    "nullAddress, S000000")
            );
            Cost cost = ParserUtil.parseCost(argMultimap.getValue(PREFIX_COST).get());

            Delivery delivery = new Delivery(address, cost, eta);
            return new AddCommand(delivery);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
