package com.app.network;

import android.app.Application;
import android.content.Context;

import com.app.models.SchoolProfile;
import com.app.models.StudentAttendance;
import com.app.models.StudentDetail;
import com.app.models.StudentExam;
import com.app.models.StudentExamDetail;
import com.app.models.StudentFee;
import com.app.models.StudentFeeDetail;
import com.app.models.StudentList;
import com.app.models.StudentLogout;
import com.app.models.StudentNotification;
import com.app.models._StudentAttendance;
import com.app.utils.Util;

public class Services implements com.app.interfaces.IServices{
    Application appObj;
    public Services(Context context, Application app)
    {
        this.appObj = app;
    }
    @Override
    public StudentList GetStudentList(String mobileNo, String id) {
        StudentList studentObject;
        try {
            StudentServices stdService = new StudentServices(appObj);
            studentObject = stdService.getStudents(mobileNo,id);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public StudentDetail GetStudentDetail(String studentId) {
        StudentDetail studentObject;
        try {
            StudentDataService stdService = new StudentDataService(appObj);
            studentObject = stdService.getStudentDetail(studentId);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public StudentFee GetStudentFee(String studentId) {
        StudentFee studentFeeObject;
        try {
            StudentFeeService stdService = new StudentFeeService(appObj);
            studentFeeObject = stdService.getStudentFeeInfo(studentId);
            return studentFeeObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public StudentFeeDetail GetStudentFeeMonthDetail(String studentId, String feeMonth) {
        StudentFeeDetail studentFeeDetailObject;
        try {
            StudentFeeMonthService stdService = new StudentFeeMonthService(appObj);
            studentFeeDetailObject = stdService.getStudentFeeDetail(studentId, feeMonth);
            return studentFeeDetailObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public StudentExam GetStudentExams(String studentId) {
        StudentExam studentObject;
        try {
            StudentExamService stdService = new StudentExamService(appObj);
            studentObject = stdService.getStudentExamInfo(studentId);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public StudentExamDetail GetStudentExamsDetails(String studentId, int semester) {
        StudentExamDetail studentObject;
        try {
            StudentExamMarksDetailService stdService = new StudentExamMarksDetailService(appObj);
            studentObject = stdService.getStudentExamDetail(studentId, semester);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public _StudentAttendance GetStudentAttendance(String studentId) {
        _StudentAttendance studentObject;
        try {
            StudentAttendanceService stdService = new StudentAttendanceService(appObj);
            studentObject = stdService.getStudentAttendance(studentId);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public StudentNotification GetStudentNotifications(String studentId) {
        StudentNotification studentObject;
        try {
            StudentNotificationService stdService = new StudentNotificationService(appObj);
            studentObject = stdService.getStudentNotification(studentId);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public SchoolProfile GetStudentSchoolProfile(String studentId) {
        SchoolProfile studentObject;
        try {
            StudentSchoolProfileService stdService = new StudentSchoolProfileService(appObj);
            studentObject = stdService.getSchoolProfile(studentId);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
}

    @Override
    public StudentLogout LogoutStudent(String id) {
        StudentLogout studentLogout;
        try {
            StudentLogoutService stdLogoutService = new StudentLogoutService(appObj);
            studentLogout = stdLogoutService.logoutStudent(id);
            return studentLogout;
        }
        catch (Exception ex)
        {
        }
        return null;
    }
}
