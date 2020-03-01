import pkg.course.*;
import pkg.staff.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public class MainUtils {
    public static void run (){
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.getAllCourses();

        Date currentDate = new Date(System.currentTimeMillis());

        for (Course course : courses){
            Date startDate = course.getDateStart();
            Date endDate = course.getDateEnd();

            if (startDate.toLocalDate().compareTo(currentDate.toLocalDate()) <= 0 &&
                    endDate.toLocalDate().compareTo(currentDate.toLocalDate()) >= 0) {
                Set<EmployeeCourse> employeeCourses = course.getEmployeeCourses();

                for (EmployeeCourse employeeCourse : employeeCourses) {
                    if (employeeCourse.getStatus() != CourseStatus.INPROGRESS) {
                        employeeCourse.setStatus(CourseStatus.INPROGRESS);
                        EmployeeCourseDAO ecDAO = new EmployeeCourseDAO();
                        ecDAO.update(employeeCourse);
                    }
                }
            }
            else if (endDate.toLocalDate().compareTo(currentDate.toLocalDate()) < 0) {
                Set<EmployeeCourse> employeeCourses = course.getEmployeeCourses();
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
