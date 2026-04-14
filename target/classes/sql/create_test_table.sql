-- Oracle TEST table creation script
-- Execute with a user like SPRING_BATCH on FREEPDB1

BEGIN
    EXECUTE IMMEDIATE '
        CREATE TABLE TEST (
            ID   NUMBER(19,0)   NOT NULL,
            COL1 VARCHAR2(100),
            COL2 VARCHAR2(100),
            COL3 VARCHAR2(100),
            CONSTRAINT PK_TEST PRIMARY KEY (ID)
        )
    ';
EXCEPTION
    WHEN OTHERS THEN
        -- ORA-00955: name is already used by an existing object
        IF SQLCODE != -955 THEN
            RAISE;
        END IF;
END;
/
