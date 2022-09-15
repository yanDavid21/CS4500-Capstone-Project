all: clean jar

clean:
    rm -rf .gradle
    rm -rf build
    rm -rf lib
    rm -rf out

jar:
	./gradlew jar
