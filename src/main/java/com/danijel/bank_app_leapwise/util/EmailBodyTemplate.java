package com.danijel.bank_app_leapwise.util;

public class EmailBodyTemplate {

    public static String createSuccessEmailBody(Long transactionId,
                                                Double amount,
                                                Double oldBalance,
                                                Double newBalance) {
        String template = getSuccessEmailTemplate();
        return String.format(template, transactionId, amount, oldBalance, newBalance);
    }

    public static String createFailureEmailBody() {
        return getFailureEmailTemplate();
    }

    private static String getSuccessEmailTemplate() {
        return """
             Hello!
            \s
             The transaction with ID: %d has been processed successfully,
             and the balance: %.2f has been added to your account.
            \s
             Old balance: %.2f
             New balance: %.2f
            \s
             Regards,
             Your bank_app_leapwise bank
            \s""".stripIndent();
    }

    private static String getFailureEmailTemplate() {
        return """
              Hello!
             \s
              We're sorry to notify you the transaction has failed
             \s
              Regards,
              Your bank_app_leapwise bank
             \s""";
    }
}
