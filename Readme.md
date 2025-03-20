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
   •	The service should generate a unique and shortened alias for a given long URL.
   •	When a user accesses a shortened URL, the system must redirect them to the original URL.

2. Non-Functional Requirements
   •	The system must ensure high availability, as any downtime would disrupt URL redirections.
   •	URL redirection should occur in real-time with minimal latency.
   •	The generated short links should be unpredictable to prevent guessing patterns.