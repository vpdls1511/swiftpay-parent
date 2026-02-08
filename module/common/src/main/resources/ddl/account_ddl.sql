-- 1. 계정 (핵심 신원)
CREATE TABLE account
(
    id         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uuid       VARCHAR(36) NOT NULL UNIQUE COMMENT '외부 노출용 UUID',
    role       VARCHAR(20) NOT NULL COMMENT 'USER, MERCHANT, ADMIN',
    status     VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, SUSPENDED, DELETED',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    INDEX idx_uuid (uuid),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 2. 로그인 정보
CREATE TABLE account_credentials
(
    account_id          BIGINT PRIMARY KEY,
    username            VARCHAR(50)  NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL COMMENT 'bcrypt',
    password_changed_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    created_at          DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
    INDEX idx_username (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 3. 프로필
CREATE TABLE account_profile
(
    account_id BIGINT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    phone      VARCHAR(20),
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
    INDEX idx_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 4. 비밀번호 이력
CREATE TABLE password_history
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_id    BIGINT       NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
    INDEX idx_account_created (account_id, created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
