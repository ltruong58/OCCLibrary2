package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Joseph on 11/23/2016.
 */

class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    static final String DATABASE_NAME = "OCCLibrary";
    private static final String DATABASE_BOOK_TABLE = "Books";
    private static final String DATABASE_ROOM_TABLE = "Rooms";
    private static final String DATABASE_ROOMBOOKING_TABLE = "RoomBooking";
    private static final String DATABASE_STUDENT_TABLE = "Student";
    private static final int DATABASE_VERSION = 1;



    //TASK 2: DEFINE THE FIELDS (COLUMN NAMES) FOR THE BOOK TABLE
    private static final String BOOK_KEY_FIELD_ID = "id";
    private static final String BOOK_FIELD_TITLE = "title";
    private static final String BOOK_FIELD_DESCRIPTION = "description";
    private static final String BOOK_FIELD_AUTHOR = "author";
    private static final String BOOK_FIELD_ISBN = "isbn";
    private static final String BOOK_FIELD_QTY_AVAIL = "quantity";
    private static final String BOOK_FIELD_IMAGE_URI = "image_uri";

    // DEFINE THE FIELDS (COLUMN NAMES) FOR THE ROOM TABLE
    private static final String ROOM_KEY_FIELD_ID = "id";
    private static final String ROOM_FIELD_NAME = "name";
    private static final String ROOM_FIELD_DESCRIPTION = "description"; // location and list of support devices
    private static final String ROOM_FIELD_CAPACITY = "capacity";

    // DEFINE THE FIELDS (COLUMN NAMES) FOR THE ROOM BOOKING TABLE
    private static final String ROOM_BOOKING_KEY_FIELD_ID = "room_booking_id";
    private static final String ROOM_BOOKING_FIELD_ROOM_ID = "room_id";
    private static final String ROOM_BOOKING_FIELD_STUDENT_ID = "student_id";
    private static final String ROOM_BOOKING_FIELD_DATE = "date"; // location and list of support devices
    private static final String ROOM_BOOKING_FIELD_START_TIME = "start_time";
    private static final String ROOM_BOOKING_FIELD_HOURS_USED = "hours_used";

    // DEFINE THE FIELDS (COLUMN NAMES) FOR THE STUDENT TABLE
    private static final String STUDENT_KEY_FIELD_ID = "student_id";
    private static final String STUDENT_KEY_FIELD_LAST_NAME = "last_name";
    private static final String STUDENT_KEY_FIELD_FIRST_NAME = "first_name";
    private static final String STUDENT_KEY_FIELD_PASSWORD = "password";
    private static final String STUDENT_KEY_FIELD_NO_SHOW_TIMES = "no_show_times";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // BOOK
        String table = "CREATE TABLE " + DATABASE_BOOK_TABLE + "("
                + BOOK_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BOOK_FIELD_TITLE + " TEXT, "
                + BOOK_FIELD_DESCRIPTION + " TEXT, "
                + BOOK_FIELD_AUTHOR + " TEXT, "
                + BOOK_FIELD_ISBN + " INTEGER, "
                + BOOK_FIELD_QTY_AVAIL + " INTEGER, "
                + BOOK_FIELD_IMAGE_URI + " TEXT" + ")";
        sqLiteDatabase.execSQL (table);

        // ROOM
        table = "CREATE TABLE " + DATABASE_ROOM_TABLE + "("
                + ROOM_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROOM_FIELD_NAME + " TEXT, "
                + ROOM_FIELD_DESCRIPTION + " TEXT, "
                + ROOM_FIELD_CAPACITY + " INTEGER" + ")";
        sqLiteDatabase.execSQL (table);

        //STUDENT
        table = "CREATE TABLE " + DATABASE_STUDENT_TABLE + "("
                + STUDENT_KEY_FIELD_ID + " INTEGER, "
                + STUDENT_KEY_FIELD_FIRST_NAME + " TEXT, "
                + STUDENT_KEY_FIELD_LAST_NAME + " TEXT, "
                + STUDENT_KEY_FIELD_PASSWORD + " TEXT,"
                + STUDENT_KEY_FIELD_NO_SHOW_TIMES + " INTEGER)";
        sqLiteDatabase.execSQL(table);

        //ROOM_BOOKING
        table = "CREATE TABLE " + DATABASE_ROOMBOOKING_TABLE_TABLE + "("
                + ROOM_BOOKING_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "FOREIGN KEY(" + ROOM_BOOKING_FIELD_ROOM_ID + ") REFERENCES "
                + DATABASE_ROOM_TABLE + "(" + ROOM_KEY_FIELD_ID + "),"
                + "FOREIGN KEY(" + ROOM_BOOKING_FIELD_STUDENT_ID + ") REFERENCES "
                + DATABASE_STUDENT_TABLE + "(" + STUDENT_KEY_FIELD_ID + "),"
                + ROOM_BOOKING_FIELD_DATE + " TEXT, "
                + ROOM_BOOKING_FIELD_START_TIME + " TEXT, "
                + ROOM_BOOKING_FIELD_HOURS_USED + " INTEGER" + ")";
        sqLiteDatabase.execSQL (table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_BOOK_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_ROOMBOOKING_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_ROOM_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + DATABASE_STUDENT_TABLE);

        onCreate(sqLiteDatabase);
    }

    // import data from csv file
    public boolean importStudentFromCSV(String csvFileName)
    {
        AssetManager am = mContext.getAssets();
        InputStream stream = null;
        try{
            stream = am.open(csvFileName);
        }catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
        String line;
        try{
            while((line = buffer.readLine()) != null){
                String[] fields = line.split(",");
                if(fields.length != 5)
                {
                    Log.d("OCC Library", "Skipping bad csv row: " + Arrays.toString(fields));
                    continue;
                }

                int id = Integer.parseInt(fields[0].trim()); // trim will cut off space behind
                String lastName = fields[1].trim();
                String firstName = fields[2].trim();
                String password = fields[3].trim();
                int noShowTimes = Integer.parseInt(fields[4].trim());

                addStudent(new Student(id, lastName, firstName, password, noShowTimes));
            }
        }catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //********* STUDENT TABLE OPERATIONS: ADD, GET ALL, UPDATE, DELETE

    public void addStudent(Student student)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STUDENT_KEY_FIELD_ID, student.getId());
        values.put(STUDENT_KEY_FIELD_LAST_NAME, student.getLastName());
        values.put(STUDENT_KEY_FIELD_FIRST_NAME, student.getFirstName());
        values.put(STUDENT_KEY_FIELD_PASSWORD, student.getPassword());
        values.put(STUDENT_KEY_FIELD_NO_SHOW_TIMES, student.getNoShowTimes());

        db.insert(DATABASE_STUDENT_TABLE, null, values);
        db.close();
    }

    public ArrayList<Student> getAllStudents(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Student> studentsList = new ArrayList<>();

        Cursor cursor = db.query(DATABASE_STUDENT_TABLE, null, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            do{
                Student student = new Student(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4));
                studentsList.add(student);
            }while (cursor.moveToNext());
        }

        return studentsList;
    }

    public void updateNoShowTimes(Student student)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_KEY_FIELD_NO_SHOW_TIMES, student.getNoShowTimes() + 1);

        db.update(DATABASE_STUDENT_TABLE, values, STUDENT_KEY_FIELD_ID + " =?",
                new String[] {String.valueOf(student.getId())});
        db.close();
    }


    //********** BOOK DATABASE OPERATIONS:  ADD, GETALL, EDIT, DELETE

    public void addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //ADD KEY-VALUE PAIR INFORMATION FOR THE TABLE
        values.put(BOOK_FIELD_TITLE, book.getTitle());
        values.put(BOOK_FIELD_DESCRIPTION, book.getDescription());
        values.put(BOOK_FIELD_AUTHOR, book.getAuthor());
        values.put(BOOK_FIELD_ISBN, book.getISBN());
        values.put(BOOK_FIELD_QTY_AVAIL, book.getQty());
        values.put(BOOK_FIELD_IMAGE_URI, book.getImageUri().toString());

        // INSERT THE ROW IN THE TABLE
        db.insert(DATABASE_BOOK_TABLE, null, values);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> bookList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        //Cursor cursor = database.rawQuery(queryList, null);
        Cursor cursor = database.query(
                DATABASE_BOOK_TABLE,
                new String[]{BOOK_KEY_FIELD_ID, BOOK_FIELD_TITLE, BOOK_FIELD_DESCRIPTION, BOOK_FIELD_AUTHOR, BOOK_FIELD_ISBN, BOOK_FIELD_QTY_AVAIL, BOOK_FIELD_IMAGE_URI},
                null,
                null,
                null, null, null, null );

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()){
            do {
                Book book =
                        new Book(cursor.getInt(0), //iD
                                cursor.getString(1),//title
                                cursor.getString(2),//desc
                                cursor.getString(3),//author
                                cursor.getInt(4),//isbn
                                cursor.getInt(5),//available
                                Uri.parse(cursor.getString(6))//imageUri
                        );
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        return bookList;
    }

    public void deleteBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETE THE TABLE ROW
        db.delete(DATABASE_BOOK_TABLE, BOOK_KEY_FIELD_ID + " = ?",
                new String[] {String.valueOf(book.getId())});
        db.close();
    }

    public void deleteAllBooks()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_BOOK_TABLE, null, null);
        db.close();
    }

    public void updateBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BOOK_FIELD_TITLE, book.getTitle());
        values.put(BOOK_FIELD_DESCRIPTION, book.getDescription());
        values.put(BOOK_FIELD_AUTHOR, book.getAuthor());
        values.put(BOOK_FIELD_ISBN, book.getISBN());
        values.put(BOOK_FIELD_QTY_AVAIL, book.getQty());
        values.put(BOOK_FIELD_IMAGE_URI, book.getImageUri().toString());

        db.update(DATABASE_BOOK_TABLE, values, BOOK_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
        db.close();
    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DATABASE_BOOK_TABLE,
                new String[]{BOOK_KEY_FIELD_ID, BOOK_FIELD_TITLE, BOOK_FIELD_DESCRIPTION, BOOK_FIELD_AUTHOR, BOOK_FIELD_ISBN, BOOK_FIELD_QTY_AVAIL, BOOK_FIELD_IMAGE_URI},
                KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        Book book = new Book(
                cursor.getInt(0), //iD
                cursor.getString(1),//title
                cursor.getString(2),//desc
                cursor.getString(3),//author
                cursor.getInt(4),//isbn
                cursor.getInt(5),//available
                Uri.parse(cursor.getString(6))//imageUri
        );

        db.close();
        return book;
    }
}