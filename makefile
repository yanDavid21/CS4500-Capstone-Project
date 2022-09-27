all: clean jar

clean:
    rm -rf build
    rm -rf .gradle

jar:
	./gradlew jar