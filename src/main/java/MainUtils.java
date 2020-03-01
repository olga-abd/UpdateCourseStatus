import pkg.course.*;
import pkg.staff.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public class MainUtils {
    public static void run (){
        CourseDAO courseDAO = new CourseDAO();
        // получаем список курсов
        List<Course> courses = courseDAO.getAllCourses();

        // текущая дата
        Date currentDate = new Date(System.currentTimeMillis());

        for (Course course : courses){
            // даты начала/окончания курса
            Date startDate = course.getDateStart();
            Date endDate = course.getDateEnd();

            // если текущая дата между
            if (startDate.toLocalDate().compareTo(currentDate.toLocalDate()) <= 0 &&
                    endDate.toLocalDate().compareTo(currentDate.toLocalDate()) >= 0) {
                Set<EmployeeCourse> employeeCourses = course.getEmployeeCourses();

                // то для всех записанных на курс меняем статус на INPROGRESS, если статус другой
                for (EmployeeCourse employeeCourse : employeeCourses) {
                    if (employeeCourse.getStatus() != CourseStatus.INPROGRESS) {
                        employeeCourse.setStatus(CourseStatus.INPROGRESS);
                        EmployeeCourseDAO ecDAO = new EmployeeCourseDAO();
                        ecDAO.update(employeeCourse);
                    }
                }
            }
            // если текущая дата после окончания курса
            else if (endDate.toLocalDate().compareTo(currentDate.toLocalDate()) < 0) {
                Set<EmployeeCourse> employeeCourses = course.getEmployeeCourses();

                // для всех записанных на курс меняем статус на COMPLETED, если статус другой
                for (EmployeeCourse employeeCourse : employeeCourses) {
                    if (employeeCourse.getStatus() != CourseStatus.COMPLETED) {
                        employeeCourse.setStatus(CourseStatus.COMPLETED);
                        EmployeeCourseDAO ecDAO = new EmployeeCourseDAO();
                        ecDAO.update(employeeCourse);
                    }
                }
            }
        }

    }
}
