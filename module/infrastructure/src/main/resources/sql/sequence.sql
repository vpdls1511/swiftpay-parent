-- 계좌번호 시퀀스 생성 (MySQL 8.0+)
CREATE SEQUENCE account_number_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9999999999
    CYCLE;
