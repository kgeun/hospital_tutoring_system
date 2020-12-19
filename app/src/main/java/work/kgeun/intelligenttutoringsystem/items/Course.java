package work.kgeun.intelligenttutoringsystem.items;

/**
 * Created by kgeun on 2017. 11. 30..
 */

public class Course {
    public String courseId;
    public String courseName;
    public String courseDescription;
    public String difficulty;
    public String numberOfLessons;

    public Course(String courseId, String courseName, String courseDescription, String difficulty, String numberOfLessons) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.difficulty = difficulty;
        this.numberOfLessons = numberOfLessons;
    }
}
