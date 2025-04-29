CREATE KEYSPACE IF NOT EXISTS testKeyspace
WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
};