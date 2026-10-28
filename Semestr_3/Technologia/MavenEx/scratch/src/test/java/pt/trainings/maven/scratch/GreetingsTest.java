package pt.trainings.maven.scratch;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class GreetingsTest {
    @Test 
    public void testNullNameShouldPrintDefaultName() {
        Greetings greetings = new Greetings();
        String result = greetings.sayHello(null);
        assertEquals("Hello John Doe", result);
    }

    @Test 
    public void testNameShouldPrintName() {
        Greetings greetings = new Greetings();
        String result = greetings.sayHello("Joe");
        assertEquals("Hello Joe", result);
    }
}
