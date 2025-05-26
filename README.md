
НА РУССКОМ ЯЗЫКЕ

	//Инструкция по запуску:

1. клонировать репозиторий на жёсткий диск

2. в докере выполнить команды:

-  команду:
  docker run -d `
  --name postgres_db `
  -e POSTGRES_USER=postgres `
  -e POSTGRES_PASSWORD=postgres `
  -e POSTGRES_DB=distcomp `
  -p 5432:5432 `
  -v postgres_data:/var/lib/postgresql/data `
  postgres:15

-  команду:
  docker run --name cassandra -d -p 9042:9042 cassandra:latest

-  команду:
  docker exec -it cassandra cqlsh

-  команду:
   docker exec -it cassandra cqlsh

-  команду:
   CREATE KEYSPACE IF NOT EXISTS distcomp
   WITH replication = {
   'class': 'SimpleStrategy',
   'replication_factor': 1
   };

-  команду:
    EXIT;

-  команду:
   docker network create kafkanet

-  команду:
   docker run -d `
    --network=kafkanet `
   --name=zookeeper `
    -e ZOOKEEPER_CLIENT_PORT=2181 `
   -e ZOOKEEPER_TICK_TIME=2000 `
    -p 2181:2181 `
   confluentinc/cp-zookeeper:7.3.2

-  команду:
   --network=kafkanet `
    --name=kafka `
   -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 `
    -e KAFKA_ADVERTISED_LISTENERS="PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092" `
   -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP="PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT" `
    -e KAFKA_LISTENERS="PLAINTEXT://0.0.0.0:29092,PLAINTEXT_HOST://0.0.0.0:9092" `
   -e KAFKA_INTER_BROKER_LISTENER_NAME="PLAINTEXT" `
    -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 `
   -p 9092:9092 `
   confluentinc/cp-kafka:7.3.2

-  команду:
   docker run -d --name redis -p 6379:6379 redis

-  команду:
   docker run --name distcomp -d -p:24100:24100 khmelov/distcomp:latest

3. Запустить файл start-apps.bat

4. Открыть приложение distcomp по http://localhost:24100/

5. Зайти под профилем:
  -Степанов
  -Руслан
  -stepanovruslan@mail.ru

6. Запустить тесты

    //Отчёт по выполнению задач:

1. - ВЫПОЛНЕНО
- REST basic test
2.1. - ВЫПОЛНЕНО
- JPA basic tests
2.2. - ВЫПОЛНЕНО
- JPA advanced tests
3. - ВЫПОЛНЕНО
- NoSQL basic test
4. - ВЫПОЛНЕНО
- Checking Kafka
5. - ВЫПОЛНЕНО
- Redis check
6. - ВЫПОЛНЕНО
- Security check
________________________________________________________________________________

IN ENGLISH LANGUAGE

//Setup instructions:

1. Clone the repository to your hard drive

2. In Docker, execute the following commands:

    Command:
    docker run -d --name postgres_db
    -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres
    -e POSTGRES_DB=distcomp -p 5432:5432
    -v postgres_data:/var/lib/postgresql/data `
    postgres:15

    Command:
    docker run --name cassandra -d -p 9042:9042 cassandra:latest

    Command:
    docker exec -it cassandra cqlsh

    Command:
    docker exec -it cassandra cqlsh

    Command:
    CREATE KEYSPACE IF NOT EXISTS distcomp
    WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
    };

    Command:
    EXIT;

    Command:
    docker network create kafkanet

    Command:
    docker run -d --network=kafkanet
    --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181
    -e ZOOKEEPER_TICK_TIME=2000 -p 2181:2181
    confluentinc/cp-zookeeper:7.3.2

    Command:
    --network=kafkanet --name=kafka
    -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS="PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092"
    -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP="PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT" -e KAFKA_LISTENERS="PLAINTEXT://0.0.0.0:29092,PLAINTEXT_HOST://0.0.0.0:9092"
    -e KAFKA_INTER_BROKER_LISTENER_NAME="PLAINTEXT" -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1
    -p 9092:9092 `
    confluentinc/cp-kafka:7.3.2

    Command:
    docker run -d --name redis -p 6379:6379 redis

    Command:
    docker run --name distcomp -d -p:24100:24100 khmelov/distcomp:latest

3. Run the start-apps.bat file

4. Open the distcomp application at http://localhost:24100/

5. Log in using the profile:
  -Stepanov
  -Ruslan
  -stepanovruslan@mail.ru

6. Run the tests

//Task completion report:

1. COMPLETED
REST basic test

2.1. COMPLETED
JPA basic tests

2.2. COMPLETED
JPA advanced tests

3. COMPLETED
NoSQL basic test

4. COMPLETED
Checking Kafka

5. COMPLETED
Redis check

6. COMPLETED
Security check	


