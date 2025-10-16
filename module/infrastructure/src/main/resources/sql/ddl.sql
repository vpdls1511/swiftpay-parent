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
    `id`               VARCHAR(50)                                                         NOT NULL COMMENT '결제 고유 ID',
    `api_pair_key`     VARCHAR(100)                                                        NOT NULL COMMENT 'API 키 페어',
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
    `idempotency_key`  VARCHAR(100)                                                                 DEFAULT NULL COMMENT '중복 방지 키',

    -- 시스템 정보
    `created_at`       DATETIME(6)                                                         NOT NULL COMMENT '생성 일시',
    `updated_at`       DATETIME(6)                                                         NOT NULL COMMENT '수정 일시',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_idempotency_key` (`idempotency_key`),
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_api_pair_key` (`api_pair_key`),
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
