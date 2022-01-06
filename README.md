# ATM
simple ATM with withdrawal and deposit function (without concurrency)

build: 
* mvn install
run: 
* mvn spring-boot:run

interfaces
* withdrawal 
  - Request URI: /api/Withdrawal 
  - Request method: POST
  - Request payload: JSON number: amount
  - Response: JSON object: denomination
  - example:
    - Request payload:
      16000
    - Response payload:
      {
      "10000": 1,
      "2000": 3
      }
  - Failure
    - cash store problem: HTTP 503 Service Unavailable
    - bad amount: HTTP 400 Bad Request
* deposit
  - Request URI: /api/Deposit
  - Request method: POST
  - Request payload: JSON object: denomination
  - Response: JSON number: cash storage sum
  - example:
    - Request payload:
      {
      "10000": 2,
      "5000": 3
      }
    - Response payload:
      35000
