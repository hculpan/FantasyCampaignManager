package org.culpan.fcm;

import javafx.scene.image.Image;
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
    public static class MoonInfo {
        String name;
        int cycle;
        int offset;
    }

    static private Calendar calendarInstance;

    protected JSONObject jsonObject;

    protected Image [] moonPhases = new Image[8];

    protected List<MoonInfo> moons;

    static public Calendar getInstance() {
        if (calendarInstance == null) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = null;
                jsonObject = (JSONObject)parser.parse(new InputStreamReader(Calendar.class.getClassLoader().getResourceAsStream("calendar.json")));

                calendarInstance = new Calendar(jsonObject);
                calendarInstance.moonPhases[0] = new Image(Calendar.class.getClassLoader().getResourceAsStream("images/moon1.png"));
                calendarInstance.moonPhases[1] = new Image(Calendar.class.getClassLoader().getResourceAsStream("images/moon2.png"));
                calendarInstance.moonPhases[2] = new Image(Calendar.class.getClassLoader().getResourceAsStream("images/moon3.png"));
                calendarInstance.moonPhases[3] = new Image(Calendar.class.getClassLoader().getResourceAsStream("images/moon4.png"));
                calendarInstance.moonPhases[4] = new Image(Calendar.class.getClassLoader().getResourceAsStream("images/moon5.png"));
                calendarInstance.moonPhases[5] = new Image(Calendar.class.getClassLoader().getResourceAsStream("images/moon6.png"));
                calendarInstance.moonPhases[6] = new Image(Calendar.class.getClassLoader().getResourceAsStream("images/moon7.png"));
                calendarInstance.moonPhases[7] = new Image(Calendar.class.getClassLoader().getResourceAsStream("images/moon8.png"));

            } catch (IOException | ParseException e) {
                throw new RuntimeException("Unable to load calendar info", e);
            }
        }
        return calendarInstance;
    }

    private Calendar(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getStartingYear() {
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
        String monthName = getMonths().get(month % getMonthsCount());
        JSONObject jobject = (JSONObject)jsonObject.get("month_len");
        return Integer.parseInt(jobject.get(monthName).toString());
    }

    /**
     * Returns the first weekday of the year, as 0-based int
     * @return
     */
    public int getFirstWeekdayOfStartingYear() {
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
        int result = getFirstWeekdayOfStartingYear();

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

    public int getMoonCount() {
        return Integer.parseInt(jsonObject.get("n_moons").toString());
    }

    public List<MoonInfo> getMoons() {
        if (moons == null) {
            moons = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) jsonObject.get("moons");
            List<String> result = new ArrayList<>();
            jsonArray.iterator().forEachRemaining(n -> {
                MoonInfo moonInfo = new MoonInfo();
                moonInfo.name = n.toString();
                moonInfo.cycle = Integer.parseInt(((JSONObject)jsonObject.get("lunar_cyc")).get(moonInfo.name).toString());
                moonInfo.offset = Integer.parseInt(((JSONObject)jsonObject.get("lunar_shf")).get(moonInfo.name).toString());
                moons.add(moonInfo);
            });
        }
        return moons;
    }

    public int daysSinceStart(int dayOfMonth, int month) {
        int result = 0;

        for (int i = 0; i < month; i++) {
            result += getDaysInMonth(i);
        }

        result += dayOfMonth;

        return result;
    }

    public int getMoonPhase(int moon, int dayOfMonth, int month) {
        List<MoonInfo> moons = getMoons();
        return getMoonPhase(moons.get(moon), dayOfMonth, month);
    }

    public int getMoonPhase(MoonInfo moonInfo, int dayOfMonth, int month) {
        double interval = moonInfo.cycle / 8.0;
        int daysSinceStart = daysSinceStart(dayOfMonth, month) + moonInfo.offset + 1;
        int modResult = (daysSinceStart % moonInfo.cycle);
        return (int)(modResult / interval);
    }

    public Image getMoonPhaseImage(MoonInfo moonInfo, int dayOfMonth, int month) {
        return moonPhases[getMoonPhase(moonInfo, dayOfMonth, month)];
    }

    public Image getMoonPhaseImage(int moon, int dayOfMonth, int month) {
        return moonPhases[getMoonPhase(moon, dayOfMonth, month)];
    }
}
