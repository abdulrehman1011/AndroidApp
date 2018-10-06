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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface APIInterface {


    @GET("/api/login?")
    Call<EmployeeList> getStudentList(@Query("emp_number") String emp_number, @Query("token") String token);

    @GET("/api/login?")
    Call<EmployeeLogout> logoutStudent(@Query("emp_logout") String token);

    @GET("/api/employee?")
    Call<EmployeeDetail> getStudentDetail(@Query("employee_id") String employee_id);

    @GET("/api/attendance?")
    Call<_EmployeeAttendance> getStudentAttendance(@Query("emp_id") String emp_id);

    @GET("/api/exams?")
    Call<StudentExam> getStudentExamInfo(@Query("student_id") String student_id);

    @GET("/api/exams")
    Call<StudentExamDetail> getStudentExamDetail(@Query("student_id") String student_id,@Query("mark_role") int mark_role);

    @GET("/api/fee?")
    Call<StudentFee> getStudentFeeInfo(@Query("std_id") String std_id);

    @GET("/api/fee?")
    Call<StudentFeeDetail> getStudentFeeDetail(@Query("student_id") String student_id,@Query("fee_month") String fee_month);

    @GET("/api/Employee?")
    Call<SchoolProfile> getSchoolProfile(@Query("emp_id") String emp_id);

    @GET("/api/Notification?")
    Call<EmployeeNotification> getStudentNotifications(@Query("emp_id") String emp_id);

    @GET("/api/Employee?")
    Call<AppRateUrlModel> getRateURL(@Query("rate_url") String rate_url);

    @GET("/api/Fee?")
    @Streaming
    Call<ResponseBody> getStudentFeeVoucher(@Query("std_id") String std_id, @Query("fee_month") String fee_month);
}
