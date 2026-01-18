from confluent_kafka import Consumer, Producer, KafkaError
from typing import Callable, Optional
import json
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class KafkaClient:
    def __init__(self, bootstrap_servers: str = "localhost:9092", group_id: str = "ai-assistant-group"):
        self.bootstrap_servers = bootstrap_servers
        self.group_id = group_id

        # Producer configuration
        self.producer_config = {
            'bootstrap.servers': bootstrap_servers,
            'client.id': 'ai-assistant-producer',
            'acks': 'all',  # Wait for all replicas to acknowledge
            'retries': 5,
            'compression.type': 'snappy'
        }

        # Consumer configuration
        self.consumer_config = {
            'bootstrap.servers': bootstrap_servers,
            'group.id': group_id,
            'auto.offset.reset': 'earliest',  # Start from beginning if no offset
            'enable.auto.commit': False,  # Manual commit for reliability
            'max.poll.interval.ms': 300000  # 5 minutes
        }

        self.producer: Optional[Producer] = None
        self.consumer: Optional[Consumer] = None

    def start(self):
        """Initialize Kafka connections"""
        try:
            self.producer = Producer(self.producer_config)
            self.consumer = Consumer(self.consumer_config)
            logger.info(f"Kafka client started connecting to {self.bootstrap_servers}")
        except Exception as e:
            logger.error(f"Failed to start Kafka client: {e}")
            raise

    def stop(self):
        """Clean shutdown"""
        if self.consumer:
            self.consumer.close()
        if self.producer:
            self.producer.flush()
        logger.info("Kafka client stopped")

    def produce(self, topic: str, key: str, value: dict):
        """Produce message to Kafka topic"""
        def delivery_callback(err, msg):
            if err:
                logger.error(f"Message delivery failed: {err}")
            else:
                logger.debug(f"Message delivered to {msg.topic()} [{msg.partition()}]")

        try:
            self.producer.produce(
                topic=topic,
                key=key,
                value=json.dumps(value, default=str),
                callback=delivery_callback
            )
            self.producer.poll(0)  # Trigger delivery callbacks
        except Exception as e:
            logger.error(f"Failed to produce message: {e}")
            raise

    def consume(self, topics: list, callback: Callable[[dict], None]):
        """Consume messages from Kafka topics"""
        self.consumer.subscribe(topics)

        logger.info(f"Starting to consume from topics: {topics}")

        try:
            while True:
                msg = self.consumer.poll(1.0)  # Timeout 1 second

                if msg is None:
                    continue
                if msg.error():
                    if msg.error().code() == KafkaError._PARTITION_EOF:
                        continue
                    else:
                        logger.error(f"Consumer error: {msg.error()}")
                        break

                try:
                    # Deserialize message
                    value = json.loads(msg.value().decode('utf-8'))
                    logger.debug(f"Received message from {msg.topic()}: {value}")

                    # Process message
                    callback(value)

                    # Manually commit offset
                    self.consumer.commit(asynchronous=False)

                except json.JSONDecodeError as e:
                    logger.error(f"Failed to decode JSON: {e}")
                except Exception as e:
                    logger.error(f"Error processing message: {e}")

        except KeyboardInterrupt:
            logger.info("Consumer interrupted by user")
        finally:
            self.consumer.close()