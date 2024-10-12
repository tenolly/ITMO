package org.server;

import java.util.Date;
import java.text.SimpleDateFormat;
import jakarta.faces.bean.ManagedBean;

@SuppressWarnings("deprecation")
@ManagedBean
public class StartPageBean {
    private String studentName = "Пышкин Никита Сергеевич";
    private String studentGroup = "P3213";
    private String studentNumber = "409429";

    public String getStudentName() {
        return studentName;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(new Date());
    }
}
