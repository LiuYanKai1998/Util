set echo on
REM Creating table employee for ext.wis.employee.model.Employee
set echo off
CREATE TABLE employee (
   age   NUMBER NOT NULL,
   dept   VARCHAR2(30) NOT NULL,
   name   VARCHAR2(60) NOT NULL,
   createStampA2   DATE,
   markForDeleteA2   NUMBER NOT NULL,
   modifyStampA2   DATE,
   classnameA2A2   VARCHAR2(600),
   idA2A2   NUMBER NOT NULL,
   updateCountA2   NUMBER,
   updateStampA2   DATE,
 CONSTRAINT PK_employee PRIMARY KEY (idA2A2))
 STORAGE ( INITIAL 20k NEXT 20k PCTINCREASE 0 )
ENABLE PRIMARY KEY USING INDEX
 TABLESPACE INDX
 STORAGE ( INITIAL 20k NEXT 20k PCTINCREASE 0 )
/
COMMENT ON TABLE employee IS 'Table employee created for ext.wis.employee.model.Employee'
/
REM @//ext/wis/employee/model/employee_UserAdditions
