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
import com.app.models.StudentNotification;
import com.app.models._StudentAttendance;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface APIInterface {


    @GET("/api/login?")
    Call<StudentList> getStudentList(@Query("mobile_number") String mobile_number,@Query("token") String token);

    @GET("/api/student?")
    Call<StudentDetail> getStudentDetail(@Query("student_id") String student_id);

    @GET("/api/attendance?")
    Call<_StudentAttendance> getStudentAttendance(@Query("student_id") String student_id);

    @GET("/api/exams?")
    Call<StudentExam> getStudentExamInfo(@Query("student_id") String student_id);

    @GET("/api/exams")
    Call<StudentExamDetail> getStudentExamDetail(@Query("student_id") String student_id,@Query("mark_role") int mark_role);

    @GET("/api/fee?")
    Call<StudentFee> getStudentFeeInfo(@Query("std_id") String std_id);

    @GET("/api/fee?")
    Call<StudentFeeDetail> getStudentFeeDetail(@Query("student_id") String student_id,@Query("fee_month") String fee_month);

    @GET("/api/Student?")
    Call<SchoolProfile> getSchoolProfile(@Query("std_id") String std_id);

    @GET("/api/Notification?")
    Call<StudentNotification> getStudentNotifications(@Query("student_id") String student_id);

    @GET("/api/student?")
    Call<AppRateUrlModel> getRateURL(@Query("rate_url") String id);

    @GET("/api/Fee?")
    @Streaming
    Call<ResponseBody> getStudentFeeVoucher(@Query("std_id") String std_id, @Query("fee_month") String fee_month);
}
