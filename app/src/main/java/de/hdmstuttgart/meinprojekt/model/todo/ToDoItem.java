package de.hdmstuttgart.meinprojekt.model.todo;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import de.hdmstuttgart.meinprojekt.database.Converter;

@Entity
public class ToDoItem {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "date")
    private final String date;

    @ColumnInfo(name = "topic")
    private final String topic;

    @ColumnInfo(name = "status")
    private final Integer status;


    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }

    public Integer getStatus(){ return status;}

    public Integer getId(){ return uid;}


    public ToDoItem(
            String title,
            String date,
            String topic,
            Integer status
    ) {
        this.title = title;
        this.date = date;
        this.topic = topic;
        this.status = status;
    }


}
