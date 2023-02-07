package de.hdmstuttgart.meinprojekt.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ToDoItem {

    @ColumnInfo(name = "title")
    private final String title;
    @ColumnInfo(name = "date")
    private final String date;
    @ColumnInfo(name = "topic")
    private final String topic;
    @ColumnInfo(name = "status")
    private final boolean status;
    @PrimaryKey(autoGenerate = true)
    public int uid;


    public ToDoItem(
            String title,
            String date,
            String topic,
            boolean status
    ) {
        this.title = title;
        this.date = date;
        this.topic = topic;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }

    public boolean getStatus() {
        return status;
    }

    public int getId() {
        return uid;
    }


}
