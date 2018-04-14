create or replace package body proj2_procedures as

    procedure viewStudentsClasses(inSID in students.sid%type, errMsg out varchar2, retCursor out sys_refcursor) is
    cnt int;    
    begin
    select count(*) into cnt from enrollments where sid=inSID; 
    if cnt = 0
        then errMsg := 'The student has not taken any courses.';
    else 
        open retCursor for
        SELECT cl.classid, cl.dept_code||cl.course_no AS DPT_COURSE_NO, co.title, cl.year, cl.semester
        FROM enrollments e right join classes cl 
        ON e.classid=cl.classid 
        INNER JOIN courses co 
        ON co.course_no=cl.course_no
        WHERE e.sid=inSID;
    end if;
    end;

    procedure addStudent(inSid in students.sid%type, first in students.firstname%type, last in students.lastname%type,status in students.status%type, gpa in 
students.gpa%type, email in students.email%type, retMsg out varchar2) is
    cnt int;
    begin
    select count(*) into cnt from students where sid=inSid;
    if cnt = 1
    then
        retMsg := 'ERROR: SID already in the table';
    else
        insert into students values (inSid, first, last, status, gpa, email);
        retMsg := 'Student successfully added to table.'; 
    end if;
    end;


    procedure deleteStudent(inSid in students.sid%type, retMsg out varchar) is
    cnt int;
    begin
    select count(*) into cnt from students where sid=inSid;
    if cnt=0
        then retMsg := 'ERROR: The SID is invalid.';
    else
        delete from students where sid=inSid;
        -- implement trigger to delete student from enrollments
        commit;
        retMsg:= 'Student deleted sucessfully from the table';
    end if;
    end;

    procedure viewStudentsTable(retCursor out sys_refcursor) as
    begin
    open retCursor for
    select * from students;
    end;

    procedure viewClassesTable(retCursor out sys_refcursor) as
    begin
    open retCursor for
    select * from classes;
    end;

    procedure viewCoursesTable(retCursor out sys_refcursor) as
    begin
    open retCursor for
    select * from courses;
    end;

    procedure viewEnrollmentsTable(retCursor out sys_refcursor) as
    begin
    open retCursor for
    select * from enrollments;
    end;

    procedure viewPrerequisitesTable(retCursor out sys_refcursor) as
    begin
    open retCursor for
    select * from prerequisites;
    end;

    procedure viewLogsTable(retCursor out sys_refcursor) as
    begin
    open retCursor for
    select * from logs;
    end;

    procedure enrollStudent(inSid students.sid%type, inClassid classes.classid%type, retMsg out varchar2) is 
    
    -- declarations 
    sidValidCnt int;
    classValidCnt int;
    tempLimit int;
    tempClass_size int;
    enrolledValidCnt int;

    begin

    select count(*) into sidValidCnt from students where sid=inSid;
    select count(*) into classValidCnt from classes where classid=inClassid;
    
    if sidValidCnt = 0
    then
        retMsg := 'ERROR: The SID is invalid.';
    else 
        if classValidCnt = 0
        then 
            retMsg := 'ERROR: The classid is invalid.'; 
        else
            select limit, class_size into tempLimit, tempClass_size from classes where classid=inClassid;

            if (tempClass_size + 1) > tempLimit
            then
                retMsg := 'ERROR: The class is full.';
            else 
                select count(*) into enrolledValidCnt from enrollments where sid=inSid and classid=inClassid;

                if (enrolledValidCnt > 0)
                then
                    retMsg := 'ERROR: The student is already in the class.';
                else 
                
                -- couldn't finish this procedure in time
                -- kept getting ambiguos errors from JDBC and unable to figure out what line caused the error
                -- pseudo continuing
                -- check for prereqs, if none, enroll if the student wouldn't become overloaded.
                -- if prereqs, check if he/her had taken all the prereqs, also check the min grade requirement
                -- if successful, then insert if wouldnt become overloaded

               end if;
            end if;
      end if;
    end if;
    end;

    procedure getInfoAndEnrollees(inClassid in classes.classid%type,errMsg out varchar2,retCursor out sys_refcursor) is 
    classCnt int;
    enrollmentCnt int;
    begin
    select count(*) into classCnt from classes where classid=inClassid;
    select count(*) into enrollmentCnt from enrollments where classid=inClassid;
    if classCnt > 0
    then
        if enrollmentCnt > 0
        then
            open retCursor for
            SELECT cl.classid, co.title, cl.semester, cl.year, s.sid, s.lastname 
            FROM enrollments e 
            INNER JOIN students s
            ON e.sid=s.sid
            RIGHT JOIN classes cl 
            ON e.classid=cl.classid 
            INNER JOIN courses co 
            ON co.course_no=cl.course_no 
            WHERE cl.classid=inClassid;
        else
            errMsg := 'ERROR: No student is enrolled in this class.';  
        end if;
    else
        errMsg := 'ERROR: The CLASSID is invalid';
    end if;           
    end;


    procedure returnAllPrereqs(inDeptCode in prerequisites.dept_code%type, inCourse_no in prerequisites.course_no%type, retCursor out sys_refcursor) is
    cursor pCursor is select pre_dept_code, pre_course_no from prerequisites where dept_code = inDeptCode and course_no = inCourse_no;
    row pCursor%rowtype;    
    
    begin
        insert into prereqResults 
        (select pre_dept_code, pre_course_no from prerequisites where dept_code=inDeptCode and course_no=inCourse_no);

        open pCursor;
        loop 
            fetch pCursor into row;
            exit when pCursor%notfound;
            returnAllPrereqs(row.pre_dept_code, row.pre_course_no, retCursor);
        end loop; 
    open retCursor for select * from prereqResults;
    close pCursor;
    end;

    procedure dropStudent(inSid in students.sid%type, inClassid in classes.classid%type, retMsg out varchar) is

    -- declarations
    sidCnt int;
    classCnt int;
    prereqCnt int;
    classSize int;
    enrollment int;
    retMsg1 varchar(75);
    retMsg2 varchar(75);
    tempDeptCode classes.dept_code%type; 
    tempCourse_no classes.course_no%type;

    begin

    select count(*) into sidCnt from students where sid=inSid;
    select count(*) into classCnt from classes where classid=inClassid;

    if sidCnt = 0
    then
        retMsg := 'ERROR: The SID is invalid.';
        else
            if classCnt = 0
            then
                retMsg := 'ERROR: The CLASSID is invalid.';
                else
                    select count(*) into enrollment from enrollments where sid=inSid and classid=inClassid;
                    if enrollment = 0
                    then
                        retMsg := 'ERROR: The student is not enrolled in the class.';
                    else
                        select course_no, dept_code into tempCourse_no, tempDeptCode from classes where classid=inClassid;

                        select count(*) into prereqCnt from classes cl, prerequisites p where classid 
                        in (select classid from enrollments where classid<>inClassid and sid=inSid) 
                        and cl.dept_code=p.dept_code 
                        and cl.course_no = p.course_no
                        and p.pre_dept_code=tempDeptCode 
                        and p.pre_course_no=tempCourse_no;
    
                    if prereqCnt > 0
                    then
                        retMsg := 'ERROR: The drop is not permitted because another class uses it as a prerequisite';
                
                    else
                        delete from enrollments where sid=inSid and classid=inClassid;

                        retMsg := 'Student dropped successfully';
                
                        if (classSize - 1) = 0 -- class now empty
                        then 
                            retMsg1:='CLASSID: ' || inClassid || ' now has no students.';
                        end if;
                
                        if (enrollment - 1) = 0 -- student no longer enrolled
                        then
                            retMsg2:='SID: now is enrolled in no classes.';
                        end if;

                        retMsg := retMsg || ' ' || retMsg1 || ' ' || retMsg2;         

                    end if;
                end if;
            end if;
        end if;
     end;





end;

/ 
show errors
