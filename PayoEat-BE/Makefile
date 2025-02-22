build:
	mvn clean install -DskipTests
	docker-compose down
	docker-compose up --build -d

	@echo "Both projects have been built and started!"