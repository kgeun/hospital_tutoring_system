package work.kgeun.intelligenttutoringsystem.items;

import java.util.List;

/**
 * Created by kgeun on 2017. 12. 5..
 */

public class Scene {

    String airwayRespiratoryRate,bloodPressureDiastolic,bloodPressureSystolic,heartRate,spO2,temperature;
    String painLocation, nurseQuestionsToPatient, patientAnswers, patientInitialDialog;
    int orderNumber, painLevel, sceneId, lessonId, courseId;
    String[] nurseQuestionsToPatientArray,patientAnswersArray;
    String time;

    public String getAirwayRespiratoryRate() {
        return airwayRespiratoryRate;
    }

    public void setAirwayRespiratoryRate(String airwayRespiratoryRate) {
        this.airwayRespiratoryRate = airwayRespiratoryRate;
    }

    public String getBloodPressureDiastolic() {
        return bloodPressureDiastolic;
    }

    public void setBloodPressureDiastolic(String bloodPressureDiastolic) {
        this.bloodPressureDiastolic = bloodPressureDiastolic;
    }

    public String getBloodPressureSystolic() {
        return bloodPressureSystolic;
    }

    public void setBloodPressureSystolic(String bloodPressureSystolic) {
        this.bloodPressureSystolic = bloodPressureSystolic;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getSpO2() {
        return spO2;
    }

    public void setSpO2(String spO2) {
        this.spO2 = spO2;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPainLocation() {
        return painLocation;
    }

    public void setPainLocation(String painLocation) {
        this.painLocation = painLocation;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getPainLevel() {
        return painLevel;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getNurseQuestionsToPatient() {
        return nurseQuestionsToPatient;
    }

    public void setNurseQuestionsToPatient(String nurseQuestionsToPatient) {
        this.nurseQuestionsToPatient = nurseQuestionsToPatient;
    }

    public String getPatientAnswers() {
        return patientAnswers;
    }

    public void setPatientAnswers(String patientAnswers) {
        this.patientAnswers = patientAnswers;
    }

    public String[] getNurseQuestionsToPatientArray() {
        return nurseQuestionsToPatientArray;
    }

    public void setNurseQuestionsToPatientArray(String[] nurseQuestionsToPatientArray) {
        this.nurseQuestionsToPatientArray = nurseQuestionsToPatientArray;
    }

    public String[] getPatientAnswersArray() {
        return patientAnswersArray;
    }

    public void setPatientAnswersArray(String[] patientAnswersArray) {
        this.patientAnswersArray = patientAnswersArray;
    }

    public String getPatientInitialDialog() {
        return patientInitialDialog;
    }

    public void setPatientInitialDialog(String patientInitialDialog) {
        this.patientInitialDialog = patientInitialDialog;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
