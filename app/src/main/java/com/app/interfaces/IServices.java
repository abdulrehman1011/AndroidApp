package com.app.interfaces;

import com.app.models.AppRateUrlModel;
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
import com.app.network.StudentExamService;

public interface IServices {

    public StudentList GetStudentList(String mobileNo, String id);
    public StudentDetail GetStudentDetail(String studentId);
    public StudentFee GetStudentFee(String studentId);
    public StudentFeeDetail GetStudentFeeMonthDetail(String studentId, String feeMonth);
    public StudentExam GetStudentExams(String studentId);
    public StudentExamDetail GetStudentExamsDetails(String studentId, int semester);
    public _StudentAttendance GetStudentAttendance(String studentId);
    public StudentNotification GetStudentNotifications(String studentId);
    public SchoolProfile GetStudentSchoolProfile(String studentId);
    public StudentLogout LogoutStudent(String id);
    AppRateUrlModel GetAppRateURL(String id);
}
