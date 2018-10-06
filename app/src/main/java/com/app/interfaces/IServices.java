package com.app.interfaces;

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

public interface IServices {

    public EmployeeList GetStudentList(String mobileNo, String playerId);
    public EmployeeDetail GetStudentDetail(String studentId);
    public StudentFee GetStudentFee(String studentId);
    public StudentFeeDetail GetStudentFeeMonthDetail(String studentId, String feeMonth);
    public StudentExam GetStudentExams(String studentId);
    public StudentExamDetail GetStudentExamsDetails(String studentId, int semester);
    public _EmployeeAttendance GetStudentAttendance(String studentId);
    public EmployeeNotification GetStudentNotifications(String studentId);
    public SchoolProfile GetStudentSchoolProfile(String studentId);
    AppRateUrlModel GetAppRateURL(String id);
    public EmployeeLogout LogoutStudent(String id);
}
