package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PickDateActivity extends AppCompatActivity {

    private static final int FULL = 0; // no time period available to book
    private static final int AVAILABLE = 1; // still available in some periods.
    private static final int EMPTY = 2; // no booking on this day
    private static final String LIBRARY_OPENING_TIME = "700am";
    private static final String LIBRARY_CLOSING_TIME = "800pm";
    private static final int LIBRARY_TOTAL_HOURS_OPEN = 13;
    private static final int IS_1_TO_10 = 0;
    private static final int IS_11_TO_20 = 1;
    private static final int IS_21_TO_30 = 2;


    private DBHelper db;
    private List<Room> allRoomsList;
    private List<Student> allStudentsList;
    private List<RoomBooking> allRoomBookingsList;
    private int datesRange; // 0: 1-10, 1: 11-20, 2:21-30

    private GridLayout datesLayout;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private ArrayList<Calendar> calendarsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        //deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);

        db.importRoomsFromCSV("rooms.csv");
        db.importRoomBookingsFromCSV("roomBookings.csv");

        allStudentsList = db.getAllStudents();
        allRoomsList = db.getAllRooms();
        allRoomBookingsList = db.getAllRoomBookings();

        datesRange = IS_1_TO_10;
        datesLayout = (GridLayout) findViewById(R.id.datesGridLayout);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        previousButton = (ImageButton) findViewById(R.id.previousButton);

        //previousButton.setEnabled(false);
        previousButton.setVisibility(View.INVISIBLE);

        // get current date
        calendarsList = new ArrayList<>();
        for(int i = 0; i < 30; i++)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            calendarsList.add(calendar);

        }

        // load 1st 10 of 30 days ahead from current date
        showDateOnTable( datesRange, calendarsList);

    }

    private void showDateOnTable(int range, ArrayList<Calendar> calendarsList)
    {

        // loop: check each day available status & load data to views
        int llChildCount = datesLayout.getChildCount();
        for(int i = 0; i < llChildCount; i++)
        {
            View childView = datesLayout.getChildAt(i);
            if (childView instanceof LinearLayout) // check if that cell is a LinearLayout
            {
                LinearLayout childLinearLayout = (LinearLayout) childView;
                // Apply Drag listener
                //childLinearLayout.setOnDragListener(new CoderDragListener());

                View grandchildView1 = childLinearLayout.getChildAt(0);
                View grandchildView2 = childLinearLayout.getChildAt(1);
                if (grandchildView1 instanceof TextView && grandchildView2 instanceof TextView) // check if that cell is a TextView
                {
                    TextView childDayOfWeekTextView = (TextView) grandchildView1;
                    TextView childDateTextView = (TextView) grandchildView2;
                    Calendar mCalendar = calendarsList.get(range * 10 + i);

                    int currentDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
                    int curMonth = mCalendar.get(Calendar.MONTH) + 1; // need +1
                    int curDate = mCalendar.get(Calendar.DATE);
                    int curYear = mCalendar.get(Calendar.YEAR);
                    String date = String.valueOf(curMonth)
                            + String.valueOf(curDate)
                            +String.valueOf(curYear);

                    String currentDayString = "";
                    switch (currentDayOfWeek) {
                        case Calendar.SUNDAY:
                            // Current day is Sunday = 1
                            currentDayString = "Sunday";
                            break;
                        case Calendar.MONDAY:
                            // Current day is Monday
                            currentDayString = "Monday";
                            break;
                        case Calendar.TUESDAY:
                            // etc.
                            currentDayString = "Tuesday";
                            break;
                        case Calendar.WEDNESDAY:
                            // etc.
                            currentDayString = "Wednesday";
                            break;
                        case Calendar.THURSDAY:
                            // etc.
                            currentDayString = "Thursday";
                            break;
                        case Calendar.FRIDAY:
                            // etc.
                            currentDayString = "Friday";
                            break;
                        case Calendar.SATURDAY:
                            // = 7
                            currentDayString = "Saturday";
                            break;
                        default:

                    }

                    childDayOfWeekTextView.setText(currentDayString);
                    childDateTextView.setText(curMonth + "/" + curDate);
                    updateBackGroundColor(childLinearLayout, getDateStatus(date));

                    //TODO:  Add the CoderTouchListener to every ImageView within each LinearLayout
                    //childImageView.setOnTouchListener(new CoderTouchListener());
                }

            }
        }
    }
    private int getDateStatus(String date)
    {
        ArrayList<RoomBooking> newList =  new ArrayList<>();
        for(RoomBooking roomBooking : allRoomBookingsList)
        {
            if(roomBooking.getmDate().equals(date))
            {
                newList.add(roomBooking);
            }
        }
        if(!newList.isEmpty())
        {
            int roomAvailable = allRoomsList.size();
            for (Room room : allRoomsList)
            {
                int totalHoursUsed = 0;
                for (RoomBooking booking : newList)
                {
                    if(room.getmId() == booking.getmRoomId())
                    {
                        totalHoursUsed += booking.getmHoursUsed();
                    }
                }

                if(totalHoursUsed == LIBRARY_TOTAL_HOURS_OPEN)
                {
                    roomAvailable--;
                }
            }

            if(roomAvailable != 0)
            {
                return AVAILABLE;
            }
            else
            {
                return FULL;
            }
        }
        else
            return EMPTY;
    }

    private void updateBackGroundColor(LinearLayout linearLayout, int status)
    {
        switch (status)
        {
            case EMPTY:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorEmpty));
                break;
            case AVAILABLE:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorAvailable));
                break;
            case FULL:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorFull));
                break;
        }
    }

    public void loadNext10Dates(View view)
    {
        datesRange++;
        showDateOnTable(datesRange, calendarsList);
        if(datesRange == IS_11_TO_20)
        {
            //previousButton.setEnabled(true);
            previousButton.setVisibility(View.VISIBLE);
        }

        if(datesRange == IS_21_TO_30)
        {
            //nextButton.setEnabled(false);
            nextButton.setVisibility(View.INVISIBLE);
        }

    }

    public void loadPrevious10Dates(View view)
    {
        datesRange--;
        showDateOnTable(datesRange, calendarsList);
        if(datesRange == IS_11_TO_20)
        {
            //nextButton.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
        }

        if(datesRange == IS_1_TO_10)
        {
            //previousButton.setEnabled(false);
            previousButton.setVisibility(View.INVISIBLE);
        }

    }


}
