-- Создание keyspace с NetworkTopologyStrategy и репликационным фактором 1 для datacenter1
CREATE KEYSPACE IF NOT EXISTS distcomp
    WITH replication = {
        'class': 'NetworkTopologyStrategy',
        'datacenter1': 1
        }
     AND durable_writes = true;

-- Использование созданного keyspace
USE distcomp;