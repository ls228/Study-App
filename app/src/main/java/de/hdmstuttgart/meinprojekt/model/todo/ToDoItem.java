package de.hdmstuttgart.meinprojekt.model.todo;

import java.util.Date;

public class ToDoItem {

    private final String title;
    private final Date date;
    private final String studyTopic;

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getActor() {
        return studyTopic;
    }


    public ToDoItem(
            String title,
            Date date,
            String studyTopic
    ) {
        this.title = title;
        this.date = date;
        this.studyTopic = studyTopic;
    }


}
