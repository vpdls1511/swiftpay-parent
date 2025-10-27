-- 계좌번호 시퀀스 생성 (MySQL 8.0+)
CREATE SEQUENCE account_number_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9999999999
    CYCLE;

-- 가맹점 시퀀스 생성 (MySQL 8.0+)
CREATE SEQUENCE merchant_number_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9999999999
    CYCLE;

-- 결제 시퀀스 생성 (MySQL 8.0+)
CREATE SEQUENCE payment_number_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9999999999
    CYCLE;

-- 주문 시퀀스 생성 (MySQL 8.0+)
CREATE SEQUENCE order_number_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9999999999
    CYCLE;

-- 정산 시퀀스 생성 (MySQL 8.0+)
CREATE SEQUENCE settlement_number_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9999999999
    CYCLE;

-- 에스크로 시퀀스 생성 (MySQL 8.0+)
CREATE SEQUENCE escrow_number_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9999999999
    CYCLE;
