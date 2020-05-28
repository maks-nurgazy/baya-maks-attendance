package project.manas.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.manas.attendance.entity.Student;
import project.manas.attendance.form.Statistic;
import project.manas.attendance.model.Gender;
import project.manas.attendance.model.Role;
import project.manas.attendance.service.StatisticService;
import project.manas.attendance.service.StudentService;
import project.manas.attendance.service.UserService;

import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    UserService userService;
    StudentService studentService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Statistic getStatistic() {
        int nStudent = userService.getNumberOf(Role.STUDENT);
        int nTeacher = userService.getNumberOf(Role.TEACHER);
        int nGirls = studentService.countByGender(Gender.FEMALE);
        int nBoys = studentService.countByGender(Gender.MALE);

        return Statistic.builder()
                .nBoys(nBoys)
                .nGirls(nGirls)
                .nStudent(nStudent)
                .nTeacher(nTeacher)
                .build();
    }

    @Override
    public Statistic getStudentStatic(List<Student> teacherStudents) {
        int male = 0;
        int female = 0;
        for (Student student : teacherStudents) {
            switch (student.getGender()) {
                case MALE:
                    male++;
                    break;
                case FEMALE:
                    female++;
            }
        }
        return Statistic.builder()
                .nStudent(teacherStudents.size())
                .nBoys(male)
                .nGirls(female)
                .build();
    }
}
