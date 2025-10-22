CREATE TABLE member
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    status     VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_status (status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE api_key
(
    api_key    VARCHAR(255) NOT NULL PRIMARY KEY,
    lookup_key VARCHAR(255) NOT NULL,
    user_id    INT          NULL,
    call_limit VARCHAR(100) NOT NULL,
    status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    issued_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_lookup_key (lookup_key),
    INDEX idx_call_limit (call_limit),
    INDEX idx_status (status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- swiftpay.payments definition

-- 기존 테이블 삭제
DROP TABLE IF EXISTS `payments`;

-- payments 테이블 생성
CREATE TABLE `payments`
(
    `id`               BIGINT                                                              NOT NULL AUTO_INCREMENT NOT NULL COMMENT '결제 고유 ID',
    `payment_id`       VARCHAR(50)                                                         NOT NULL COMMENT '결제 ID',
    `merchant_id`      VARCHAR(100)                                                        NOT NULL COMMENT '가맹점 ID',
    `order_id`         VARCHAR(100)                                                        NOT NULL COMMENT '가맹점 주문 번호',
    `order_name`       VARCHAR(200)                                                        NOT NULL COMMENT '주문 상품명',
    `amount`           DECIMAL(19, 2)                                                      NOT NULL COMMENT '결제 금액',
    `currency`         VARCHAR(3)                                                          NOT NULL DEFAULT 'KRW' COMMENT '통화',

    -- 결제 수단 정보
    `method`           ENUM ('CARD', 'BANK_TRANSFER')                                      NOT NULL COMMENT '결제 수단',
    `type`             VARCHAR(50)                                                                  DEFAULT NULL COMMENT '결제 수단 타입',

    `card_number`      VARCHAR(20)                                                                  DEFAULT NULL COMMENT '카드 번호 (토큰화)',
    `card_expiry`      VARCHAR(4)                                                                   DEFAULT NULL COMMENT '카드 유효기간 (YYMM)',
    `card_cvc`         VARCHAR(4)                                                                   DEFAULT NULL COMMENT '카드 CVC',
    `card_type`        ENUM ('CREDIT', 'DEBIT')                                                     DEFAULT NULL COMMENT '카드 타입 (신용/체크)',
    `card_issuer`      VARCHAR(50)                                                                  DEFAULT NULL COMMENT '카드 발급사 (현대카드, 삼성카드 등)',
    `installment_plan` INT                                                                          DEFAULT NULL COMMENT '할부 개월수 (0: 일시불)',
    `use_card_point`   BIT(1)                                                                       DEFAULT NULL COMMENT '카드 포인트 사용 여부',

    -- 계좌이체 정보
    `bank_code`        VARCHAR(50)                                                                  DEFAULT NULL COMMENT '은행 코드',
    `account_number`   VARCHAR(50)                                                                  DEFAULT NULL COMMENT '계좌 번호',

    -- 콜백 URL
    `success_url`      VARCHAR(500)                                                                 DEFAULT NULL COMMENT '성공 콜백 URL',
    `cancel_url`       VARCHAR(500)                                                                 DEFAULT NULL COMMENT '취소 콜백 URL',
    `failure_url`      VARCHAR(500)                                                                 DEFAULT NULL COMMENT '실패 콜백 URL',

    -- 상태 관리
    `status`           ENUM ('PENDING', 'IN_PROGRESS', 'SUCCEEDED', 'FAILED', 'CANCELLED') NOT NULL COMMENT '결제 상태',
    `reason`           varchar(255)                                                                 DEFAULT NULL COMMENT '실패 이유',
    `idempotency_key`  VARCHAR(100)                                                                 DEFAULT NULL COMMENT '중복 방지 키',
    `settlement_id`    VARCHAR(100)                                                                 DEFAULT NULL COMMENT '정산 관리 키',

    -- 시스템 정보
    `created_at`       DATETIME(6)                                                         NOT NULL COMMENT '생성 일시',
    `updated_at`       DATETIME(6)                                                         NOT NULL COMMENT '수정 일시',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_idempotency_key` (`idempotency_key`),
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_merchant_id` (`merchant_id`),
    INDEX `idx_settlement_id` (`settlement_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_order_id_status` (`order_id`, `status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='결제 정보 테이블';

CREATE TABLE card_bin
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    bin                 VARCHAR(8)  NOT NULL UNIQUE,
    issuer              VARCHAR(50) NOT NULL,
    slip_parameter_name VARCHAR(50),
    owner_type          VARCHAR(20) NOT NULL,
    brand               VARCHAR(30),
    card_type           VARCHAR(20) NOT NULL,
    change_history      TEXT,
    remark              TEXT,
    created_at          TIMESTAMP   NOT NULL,
    updated_at          TIMESTAMP   NOT NULL,

    INDEX idx_card_bin_bin (bin),
    INDEX idx_card_bin_issuer (issuer),
    INDEX idx_card_bin_card_type (card_type),
    INDEX idx_card_bin_brand (brand),

    CONSTRAINT chk_owner_type CHECK (owner_type IN ('PERSONAL', 'CORPORATE')),
    CONSTRAINT chk_card_type CHECK (card_type IN ('CREDIT', 'DEBIT'))
);

CREATE TABLE merchant
(
    id                  VARCHAR(36) PRIMARY KEY,
    user_id             BIGINT,

    business_number     VARCHAR(20)   NOT NULL UNIQUE,
    business_name       VARCHAR(100)  NOT NULL,
    representative_name VARCHAR(50)   NOT NULL,
    business_type       VARCHAR(50)   NOT NULL,

    email               VARCHAR(100)  NOT NULL,
    phone_number        VARCHAR(20)   NOT NULL,
    address             VARCHAR(200)  NOT NULL,

    bank_account_number VARCHAR(50)   NOT NULL,
    fee_rate            DECIMAL(5, 4) NOT NULL DEFAULT 0.0300,
    settlement_cycle    VARCHAR(20)   NOT NULL,

    contract_start_date DATE,
    contract_end_date   DATE,

    status              VARCHAR(20)   NOT NULL,
    approved_at         DATETIME,

    created_at          TIMESTAMP     NOT NULL,
    updated_at          TIMESTAMP     NOT NULL,

    INDEX idx_business_number (business_number),
    INDEX idx_status (status)
);


-- escrow 테이블 생성
CREATE TABLE `escrow`
(
    `id`           BIGINT                               NOT NULL AUTO_INCREMENT COMMENT '에스크로 고유 ID (PK)',
    `escrow_id`    VARCHAR(100)                         NOT NULL COMMENT '에스크로 ID (외부 노출)',
    `payment_id`   VARCHAR(100)                         NOT NULL COMMENT '결제 ID (Payment.paymentId 참조)',
    `merchant_id`  VARCHAR(100)                         NOT NULL COMMENT '가맹점 ID',
    `amount`       DECIMAL(19, 2)                       NOT NULL COMMENT '보관 금액',
    `currency`     VARCHAR(3)                           NOT NULL DEFAULT 'KRW' COMMENT '통화',
    `status`       ENUM ('HOLD', 'SETTLED', 'REFUNDED') NOT NULL COMMENT '에스크로 상태 (HOLD: 보관중, SETTLED: 정산완료, REFUNDED: 환불완료)',

    `created_at`   DATETIME(6)                          NOT NULL COMMENT '생성 일시',
    `completed_at` DATETIME(6)                                   DEFAULT NULL COMMENT '완료 일시 (정산 or 환불)',
    `updated_at`   DATETIME(6)                          NOT NULL COMMENT '수정 일시',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_escrow_id` (`escrow_id`),
    INDEX `idx_payment_id` (`payment_id`),
    INDEX `idx_merchant_id` (`merchant_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='에스크로 (결제금 보관) 테이블';

-- settlement 테이블 생성
CREATE TABLE `settlement`
(
    `id`                      BIGINT                                                NOT NULL AUTO_INCREMENT COMMENT '정산 고유 ID (PK)',
    `settlement_id`           VARCHAR(100)                                          NOT NULL COMMENT '정산 ID (외부 노출)',
    `merchant_account_number` VARCHAR(50)                                           NOT NULL COMMENT '가맹점 계좌번호',
    `merchant_name`           VARCHAR(100)                                          NOT NULL COMMENT '가맹점명',
    `total_amount`            DECIMAL(19, 2)                                        NOT NULL COMMENT '총 결제 금액',
    `fee_amount`              DECIMAL(19, 2)                                        NOT NULL COMMENT '총 수수료',
    `settlement_amount`       DECIMAL(19, 2)                                        NOT NULL COMMENT '실제 정산 금액 (총액 - 수수료)',
    `currency`                VARCHAR(3)                                            NOT NULL DEFAULT 'KRW' COMMENT '통화',
    `payment_ids`             JSON                                                  NOT NULL COMMENT '포함된 결제 ID 목록 (JSON 배열)',
    `settlement_date`         DATE                                                  NOT NULL COMMENT '정산 예정일',
    `status`                  ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED') NOT NULL COMMENT '정산 상태 (PENDING: 대기, PROCESSING: 처리중, COMPLETED: 완료, FAILED: 실패)',
    `fail_reason`             VARCHAR(500)                                                   DEFAULT NULL COMMENT '실패 사유',

    `created_at`              DATETIME(6)                                           NOT NULL COMMENT '생성 일시',
    `executed_at`             DATETIME(6)                                                    DEFAULT NULL COMMENT '실행 일시',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_settlement_id` (`settlement_id`),
    INDEX `idx_merchant_account` (`merchant_account_number`),
    INDEX `idx_settlement_date` (`settlement_date`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='정산 테이블';

-- orders 테이블 생성
CREATE TABLE `orders`
(
    `id`              BIGINT                                                                        NOT NULL AUTO_INCREMENT COMMENT '주문 고유 ID (PK)',
    `order_id`        VARCHAR(100)                                                                  NOT NULL COMMENT '주문 ID (외부 노출)',
    `merchant_id`     VARCHAR(100)                                                                  NOT NULL COMMENT '가맹점 ID',
    `order_name`      VARCHAR(200)                                                                  NOT NULL COMMENT '주문 상품명',

    `total_amount`    DECIMAL(19, 2)                                                                NOT NULL COMMENT '총 주문 금액',
    `balance_amount`  DECIMAL(19, 2)                                                                NOT NULL COMMENT '환불 가능 금액',
    `supply_amount`   DECIMAL(19, 2)                                                                NOT NULL COMMENT '공급가액',
    `tax`             DECIMAL(19, 2)                                                                NOT NULL COMMENT '부가세',
    `currency`        VARCHAR(3)                                                                    NOT NULL DEFAULT 'KRW' COMMENT '통화',

    `status`          ENUM ('READY', 'PROCESSING', 'DONE', 'PARTIAL_REFUNDED', 'REFUNDED', 'CANCELLED') NOT NULL COMMENT '주문 상태 (READY: 생성, PROCESSING: 진행중, DONE: 완료, PARTIAL_REFUNDED: 부분환불, REFUNDED: 전액환불, CANCELLED: 취소)',

    -- 고객 정보
    `customer_name`   VARCHAR(100)                                                                           DEFAULT NULL COMMENT '고객명',
    `customer_email`  VARCHAR(100)                                                                           DEFAULT NULL COMMENT '고객 이메일',
    `customer_phone`  VARCHAR(20)                                                                            DEFAULT NULL COMMENT '고객 전화번호',

    `created_at`      DATETIME(6)                                                                   NOT NULL COMMENT '생성 일시',
    `updated_at`      DATETIME(6)                                                                   NOT NULL COMMENT '수정 일시',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    INDEX `idx_merchant_id` (`merchant_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='주문 테이블';
