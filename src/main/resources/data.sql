-- admin@example.com  1234  ROLE_USER
-- user@example.com  1234  ROLE_ADMIN
-- seller@example.com  1234  ROLE_SELLER
INSERT INTO MEMBER (ID, USERNAME, EMAIL, PASSWORD, ROLE, PLATFORM, CREATEDAT, UPDATEDAT)
VALUES ('c0bbf8bb-a6f2-4445-aaa3-2892e551ff4c', 'admin', 'admin@example.com',
        '$2a$10$hhBnxy3adfeuHxjTSS4que/on8NJBkbPsYcGHd5Yy887OxF1BHq16', 'ROLE_ADMIN', 'SERVER', NOW(), NOW()),
       ('f6b73d77-5fb8-462e-85f7-f6ed0425d2ba', 'username', 'user@example.com',
        '$2a$10$tHUPyMBwPx/jKtuBRjldTe3tAmo34LnayXdecS2Os.hKatKyjmmvG', 'ROLE_USER', 'SERVER', NOW(), NOW()),
       ('a8a8cbf7-ae99-4b45-81ee-7d99b72aa317', 'seller', 'seller@example.com',
        '$2a$10$TkFXHGahRGR8zAYuOCshK.kqhilZJhQaWKint5pdf3jkZ.5CUfzTa', 'ROLE_SELLER', 'SERVER', NOW(), NOW());