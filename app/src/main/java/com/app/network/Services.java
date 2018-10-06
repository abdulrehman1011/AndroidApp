package com.app.network;

import android.app.Application;
import android.content.Context;

import com.app.models.AppRateUrlModel;
import com.app.models.EmployeeList;
import com.app.models.SchoolProfile;
import com.app.models.EmployeeDetail;
import com.app.models.StudentExam;
import com.app.models.StudentExamDetail;
import com.app.models.StudentFee;
import com.app.models.StudentFeeDetail;
import com.app.models.EmployeeLogout;
import com.app.models.EmployeeNotification;
import com.app.models._EmployeeAttendance;

public class Services implements com.app.interfaces.IServices{
    Application appObj;
    public Services(Context context, Application app)
    {
        this.appObj = app;
    }
    @Override
    public EmployeeList GetStudentList(String mobileNo, String playerId) {
        EmployeeList studentObject;
        try {
            EmployeeServices stdService = new EmployeeServices(appObj);
            studentObject = stdService.getEmployees(mobileNo,playerId);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public EmployeeDetail GetStudentDetail(String studentId) {
        EmployeeDetail studentObject;
        try {
            EmployeeDataService stdService = new EmployeeDataService(appObj);
            studentObject = stdService.getEmployeeDetail(studentId);
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
    public _EmployeeAttendance GetStudentAttendance(String studentId) {
        _EmployeeAttendance studentObject;
        try {
            EmployeeAttendanceService stdService = new EmployeeAttendanceService(appObj);
            studentObject = stdService.getEmployeeAttendance(studentId);
            return studentObject;
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    @Override
    public EmployeeNotification GetStudentNotifications(String studentId) {
        EmployeeNotification studentObject;
        try {
            EmployeeNotificationService stdService = new EmployeeNotificationService(appObj);
            studentObject = stdService.getEmployeeNotification(studentId);
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
    public EmployeeLogout LogoutStudent(String id) {
        EmployeeLogout employeeLogout;
        try {
            EmployeeLogoutService stdLogoutService = new EmployeeLogoutService(appObj);
            employeeLogout = stdLogoutService.logoutStudent(id);
            return employeeLogout;
        }
        catch (Exception ex)
        {
        }
        return null;
    }
    @Override
    public AppRateUrlModel GetAppRateURL(String id) {
        try {
            AppRateUrlService appRateUrlService = new AppRateUrlService(appObj);

            return appRateUrlService.getRateUrl(id);
        }
        catch (Exception ex)
        {
        }
        return null;
    }
}
