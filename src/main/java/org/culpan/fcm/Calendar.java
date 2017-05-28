package org.culpan.fcm;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harryculpan on 5/28/17.
 */
public class Calendar {
    static private Calendar calendarInstance;

    protected JSONObject jsonObject;

    static public Calendar getInstance() {
        if (calendarInstance == null) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = null;
                jsonObject = (JSONObject)parser.parse(new InputStreamReader(Calendar.class.getClassLoader().getResourceAsStream("calendar.json")));
                calendarInstance = new Calendar(jsonObject);
            } catch (IOException | ParseException e) {
                throw new RuntimeException("Unable to load calendar info", e);
            }
        }
        return calendarInstance;
    }

    private Calendar(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getYear() {
        return Integer.parseInt(jsonObject.get("year").toString());
    }

    public int getMonthsCount() {
        return Integer.parseInt(jsonObject.get("n_months").toString());
    }

    public List<String> getMonths() {
        JSONArray jsonArray = (JSONArray) jsonObject.get("months");
        List<String> result = new ArrayList<>();
        jsonArray.iterator().forEachRemaining(n -> result.add(n.toString()));
        return result;
    }

    public int getWeekLength() {
        return Integer.parseInt(jsonObject.get("week_len").toString());
    }

    public List<String> getDaysPerWeek() {
        JSONArray jsonArray = (JSONArray) jsonObject.get("weekdays");
        List<String> result = new ArrayList<>();
        jsonArray.iterator().forEachRemaining(n -> result.add(n.toString()));
        return result;
    }

    public int getDaysInMonth(String month) {
        JSONObject jobject = (JSONObject)jsonObject.get("month_len");
        return Integer.parseInt(jobject.get(month).toString());
    }

    public int getDaysInMonth(int month) {
        String monthName = getMonths().get(month);
        JSONObject jobject = (JSONObject)jsonObject.get("month_len");
        return Integer.parseInt(jobject.get(monthName).toString());
    }

    /**
     * Returns the first weekday of the year, as 0-based int
     * @return
     */
    public int getFirstWeekdayOfYear() {
        return Integer.parseInt(jsonObject.get("first_day").toString());
    }

    public boolean cellInMonth(int cellDay, int cellWeek, String month) {
        return cellInMonth(cellDay, cellWeek, getDaysInMonth(month));
    }

    public boolean cellInMonth(int cellDay, int cellWeek, int month) {
        int firstDayOfMonth = getFirstWeekdayOfMonth(month);
        int daysInMonth = getDaysInMonth(month);
        if (cellWeek == 0 && cellDay < firstDayOfMonth) {
            return false;
        } else if (cellDay + (getWeekLength() * cellWeek) >= firstDayOfMonth + daysInMonth) {
            return false;
        }

        return true;
    }

    public int getFirstWeekdayOfMonth(int month) {
        int result = getFirstWeekdayOfYear();

        for (int i = 0; i < month; i++) {
            result = (result + (getDaysInMonth(i) % getWeekLength())) % getWeekLength();
        }

        return result;
    }

    public int getIndexOfMonth(String month) {
        List<String> months = getMonths();
        int cnt = 0;
        for (String m : months) {
            if (m.equalsIgnoreCase(month)) {
                return cnt;
            }
            cnt++;
        }

        return -1;
    }
}
