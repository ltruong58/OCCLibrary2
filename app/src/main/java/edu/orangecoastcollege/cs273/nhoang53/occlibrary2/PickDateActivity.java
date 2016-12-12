package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Pick date activity: first activity of room booking process
 * @author Long Truong
 */
public class PickDateActivity extends AppCompatActivity {

    private static final int FULL_OR_WEEKEND_DAYS = 0; // no time period available to book
    private static final int AVAILABLE = 1; // still available in some periods.
    private static final int EMPTY = 2; // no booking yet on this day
    private static final int LIBRARY_TOTAL_HOURS_OPEN = 13; // Is used to check room/ date is full booked
    private static final int IS_1_TO_10 = 0;
    private static final int IS_11_TO_20 = 1;
    private static final int IS_21_TO_30 = 2;
    private static final int TOTAL_AVAILABLE_DAYS = 30;

    private DBHelper db;
    private List<Room> allRoomsList;
    private List<RoomBooking> allRoomBookingsList;
    private int datesRange; // show 10 of 30 days on view: 0: 1-10, 1: 11-20, 2:21-30

    private GridLayout datesLayout;
    private ImageButton nextButton;
    private ImageButton previousButton;

    private ArrayList<Calendar> calendarsList;
    private TextView datesRangeTextView;

    /** Set up all elements for activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        db = new DBHelper(this);

        allRoomsList = db.getAllRooms();
        allRoomBookingsList = db.getAllRoomBookings();

        datesRange = IS_1_TO_10; // start showing first 10 days

        datesLayout = (GridLayout) findViewById(R.id.datesGridLayout);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        previousButton = (ImageButton) findViewById(R.id.previousButton);
        datesRangeTextView = (TextView) findViewById(R.id.dateRangeTextView);

        // disable privious button
        previousButton.setVisibility(View.INVISIBLE);

        // get 30 days from today
        calendarsList = new ArrayList<>();
        for(int i = 0; i < TOTAL_AVAILABLE_DAYS; i++)
        {
            Calendar nCalendar = Calendar.getInstance();
            nCalendar.add(Calendar.DATE, i);
            calendarsList.add(nCalendar);
        }

        datesRangeTextView.setText(getString(R.string.dates_range,getDateStringDisplay(calendarsList.get(0)),getDateStringDisplay(calendarsList.get(calendarsList.size() - 1))));

        // update views
        showDateOnTable( datesRange, calendarsList);

    }


    /** Get a string of date to display in room layout from input calendar object
     * @param calendar store a date needed to display
     * @return string of the date
     */
    private String getDateStringDisplay(Calendar calendar)
    {
        int curMonth = calendar.get(Calendar.MONTH) + 1; // need +1
        int curDate = calendar.get(Calendar.DATE);
        int curYear = calendar.get(Calendar.YEAR);
        String date = String.valueOf(curMonth) + "/"
                + String.valueOf(curDate) + "/"
                +String.valueOf(curYear);
        return date;
    }

    /** update view of pick date layout
     * @param range decide which days will be showed on screen
     * @param calendarsList list of 30 available days
     */
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

