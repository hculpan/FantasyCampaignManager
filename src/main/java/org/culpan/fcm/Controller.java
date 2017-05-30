package org.culpan.fcm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Canvas canvasMainView;

    @FXML
    ScrollPane scrollPaneMainView;

    @FXML
    Button btnPreviousMonth;

    @FXML
    Button btnNextMonth;

    final static int CellWidth = 100;

    final static int CellHeight = 125;

    final static int HeaderHeight = 50;

    final static Color BackgroundColor = Color.TRANSPARENT;

    int currentMonth = 0;

    @FXML
    protected void btnLoadOnAction(ActionEvent ac) {
        System.out.println("Got event!");
    }

    @FXML
    protected void nextMonth(ActionEvent ac) {
        currentMonth++;
        btnPreviousMonth.setDisable(false);
        drawMonth(canvasMainView, currentMonth);
    }

    @FXML
    protected void previousMonth(ActionEvent ac) {
        currentMonth--;
        if (currentMonth == 0) {
            btnPreviousMonth.setDisable(true);
        }
        drawMonth(canvasMainView, currentMonth);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (canvasMainView != null) {
            Calendar c = Calendar.getInstance();
            int spacing = 5;
            int numDays = c.getWeekLength(), numWeeks = 8;

            canvasMainView.setHeight((CellHeight + spacing) * numWeeks + HeaderHeight + spacing);
            canvasMainView.setWidth((CellWidth + spacing) * numDays + 1 + spacing);

            btnPreviousMonth.setDisable(true);
            drawMonth(canvasMainView, currentMonth);
        } else {
            throw new RuntimeException("main canvas not initialized!");
        }
    }

    private void drawMonth(Canvas canvas, int monthNum) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Calendar c = Calendar.getInstance();
        int spacing = 5;
        int numDays = c.getWeekLength(), numWeeks = 8;

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);

        gc.setFont(new Font("Arial", 24));
        gc.fillText(String.format("Year: %4d", c.getStartingYear() + (monthNum / c.getMonthsCount())), 20, 30);

        String msg = String.format("Month: %s", c.getMonths().get(monthNum % c.getMonthsCount()));
        double twidth = computeTextWidth(gc.getFont(), msg, Double.MAX_VALUE);
        gc.fillText(msg , canvas.getWidth() - (twidth + 25), 30);

        for (int d = 0; d < numDays; d++) {
            String dayName = c.getDaysPerWeek().get(d);
            gc.setFont(new Font("Arial", 11));
            twidth = computeTextWidth(gc.getFont(), dayName, Double.MAX_VALUE);
            int x = (int)((((CellWidth * d) + (spacing * d) + spacing) + (CellWidth / 2)) - (twidth / 2));
            gc.fillText(dayName, x, 50);
        }

        gc.setFont(new Font("Arial", 20));
        int dayOfMonth = 0;
        for (int w = 0; w < numWeeks; w++) {
            for (int d = 0; d < numDays; d++) {
                dayOfMonth = drawCell(gc, d, w, monthNum, dayOfMonth, spacing);
            }
        }
    }

    private int drawCell(GraphicsContext gc, int day, int week, int month, int dayOfMonth, int spacing) {
        int x = (CellWidth * day) + (spacing * day) + spacing;
        int y = (CellHeight * week) + (spacing * week) + spacing + HeaderHeight;
        int result = dayOfMonth;
        boolean cellInMonth = Calendar.getInstance().cellInMonth(day, week, month);

        if (cellInMonth) {
            gc.setStroke(Color.GRAY);
            gc.fillText(Integer.toString(result + 1), x + 6, y + 22);

            int idx = 0;
            for (Calendar.MoonInfo moonInfo : Calendar.getInstance().getMoons()) {
                gc.drawImage(Calendar.getInstance().getMoonPhaseImage(moonInfo, dayOfMonth, month), x + 65, y + (36 * idx));
                idx++;
            }

            result += 1;
        } else {
            gc.setStroke(BackgroundColor);
        }

        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, CellWidth, CellHeight, 20, 20);

        return result;
    }

    double computeTextWidth(Font font, String text, double wrappingWidth) {
        Text helper = new Text();
        helper.setFont(font);
        helper.setText(text);
        // Note that the wrapping width needs to be set to zero before
        // getting the text's real preferred width.
        helper.setWrappingWidth(0);
        helper.setLineSpacing(0);
        double w = Math.min(helper.prefWidth(-1), wrappingWidth);
        helper.setWrappingWidth((int)Math.ceil(w));
        double textWidth = Math.ceil(helper.getLayoutBounds().getWidth());
        return textWidth;
    }


}
