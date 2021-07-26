package model.todospage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DueSoonTest {
    DueSoon dueSoon;

    @Test
    void testConstructor() {
        dueSoon = new DueSoon();
        assertEquals(DueSoon.class, dueSoon.getClass());
    }
}
