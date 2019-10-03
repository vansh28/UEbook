package com.ue.uebook.Dictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class TranslationDataSource {

    private SQLiteDatabase dictionary_long_database;
    private SQLiteDatabase dictionary_brief_database;
    private SQLiteDatabase dictionary_english_database;
    private Context context;

    public TranslationDataSource(Context context)
    {
        this.context = context;
        this.dictionary_long_database = new DataBaseHelper(context, "dictionary_long.sqlite").openDataBase();
        this.dictionary_brief_database = new DataBaseHelper(context, "dictionary_brief.sqlite").openDataBase();
        this.dictionary_english_database = new DataBaseHelper(context, "dictionary_english.sqlite").openDataBase();
    }


    public ArrayList<Translation> getEngEngTranslation(String word)
    {
        Cursor cursor = dictionary_english_database.rawQuery("select Definition from DEFINITIONS where wordID = (select wordID from WORDS where lower(word) = lower(\""+word+"\"));", null);
        cursor.moveToFirst();
        ArrayList<Translation> translation_array = new ArrayList<Translation>();
        for(int i = 0; i < cursor.getCount(); i++)
        {
            translation_array.add(new Translation(word, cursor.getString(0)));
            cursor.moveToNext();
        }
        return translation_array;
    }

}