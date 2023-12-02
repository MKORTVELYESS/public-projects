# Node.js Module Locker

When borrowing, a connection is established between the book and the user's records, and this connection ceases at the end of the loan. Within the framework of the module closure, you will create an API for users of a fictitious library. The library database can store books and users. Each book record represents a book copy that the user can borrow. The application contains the following tables:

## Books

1. id: the identifier of the book
1. title: the title of the book
1. author: the name of the author of the
1. book category: the category of the
1. book borrower: the identifier of the user currently borrowing the book. If it is null, no one is currently borrowing the book.
1. borrowing_date: start date of the current borrowing. If it is null, no one is currently borrowing the book.
1. return_date: the end date of the most recent rental. If it is null, then the book is being borrowed by someone.

## Users

1. id: the user's ID first_name: the
1. user's first name
1. last_name: last name of the user
1. email: the user's email address
1. phone: the user's phone number

To create the API, follow the instructions below! The API accepts JSON and can respond with JSON. Make sure to implement all the answers in the documentation! When solving the task, it is not mandatory to use the router - controller - service structure
learned in class, but it is recommended.

# Task 1 - Initialization

1. Create a new PostgreSQL database on your computer!
2. Load the data from the data.sql file in the task folder.
3. Create a new Node.js application on your computer! The name of the application should be in the following format: username-firstname-modulzaro 4. Create an Express.js server in this application, which is on port 3000. The port number should come from an environment variable!
4. Create a connection between the application and the database server! The data required for the database connection should come from environment variables!
5. Test it somehow! (the test code does not need to be included in the solution)

## Task 2 - List books

1. Develop the API endpoint for listing books! To do this, check the API documentation for the GET /books endpoint!
2. The endpoint should return all the books in the database a in the form found in the documentation.
3. Filter functions in the documentation (available and according to title filtering) is not needed yet!

## Task 3 - Filtering books

1. Add filter functions to the GET /books endpoint in the API documentation
2. If the user specifies the available parameter, the API returns only the available / unavailable books based on the value of the parameter!
3. If the user specifies the title parameter, then filter what is to be returned books by title based on the value of the specified parameter!

## Task 4 - Retrieving the data of a book

1. Develop it can be found in the API documentation under GET /books/{id} endpoint!
2. This endpoint must return the data of the book with the identifier specified in the id parameter as specified in the documentation.

## Task 5 - Mechanics of lending

1. Develop the lending mechanics in the PATCH in the API documentation based on /books/:id!
2. Borrowing is the book in the database with the ID parameter id is created by modifying it in the following way:

- if the book is being borrowed, then return_date to null, the and sets borrowing_date to the date of borrowing
- if the book is being returned, then borrowing_date to null, the and set return_date to the rental date

3. When borrowing, in addition to the dates, also the relationship between the user and the book must be properly set.
4. The parameters, body, and return options of the request can be found in the documentation

## Task 6 - Registration in the library

1. Develop the API endpoint that implements user registration based on the POST /users documentation!
2. The registration of a user means the creation of the user in the database.
3. The body of the request and its return options can be found in the documentation

## Task 7 - User loans

1. Develop the GET API endpoint listing the user's current rentals Based on /users/{id}/books!
2. The endpoint should list all the books specified in the id parameter user with ID is currently borrowing! A condition for borrowing is that there is a connection between the records.
3. The request parameter and return options can be found in the documentation

## Task 8 - Signing out of the library

1. Define the API endpoint for deleting the user as DELETE Based on the documentation of /users/{id}!
2. The endpoint deletes the user with id from the database.
3. The request parameter and return options can be found in the documentation

## Task 9 - Borrowing validation

1. Create a middleware that validates the rental data before it is created or it would cease!
2. A loan can be created if

- the book you want to borrow exists
- the user who wants to borrow exists
- the book you want to borrow has not been borrowed

3. A rental can be terminated if

- the specified book exists
- the borrowing user exists
- the given book is really with the given user

4. If either condition is not met, the endpoint is returned with a status of 405 and one Respond with an error message in JSON

5. If all conditions are met, the request is valid and the rental can be established/cancelled

## Task 10 - Logging

1. Create a middleware that writes to the console for every incoming HTTP request the request method and URL!
