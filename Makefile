build:
	mvn clean install -DskipTests
	docker-compose down -v
	docker-compose up --build -d

	@echo "Build complete!"