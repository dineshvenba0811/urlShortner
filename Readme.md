Requirement:

To provide a proper REST API that will enable other users/ clients to send a
URL and will receive some kind of identifier/ hash to which this URL corresponds
in our system. And the other way around of course, meaning that a user should be
able to resolve the full URL.


For example :
https://www.linkedin.com/in/dineshkumarchandrasekar081094/

We get the result given below:
http://bit.ly/3uQqImU 

1. Functional Requirements
   Generate a unique short URL for a given long URL
   Redirect the user to the original URL when the short URL is accessed

   Feature:
   Allow users to customize their short URLs 
   Support link expiration where URLs are no longer accessible after a certain period


2. Non-Functional Requirements
   High availability 
   Low latency 
   Scalability
   Durability
   

3. High Level Design:

   Load Balancer: Distributes incoming requests across multiple application servers
   Application Servers: Handles incoming requests for shortening URLs and redirecting users
   URL Generation Service: Generates short URLs.
   Redirection Service: Redirects the users to the original URL.
   Database: Stores mappings between short URLs and long URLs.
   Cache: Stores frequently accessed URL mappings for faster retrieval.

4. Database Design
   
   We need to store billions of records 
   Most database operations are simply key value lookups 
   Read Queries are much higher than write queries 
   We dont need joins between tables
   database needs to be highly scalable and available



