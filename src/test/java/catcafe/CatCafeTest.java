package catcafe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests für die CatCafe Logik")
class CatCafeTest {

    private CatCafe cafe;

    @BeforeEach
    void setUp() {
        cafe = new CatCafe();
    }

    @Test
    @DisplayName("Ein frisches Café sollte keine Katzen enthalten")
    void shouldReturnZeroCount_whenCafeIsInitial() {
        long count = cafe.getCatCount();
        assertEquals(0, count, "Die initiale Anzahl der Katzen muss 0 sein.");
    }

    @Test
    @DisplayName("Die Anzahl der Katzen erhöht sich beim Hinzufügen")
    void shouldIncreaseCount_whenCatsAreAdded() {
        FelineOverLord fluffy = new FelineOverLord("Fluffy", 3);
        cafe.addCat(fluffy);
        assertEquals(1, cafe.getCatCount(), "Nach dem Hinzufügen einer Katze muss der Count 1 sein.");
    }

    @Test
    @DisplayName("Hinzufügen von null sollte eine Exception werfen")
    void shouldThrowException_whenAddingNullCat() {
        assertThrows(NullPointerException.class, () -> {
            cafe.addCat(null);
        }, "Das Café darf keine null-Referenzen als Katzen akzeptieren.");
    }

    @Test
    @DisplayName("Katzen mit gleichem Gewicht werden nicht doppelt gezählt")
    void shouldNotCountDuplicateWeights_whenAddingCatsWithSameWeight() {
        FelineOverLord cat1 = new FelineOverLord("Minka", 4);
        FelineOverLord cat2 = new FelineOverLord("Luna", 4); // Gleiches Gewicht wie Minka
        cafe.addCat(cat1);
        cafe.addCat(cat2);
        assertEquals(1, cafe.getCatCount(), "Da der interne Baum auf dem Gewicht basiert, wird die zweite Katze ignoriert.");
    }

    @Test
    @DisplayName("Eine Katze sollte über ihren Namen gefunden werden")
    void shouldReturnCorrectCat_whenSearchingByExistingName() {
        FelineOverLord morticia = new FelineOverLord("Morticia", 3);
        cafe.addCat(morticia);
        cafe.addCat(new FelineOverLord("Sooky", 2));
        FelineOverLord found = cafe.getCatByName("Morticia");
        assertNotNull(found);
        assertEquals("Morticia", found.name());
    }

    @Test
    @DisplayName("Die Namenssuche liefert null, wenn der Name nicht existiert")
    void shouldReturnNull_whenSearchingForUnknownName() {
        cafe.addCat(new FelineOverLord("Morticia", 3));
        FelineOverLord found = cafe.getCatByName("Garfield");
        assertNull(found, "Die Suche nach einem unbekannten Namen muss null zurückgeben.");
    }

    @Test
    @DisplayName("Die Namenssuche mit null ist sicher und liefert null")
    void shouldReturnNull_whenSearchingForNameNull() {
        FelineOverLord found = cafe.getCatByName(null);
        assertNull(found, "Eine Suche nach 'null' sollte nicht abstürzen, sondern null liefern.");
    }

    @Test
    @DisplayName("Findet eine Katze innerhalb eines gültigen Gewichtsbereichs")
    void shouldReturnCat_whenWeightIsWithinRange() {
        FelineOverLord cat = new FelineOverLord("Gwenapurr", 3);
        cafe.addCat(cat);
        FelineOverLord found = cafe.getCatByWeight(3, 4);
        assertNotNull(found);
        assertEquals("Gwenapurr", found.name());
    }

    @Test
    @DisplayName("Gewichtssuche liefert null, wenn keine Katze in den Bereich passt")
    void shouldReturnNull_whenNoCatIsWithinWeightRange() {
        cafe.addCat(new FelineOverLord("Sooky", 2));
        FelineOverLord found = cafe.getCatByWeight(5, 10);
        assertNull(found, "Es gibt keine Katze zwischen 5 und 10kg.");
    }

    @Test
    @DisplayName("Gewichtssuche mit unlogischen Grenzen liefert null")
    void shouldReturnNull_whenWeightLimitsAreInvalid() {
        cafe.addCat(new FelineOverLord("Morticia", 3));
        FelineOverLord found = cafe.getCatByWeight(10, 5);
        assertNull(found, "Wenn minWeight > maxWeight ist, kann keine Katze gefunden werden.");
    }
}
