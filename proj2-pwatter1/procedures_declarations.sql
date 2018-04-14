create or replace package proj2_procedures as
    procedure viewStudentsClasses(inSID in students.sid%type, errMsg out varchar2, retCursor out sys_refcursor);
    procedure addStudent(inSid in students.sid%type, first in students.firstname%type, last in students.lastname%type,status in students.status%type, gpa in 
students.gpa%type, email in students.email%type, retMsg out varchar2);
    procedure deleteStudent(inSid in students.sid%type, retMsg out varchar);
    procedure viewStudentsTable(retCursor out sys_refcursor);
    procedure viewClassesTable(retCursor out sys_refcursor);
    procedure viewCoursesTable(retCursor out sys_refcursor);
    procedure viewEnrollmentsTable(retCursor out sys_refcursor);
    procedure viewPrerequisitesTable(retCursor out sys_refcursor);
    procedure viewLogsTable(retCursor out sys_refcursor);
    procedure getInfoAndEnrollees(inClassid in classes.classid%type,errMsg out varchar2,retCursor out sys_refcursor);
    procedure dropStudent(inSid in students.sid%type, inClassid in classes.classid%type, retMsg out varchar);
    procedure enrollStudent(inSid students.sid%type, inClassid classes.classid%type, retMsg out varchar2);
    procedure returnAllPrereqs(inDeptCode in prerequisites.dept_code%type, inCourse_no in prerequisites.course_no%type, retCursor out sys_refcursor);
end;
/
show errors
