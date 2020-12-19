package work.kgeun.intelligenttutoringsystem.items;

/**
 * Created by kgeun on 2017. 12. 13..
 */

public class Lesson {
    String courseId, lessonId;
    String difficulty, lessonDescription,lessonName;

    public Lesson(String courseId, String lessonId, String difficulty, String lessonDescription, String lessonName) {
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.difficulty = difficulty;
        this.lessonDescription = lessonDescription;
        this.lessonName = lessonName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLessonDescription() {
        return lessonDescription;
    }

    public void setLessonDescription(String lessonDescription) {
        this.lessonDescription = lessonDescription;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }
}
