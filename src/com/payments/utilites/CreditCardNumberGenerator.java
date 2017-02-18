package com.payments.utilites;

import org.apache.log4j.Logger;

import java.util.Random;

/**
 * A credit card number generator.
 *
 * @author Josef Galea
 */
public class CreditCardNumberGenerator {

    private static final Logger LOG = Logger.getLogger(CreditCardNumberGenerator.class);
    private static volatile CreditCardNumberGenerator instance;
    private final Random random = new Random(System.currentTimeMillis());

    private CreditCardNumberGenerator() {
        LOG.debug(String.format(Constants.ENTITY_WAS_CREATED, this.getClass().getSimpleName()));
    }

    public static CreditCardNumberGenerator getInstance() {
        if (instance == null) {
            synchronized (CreditCardNumberGenerator.class) {
                if (instance == null) {
                    instance = new CreditCardNumberGenerator();
                }
            }
        }
        return instance;
    }

    /**
     * Generates a random valid credit card number. For more information about
     * the credit card number generation algorithms and credit card numbers
     * refer to <a
     * href="http://euro.ecom.cmu.edu/resources/elibrary/everycc.htm">Everything
     * you ever wanted to know about CC's</a>, <a
     * href="http://www.darkcoding.net/credit-card/">Graham King's blog</a>, and
     * <a href="http://codytaylor.org/2009/11/this-is-how-credit-card-numbers-are-generated.html"
     * >This is How Credit Card Numbers Are Generated</a>
     *
     * @param bin    The bank identification number, a set digits at the start of the credit card
     *               number, used to identify the bank that is issuing the credit card.
     * @param length The total length (i.e. including the BIN) of the credit card number.
     * @return A randomly generated, valid, credit card number.
     */
    public String generate(String bin, int length) {

        int randomNumberLength = length - (bin.length() + 1);

        StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = this.random.nextInt(10);
            builder.append(digit);
        }

        int checkDigit = this.getCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }

    /**
     * Generates the check digit required to make the given credit card number
     * valid (i.e. pass the Luhn check)
     *
     * @param number The credit card number for which to generate the check digit.
     * @return The check digit required to make the given credit card number
     * valid.
     */
    private int getCheckDigit(String number) {

        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            // Get the digit at the current position.
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }
}
