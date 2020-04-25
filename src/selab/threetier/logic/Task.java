package selab.threetier.logic;

import jdk.nashorn.internal.runtime.ParserException;
import selab.threetier.storage.Storage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;

public class Task extends Entity {
    private String title;
    private Date start;
    private Date end;

    public String getTitle() { return title; }
    public void setTitle(String value) { title = value; }

    public void setStart(Date value) { start = value; }
    public String getStartDate() {
        return new SimpleDateFormat("YYYY-MM-DD").format(start);
    }
    public String getStartTime() {
        return new SimpleDateFormat("HH:mm:ss").format(start);
    }

    public void setEnd(Date value) { end = value; }
    public String getEndTime() {
        return new SimpleDateFormat("HH:mm:ss").format(end);
    }

    public void save() throws IOException {
        if (this.start.compareTo(this.end) > 0)
            throw new IOException("Impossible timing");
        for (Task task: Task.getAll()) {
            if ((task.start.compareTo(this.start) < 0 && this.start.compareTo(task.end) < 0)
                    || (task.start.compareTo(this.end) < 0 && this.end.compareTo(task.end) < 0))
                throw new IOException("Overlapping");
        }
        Storage.getInstance().getTasks().addOrUpdate(this);
    }
    public void delete() {
        Storage.getInstance().getTasks().remove(this);
    }

    public static ArrayList<Task> getAll() {
        ArrayList<Task> tasks = Storage.getInstance().getTasks().getAll();
        tasks.sort((o1, o2) -> o1.start.compareTo(o2.start));
        return tasks;
    }
}
