create or replace trigger deleteStudentTrigger
after delete on students
for each row
declare
id varchar2(15);
tempSid char(4);
tempUser varchar2(15);
tempTable varchar2(15) default 'students'; 
operation varchar2(15) default 'delete';
begin
tempSid := :old.sid; -- sid val before trigger fire
select user into tempUser from dual; -- want results of system function in select statement, not data
id:=log_sequence.nextval;
  insert into logs values(id, user, sysdate, tempTable, operation, tempSid);
  delete from enrollments where sid=tempSid; -- delete records with the students sid, since he/shes deleted
end;
/ 

create or replace trigger addStudentTrigger
after insert on students
for each row
declare
id varchar2(15);
tempSid char(4);
tempUser varchar2(15);
tempTable varchar2(15) default 'students'; 
operation varchar2(15) default 'add';
begin
tempSid := :new.sid; -- sid val after trigger fire
select user into tempUser from dual; -- want results of system function in select statement, not data
id:=log_sequence.nextval;
  insert into logs values(id, tempUser, sysdate, tempTable, operation, tempSid);
end;
/ 

create or replace trigger dropCourseTrigger
after delete on enrollments
for each row
declare
id varchar2(15);
tempSid char(4);
tempClassid char(5);
tempUser varchar2(15);
tempTable varchar2(15) default 'enrollments'; 
operation varchar2(15) default 'delete';
key varchar2(25);

begin
tempSid := :old.sid;
tempClassid:= :old.classid; -- sid val after trigger fire (old)
key := (tempSid || '-' || tempClassid);
select user into tempUser from dual; -- want results of system function in select statement, not data
id:=log_sequence.nextval;
 insert into logs values(id, tempUser, sysdate, tempTable, operation, key); 
 update classes set class_size=class_size-1 where classid=tempClassid; -- lost a student, decrement size
 end;
/

create or replace trigger insertCourseTrigger
after insert on enrollments
for each row
declare
id varchar2(15);
tempSid char(4);
tempClassid char(5);
tempUser varchar2(15);
tempTable varchar2(15) default 'enrollments'; 
operation varchar2(15) default 'insert';
key varchar2(25);

begin
tempSid := :new.sid;
tempClassid := :new.classid; -- sid val before trigger fire (new)
key := (tempSid || ',' || tempClassid);
select user into tempUser from dual; -- want results of system function in select statement, not data
log_key_value:=log_sequence.nextval;
 insert into logs values(id, tempUser, sysdate, tempTable, operation, key);
 update classes set class_size=class_size+1 where classid=log_classid; -- gained a student, increment size
 end;
/

/
show errors