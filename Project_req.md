# Project Requirements

* Your database must be located on the server (i.e. on uranium.cs.umanitoba.ca, not local).
* Once your database is created, you will populate it with your data. You must use a code-based method to add records and submit that code as part of your final submission.
* You will create a front-end interface which allows a person (say, an analyst) to access and use your database. It can be as simple (e.g., command-line interface) or as feature-rich (e.g., complete GUI) as you want. However, a command-line interface is recommended, as your focus in this project should be on the database and querying.
* Your application should be implemented using Java, Python or as a website (i.e. index.html that is submitted as part of your project, not hosted somewhere)
* Your application must run on aviary without any installs.

## Implementation Requirements

* When using the interface, the database should be relatively secure according to what was discussed in class (e.g., can’t allow freely entering SQL commands to prevent SQL injection)
* Your interface should support an analyst trying to answer interesting questions they might have of the data, which are not easily answerable.
* **You should support at least one query which includes GROUP BY**
*  **one that includes ORDER BY**
*  **and one that includes an aggregate function**
* Queries should be relevant and potentially useful to an analyst. Complex queries that are hundreds of words long, nested four layers deep could be interesting for you to implement, but might be too convoluted for an analyst to ever reasonably run.
* Don’t worry too much about optimizing your queries, as long as they run in a reasonable amount of time. However, consider informing the user with a message of some kind if a query is currently being executed (so that they don’t think your interface has crashed).
* **Consider letting the user enter parameters for your queries.**
* **Content from all tables should be accessible one way or another. This might mean you have some simple queries in addition to the queries above.**
* Results from queries should be easy for an analyst to parse. **Results should have a logical ordering, be neatly organized into columns, be labelled with column headings, and have a limited number of tuples (or use paging) to prevent results from scrolling out of view.**
* **The interface should provide a way to delete all data from your database, and a way to repopulate it.**
* You will show that **your project is robust against invalid input** and SQL injection.


