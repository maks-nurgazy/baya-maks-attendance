package project.manas.attendance.service;

import project.manas.attendance.entity.Student;
import project.manas.attendance.form.Statistic;

import java.util.List;

public interface StatisticService {
    Statistic getStatistic();

    Statistic getStudentStatic(List<Student> teacherStudents);
}
