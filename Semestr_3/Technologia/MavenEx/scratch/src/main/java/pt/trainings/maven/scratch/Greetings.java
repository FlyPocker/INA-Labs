package pt.trainings.maven.scratch;

public class Greetings {
    private static final String GREETING = "Hello ";
    public String sayHello(String name) {
        return GREETING + (name != null ? name : "John Doe");
    }
}
