build:
	@echo "Building PayoEat-Auth..."
	@cd ./PayoEat-Auth && mvn clean install -DskipTests
	@cd ./PayoEat-Auth && docker-compose down
	@cd ./PayoEat-Auth && docker-compose up --build -d

	@echo "Building PayoEat-BE..."
	@cd ./PayoEat-BE && mvn clean install -DskipTests
	@cd ./PayoEat-BE && docker-compose down
	@cd ./PayoEat-BE && docker-compose up --build -d

	@echo "Both projects have been built and started!"

stop:
	@echo "Stopping PayoEat-Auth..."
	@cd ./PayoEat-Auth && docker-compose stop
	@echo "Stopping PayoEat-BE..."
	@cd ./PayoEat-BE && docker-compose stop
	@echo "Both projects have been stopped."

logs:
	@echo "Showing logs for PayoEat-Auth..."
	@cd ./PayoEat-Auth && docker-compose logs -f
	@echo "Showing logs for PayoEat-BE..."
	@cd ./PayoEat-BE && docker-compose logs -f

clean:
	@echo "Cleaning up Docker resources..."
	@docker system prune -f
	@echo "Docker resources cleaned."