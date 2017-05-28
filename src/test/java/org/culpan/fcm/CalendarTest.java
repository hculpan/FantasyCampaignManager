package org.culpan.fcm;

import org.junit.Test;

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
    }
}