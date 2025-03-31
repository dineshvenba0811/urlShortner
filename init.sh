#!/bin/bash
#!/bin/bash

# Wait for Cassandra to become available
echo "Waiting for Cassandra to start..."
until cqlsh localhost 9042 -e "describe cluster"; do
  echo "Cassandra not available yet, retrying in 10 seconds..."
  sleep 10
done

echo "Cassandra is up - creating keyspace..."
cqlsh localhost 9042 -f /scripts/init-keyspace.cql