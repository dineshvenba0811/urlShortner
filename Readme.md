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

4. Database Design [ SQL vs NoSQL ]

   When to Choose PostgreSQL
   •	strong consistency and transactions (ACID).
   •	system is not massively distributed or doesn’t need high write throughput.
   •	rich query language and need joins/aggregates (e.g., analytics).
   •	prefer a simpler setup and rich ecosystem.

   When to Choose Cassandra
   •	need massive horizontal scalability (millions of writes/reads per second).
   •	High availability and fault tolerance are key (e.g., global-scale system).
   •	okay with eventual consistency and denormalized data models.
   •	access patterns are read/write by primary key (great fit for short URL lookups!).

   As per our use case we choose Cassandra, 
      as we are not handling relational data among tables.
      most of the database operations are simple key values lookups
      read queries are much higher

5. URL Shortening API

   Endpoint: POST /api/v1/shorten
   This endpoint creates a new short URL for a given long URL.

   Sample Request:
   
   {
   "longUrl": "https://www.cricbuzz.com/live-cricket-scorecard/115014/"
   }
   
   Sample Response:

   Endpoint: GET /{shortUrlKey}

   This endpoint redirects the user to the original long URL.

6. ShortUrlGeneratorService

   The primary function of the service is to generate a short, unique URL for each long URL provided by the user.
   
   Here are some things to think about when picking an algorithm to shorten the URL:
   
       URL Length: Shorter is generally better, but it limits the number of possible distinct URLs you can generate.
   
       Scalability: The algorithm should work well even with billions of URLs.
   
       Collision Handling: The algorithm should be able to handle duplicate url generations.

       Hashing and Encoding

       approach for generating short URLs is to use a hash function, such as MD5 to generate a fixed-length hash of the original URL.
       This hash is then encoded into a shorter form using Base62.

       Base62 uses alphanumeric characters (A-Z, a-z, 0-9), which are URL-friendly and provide a dense encoding space.

       The length of the short URL is determined by the number of characters in the Base62 encoded string.

       A 7-character Base62 string can represent approximately 3.5 billion unique URLs (62^7).

       Example Workflow:
   
            Generate an MD5 hash of the long URL. MD5 produces a 128-bit hash, typically a 32-character hexadecimal string: 1b3aabf5266b0f178f52e45f4bb430eb
   
            Instead of encoding the entire 128-bit hash, we typically use a portion of the hash (e.g., the first few bytes) to create a more manageable short URL.
   
            Encode the result into a Base62 encoded string: DZFbb43

           Step-by-Step Flow
            
            1. Receive Long URL Input
               User submits a long URL via API (POST /shorten).
               Backend receives and processes the original URL.

            2. Generate Hash of the Long URL
               Compute a hash 

            3. Lookup Repository Check

               If the hash is found:
               •	Retrieve the corresponding short_url from the lookup repository.
               •	Return the short URL to the user.

               If the hash is not found:
               •	Proceed to generate a new short URL.
         
            4. Short URL Generation
               Generate a new short URL

7. Redirection Service

   When a user accesses a short URL, this service is responsible for redirecting the user to the original URL.

   This involves two key steps:
   
        Database Lookup: The Service Layer queries the database to retrieve the original URL associated with the short URL. This lookup needs to be optimized for speed, as it directly impacts user experience.
   
        Redirection: Once the long URL is retrieved, the service issues an HTTP redirect response, sending the user to the original URL.


8. Future Enhancement:
   Allow users to customize their short URLs.
   Support link expiration where URLs are no longer accessible after a certain period.
   Collision Resolution Strategies by Incremental Suffix
   Async Event driven mechanism instead of batch operation to insert data into two tables.


![](/Users/dineshkumar/Downloads/HLD.png)