# Exercise
​
    Design a simplified banking system that manages customer accounts, transactions, and notifications towards users.
​
---
# Specification
​
## 1. Import file during startup
    The file should contain transaction history for the last year and have 100k random records imported into the database during application startup.
* ### Use-case
    Generate file with 100k records of transaction to be imported into database
​
* ### Workflow
    File should be parsed and mapped to database objects, then imported into database.
    Consider using multithreaded approach.
​
---
## 2. Provide a REST API and a service for:
    1) Checking customer details (including balance) for any given customerId.
    2) Processing payments.
    3) Viewing transaction history for any given customerId with optional filters.
​
* ## API Definitions:
​
* ### <u>2.1 Customer Endpoint </u>
​
    ```
    /customer/{id}
    ```
​
  * #### API Input:
    customerId
​
  * #### API Response:
    For each request to this API endpoint, return a response that includes the customer details.
​
  * #### Workflow:
    When the API is called, your code should retrieve the customer account data from the database and return the customer object.
​
* ### <u>2.2 Transaction endpoint: </u>
    ```
    /transaction
    ```
​
  * #### API Input:
    This API endpoint takes a transaction object and processes the transaction.
​
  * #### API Output:
    Returns the ID of the stored transaction.
​
  * #### Workflow:
    When this API is called, create a new transaction in the database and return its ID.
​
* ### <u>2.3 Transaction history endpoint: </u>
    ```
    /customer/history/{id}
    ```
​
  * #### API Input:
    This API endpoint takes the customer ID and optionally filter name and value as input, for example:
    * /transaction/history/{customerId}
    * or
    * /transaction/history/{customerId}?{filter_name}={filter_value}
​
  * #### API Output:
    Returns the transaction history for the client.
​
  * #### Workflow:
    When this API is called, search the database for customer transactions and return them as a list.
​
---
​
## 3. Monthly transaction update job
    There should be a configurable periodic job that calculates the turnover value based on income and expenditure for each account and updates all account entities
    (e.g., 'pastMonthTurnover' in the customer table column). 
​
​
---
## 4. Transaction confirmation
After each transaction is processed, send an email to clients with a confirmation that includes the amount and transaction status.
​
- **Email content example:**
​

        Hello!
    
        The transaction with ID: {id} has been processed successfully/unsuccessfully,
        and the balance: {balance} has been added/taken from your account.
        
        Old balance: {old balance}
        New balance: {new balance}
        
        Regards,
        Your XYZ bank
​
---
## Additional Information
​
You should use the following frameworks for your work.
​
 * ### Spring JPA
    H2 database running in memory (data will not be persistent across application restarts).
​
* ### 3rd party libraries
    You are free to add/change any libraries that you might need to solve this exercise. The only requirement is that we do not have to set up/install any external software to run this application.
​
* ### Running the exercise with Maven
    ```mvn spring-boot:run```
​
* ### Committing
    Provide your solution by creating a feature branch using your name (e.g., feature/ivanhorvat) and push it to this repository.
​
* ### Class examples (not mandatory to use):
    ```
    Transaction 
    {
         transactionId;
         senderAccountId;
         receiverAccountId;
         amount;
         currencyId;
         message;
         timestamp;
    }
    ```
    ```
    Customer 
    {
        customerId;
        name;
        address;
        email;
        phoneNumber;
        accounts;
    }
    ```
    ```
    Account 
    {
        accountId;
        accountNumber;
        accountType;
        balance;
        pastMonthTurnover;
    }
    ```