                // set onClick listener
                childLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewRoomOfSelectedDate(view);
                    }
                });

                View grandchildView1 = childLinearLayout.getChildAt(0);
                View grandchildView2 = childLinearLayout.getChildAt(1);
                if (grandchildView1 instanceof TextView && grandchildView2 instanceof TextView) // check if that cell is a TextView
                {
                    TextView childDayOfWeekTextView = (TextView) grandchildView1;
                    TextView childDateTextView = (TextView) grandchildView2;
                    Calendar mCalendar = calendarsList.get(range * 10 + i);

                    int currentDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK); // get day of week

                    // get a string of date
                    int curMonth = mCalendar.get(Calendar.MONTH) + 1; // need +1
                    int curDate = mCalendar.get(Calendar.DATE);
                    int curYear = mCalendar.get(Calendar.YEAR);
                    String date = getString(R.string.get_date_from_calendar, String.valueOf(curMonth)
                            ,String.valueOf(curDate)
                            ,String.valueOf(curYear));


                    String currentDayString = ""; // store day of week
                    switch (currentDayOfWeek) {
                        case Calendar.SUNDAY:
                            // Current day is Sunday = 1
                            currentDayString = "SUN";
                            break;
                        case Calendar.MONDAY:
                            // Current day is Monday
                            currentDayString = "MON";
                            break;
                        case Calendar.TUESDAY:
                            // etc.
                            currentDayString = "TUE";
                            break;
                        case Calendar.WEDNESDAY:
                            // etc.
                            currentDayString = "WED";
                            break;
                        case Calendar.THURSDAY:
                            // etc.
                            currentDayString = "THUR";
                            break;
                        case Calendar.FRIDAY:
                            // etc.
                            currentDayString = "FRI";
                            break;
                        case Calendar.SATURDAY:
                            // = 7
                            currentDayString = "SAT";
                            break;
                        default:

                    }


                    childDayOfWeekTextView.setText(currentDayString);
                    childDateTextView.setText(curMonth + "/" + curDate);

                    if((currentDayString == "SUN"))
                        updateBackGroundColor(childLinearLayout, FULL_OR_WEEKEND_DAYS);
                    else
                        updateBackGroundColor(childLinearLayout, getDateStatus(date));

                    childLinearLayout.setTag(date);
                }

            }
        }
    }


    /** get available status of a date
     * @param date
     * @return
     */
    private int getDateStatus(String date)
    {
        ArrayList<RoomBooking> newList =  new ArrayList<>();

        // filter room bookings of input date
        for(RoomBooking roomBooking : allRoomBookingsList)
        {
            if(roomBooking.getDate().equals(date))
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
                    if(room.getId() == booking.getRoomId())
                    {
                        totalHoursUsed += booking.getHoursUsed();
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
                return FULL_OR_WEEKEND_DAYS;
            }
        }
        else
            return EMPTY;
    }

    /** update background color
     * @param linearLayout
     * @param status
     */
    public void updateBackGroundColor(LinearLayout linearLayout, int status)
    {
        Drawable background = ContextCompat.getDrawable(this, R.drawable.empty_background);
        switch (status)
        {
            case EMPTY:

                break;
            case AVAILABLE:
                background = ContextCompat.getDrawable(this, R.drawable.available_background);
                break;
            case FULL_OR_WEEKEND_DAYS:

                // don't allow to pick date that full booked or weekend days
                linearLayout.setClickable(false);

                background = ContextCompat.getDrawable(this, R.drawable.full_background);
                break;
            default:
        }
        linearLayout.setBackground(background);
    }

    /** onClick method of nextButton: load next 10 days of 30 days
     * @param view
     */
    public void loadNext10Dates(View view)
    {
        if(view instanceof ImageButton)
        {
            datesRange++;
            showDateOnTable(datesRange, calendarsList);
            if(datesRange == IS_11_TO_20)
            {
                previousButton.setVisibility(View.VISIBLE);
            }

            if(datesRange == IS_21_TO_30)
            {
                nextButton.setVisibility(View.INVISIBLE);
            }
        }


    }

    /** onClick method of previousButton: load previous 10 days of 30 days
     * @param view
     */
    public void loadPrevious10Dates(View view)
    {
        if(view instanceof ImageButton)
        {
            datesRange--;
            showDateOnTable(datesRange, calendarsList);
            if(datesRange == IS_11_TO_20)
            {
                nextButton.setVisibility(View.VISIBLE);
            }

            if(datesRange == IS_1_TO_10)
            {
                previousButton.setVisibility(View.INVISIBLE);
            }
        }


    }

    /** onClick button for dateLayout
     * @param view
     */
    public void viewRoomOfSelectedDate(View view)
    {
        if(view instanceof LinearLayout)
        {
            LinearLayout selectedLinearLayout = (LinearLayout) view;
            String selectedDate = (String) selectedLinearLayout.getTag();

            Intent pickRoomIntent = new Intent(this, PickRoomActivity.class);

            pickRoomIntent.putExtra("Date", selectedDate);
            startActivity(pickRoomIntent);
        }
    }


}
