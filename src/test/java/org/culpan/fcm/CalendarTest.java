package org.culpan.fcm;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by harryculpan on 5/28/17.
 */
public class CalendarTest {
    @Test
    public void testFirstWeekdayOfMonth() throws Exception {
        Calendar c = Calendar.getInstance();

        assertEquals(2, c.getFirstWeekdayOfMonth(0));
        assertEquals(5, c.getFirstWeekdayOfMonth(4));
        assertEquals(4, c.getFirstWeekdayOfMonth(c.getIndexOfMonth("Fenella")));
        assertEquals(3, c.getFirstWeekdayOfMonth(6));
        assertEquals(2, c.getFirstWeekdayOfMonth(7));

        assertEquals(1, c.getFirstWeekdayOfMonth(8));
        assertEquals(0, c.getFirstWeekdayOfMonth(9));
    }

    @Test
    public void testMoonInfo() throws Exception {
        Calendar c = Calendar.getInstance();

        List<Calendar.MoonInfo> result = c.getMoons();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Stanro", result.get(0).name);
        assertEquals("Fingrimm", result.get(1).name);
    }

    @Test
    public void testDaysSinceStart() throws Exception {
        Calendar c = Calendar.getInstance();

        assertEquals(13, c.daysSinceStart(13, 0));
        assertEquals(52, c.daysSinceStart(4, 1));
        assertEquals(388, c.daysSinceStart(4, 8));
    }

    @Test
    public void testPhasesOfMoon() throws Exception {
        Calendar c = Calendar.getInstance();

        assertEquals(0, c.getMoonPhase(0, 0, 0));
        assertEquals(1, c.getMoonPhase(0, 1, 0));
    }
}