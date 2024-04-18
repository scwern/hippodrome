import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

public class HorseTest {
    @Test
    public void nullNameException() {
        assertThrows(IllegalArgumentException.class, () -> new Horse(null, 1, 1));
    }

    @Test
    public void nullNameMessage() { // - тест можно объединить с nullNameException, оставляю его в таком виде для наглядной разницы
        try {
            new Horse(null, 1, 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Name cannot be null.", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "\n", "\t"})
    public void emptyNameException(String name) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Horse(name, 1, 1));
        assertEquals("Name cannot be blank.", e.getMessage());
    }

    // - использую параметризованные тесты для усвоения материала, здесь можно было обойтись обычными
    @ParameterizedTest
    @ValueSource(doubles = {-1.2, -2.2, -3.3})
    public void negativeSpeedException(double speed) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Horse("name", speed, 1));
        assertEquals("Speed cannot be negative.", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.1, -2.5, -5.7})
    public void negativeDistanceException(double distance) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Horse("name", 1, distance));
        assertEquals("Distance cannot be negative.", e.getMessage());
    }

    @Test
    public void getName() {
        Horse horse = new Horse("name", 1, 1);
        assertEquals("name", horse.getName());
    }

    @Test
    public void getSpeed() {
        Horse horse = new Horse("name", 1, 1);
        assertEquals(1, horse.getSpeed());
    }

    @Test
    public void getDistance() {
        Horse horse = new Horse("name", 1, 1);
        assertEquals(1, horse.getDistance());
    }

    @Test
    public void getDefaultDistance() {
        Horse horse = new Horse("name", 1);
        assertEquals(0, horse.getDistance());
    }

    @Test
    public void moveUsesGetRandom() {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            new Horse("name", 1, 1).move();
            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }
    @ParameterizedTest
    @ValueSource(doubles = {1.2 , 2.4, 3.7, 7.9, 9.5})
    public void move(double random){
        try(MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)){
            Horse horse = new Horse("a", 5, 10);
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(random);

            horse.move();

            assertEquals(10 + 5 * random, horse.getDistance() );
        }
    }

}
