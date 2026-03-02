help:
	@echo "Available targets: deps-up deps-down clean build test run gen-api docs"

deps-up:
	cd .. && docker compose up -d

deps-down:
	cd .. && docker compose down

clean:
	mvn clean

build:
	mvn clean package

test:
	mvn test

it-test:
	mvn clean verify -P cucumber

run:
	mvn spring-boot:run

gen-api:
	mvn generate-sources

docs:
	cd docs/documentation && npx antora antora-playbook.yml
