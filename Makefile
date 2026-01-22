build:
	mvn clean install -DskipTests
	docker-compose up --build -d

	@echo "Both projects have been built and started!"

build2:
	mvn clean install -DskipTests
	docker-compose down -v
	docker-compose up --build -d

	@echo "Both projects have been built and started!"