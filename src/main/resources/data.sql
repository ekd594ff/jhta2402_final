-- admin@example.com  1234  ROLE_ADMIN
-- user@example.com  1234  ROLE_USER
-- seller@example.com  1234  ROLE_SELLER (seller2, seller3)
INSERT INTO MEMBER (ID, USERNAME, EMAIL, PASSWORD, ROLE, PLATFORM, ISDELETED, CREATEDAT, UPDATEDAT)
VALUES ('c0bbf8bb-a6f2-4445-aaa3-2892e551ff4c', 'admin', 'admin@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_ADMIN', 'SERVER', FALSE, NOW(), NOW()),
       ('f6b73d77-5fb8-462e-85f7-f6ed0425d2ba', 'username', 'user@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('cf6e8329-59a2-458b-b1ee-693d70d128fd', 'username2', 'user2@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('5677b0b6-7b63-42c8-936c-a6d07c4793eb', 'username3', 'user3@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('c1b6ca53-0437-4eeb-959e-ec3ec97139d3', 'username4', 'user4@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('8ccf8a1a-3e8d-48a1-a508-d2b99ec4e631', 'username5', 'user5@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('323e4567-e89b-12d3-a456-426614174002', 'username6', 'user6@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('423e4567-e89b-12d3-a456-426614174003', 'username7', 'user7@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('523e4567-e89b-12d3-a456-426614174004', 'username8', 'user8@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('623e4567-e89b-12d3-a456-426614174005', 'username9', 'user9@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('723e4567-e89b-12d3-a456-426614174006', 'username10', 'user10@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('823e4567-e89b-12d3-a456-426614174007', 'username11', 'user11@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('923e4567-e89b-12d3-a456-426614174008', 'username12', 'user12@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('a23e4567-e89b-12d3-a456-426614174009', 'username13', 'user13@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW()),
       ('a8a8cbf7-ae99-4b45-81ee-7d99b72aa317', 'seller', 'seller@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_SELLER', 'SERVER', FALSE, NOW(), NOW()),
       ('6f3f2761-0fa0-43f3-bd22-e658e2a5d7df', 'seller2', 'seller2@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_SELLER', 'SERVER', FALSE, NOW(), NOW()),
       ('e766a25f-dd4a-4966-9b2c-cd1a1ec3a67d', 'seller3', 'seller3@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_SELLER', 'SERVER', FALSE, NOW(), NOW()),
       ('35e54eec-1eae-46b8-aacc-b005be7c56f3', 'seller4', 'seller4@example.com',
        '$2a$10$QnLBx1AZRoKGi6NTaCFTxOc1AnuRiKJo.2iQZyLNHW5jBJ5yRiwQq', 'ROLE_SELLER', 'SERVER', FALSE, NOW(), NOW()),
       ('d4dfb2e2-0e66-41df-bf32-0509ff4089f4', 'typhoon0678', 'typhoon0678@gmail.com',
        '$2a$10$mqdWk3omxb5qF.C//c2MouPLJQYPmNtf5iFM/E.YGzLtrX7VP.R.e', 'ROLE_USER', 'SERVER', FALSE, NOW(), NOW());



INSERT INTO COMPANY (ID, MEMBERID, COMPANYNAME, DESCRIPTION, PHONE, ADDRESS, ISAPPLIED, ISDELETED, CREATEDAT, UPDATEDAT)
VALUES ('d7e3b2f1-4a5e-4d0d-bc18-8c79c63c7c68', 'a8a8cbf7-ae99-4b45-81ee-7d99b72aa317', 'company1', NULL,
        '010-2345-6789', 'address1', TRUE, FALSE, NOW(), NOW()),
       ('168017c2-79ca-4c87-9eb1-1f97204150a2', 'f6b73d77-5fb8-462e-85f7-f6ed0425d2ba', 'company2', NULL,
        '010-1234-5678', 'address2', FALSE, FALSE, NOW(), NOW()),
       ('48782f22-0d19-4548-a605-10327389ba6b', 'cf6e8329-59a2-458b-b1ee-693d70d128fd', 'company3', NULL,
        '010-1234-5678', 'address3', FALSE, FALSE, NOW(), NOW()),
       ('a1c4d6b4-5e2c-4b7e-9c4d-9f5d0f5e2c11', '6f3f2761-0fa0-43f3-bd22-e658e2a5d7df', 'company4', 'description4',
        '010-3456-7890', 'address4', TRUE, FALSE, NOW(), NOW()),
       ('b2f4d3e6-8b8c-4a1e-9c2c-5f6a7e1c4b12', 'e766a25f-dd4a-4966-9b2c-cd1a1ec3a67d', 'company5', 'description5',
        '010-4567-8901', 'address5', TRUE, FALSE, NOW(), NOW()),
       ('c3e5d4f7-1a9c-4d2e-8b4e-7c8d3f5b2a14', '35e54eec-1eae-46b8-aacc-b005be7c56f3', 'company6', 'description6',
        '010-5678-9012', 'address6', TRUE, FALSE, NOW(), NOW());


INSERT INTO PORTFOLIO (ID, COMPANYID, TITLE, DESCRIPTION, ISDELETED, CREATEDAT, UPDATEDAT)
VALUES ('136780e7-9ead-46fc-8526-32fa58fe5846', 'd7e3b2f1-4a5e-4d0d-bc18-8c79c63c7c68', 'title1', 'description1',
        FALSE, NOW(), NOW()),
       ('f7ee9484-9c7e-4868-8d5c-0e344727e220', 'd7e3b2f1-4a5e-4d0d-bc18-8c79c63c7c68', 'title2', 'description2',
        FALSE, NOW(), NOW()),
       ('8f2656be-2ba6-48f0-852c-0ce7901c7694', 'd7e3b2f1-4a5e-4d0d-bc18-8c79c63c7c68', 'title3', 'description3',
        FALSE, NOW(), NOW()),
       ('32a53d61-d796-4dd5-85e5-de5ae790e3d0', 'd7e3b2f1-4a5e-4d0d-bc18-8c79c63c7c68', 'title4', 'description4',
        FALSE, NOW(), NOW()),
       ('a84a9e98-3eb0-4542-9bd0-94b54de5276f', 'a1c4d6b4-5e2c-4b7e-9c4d-9f5d0f5e2c11', 'title5', 'description5',
        FALSE, NOW(), NOW());


INSERT INTO SOLUTION (ID, PORTFOLIOID, TITLE, DESCRIPTION, PRICE, CREATEDAT, UPDATEDAT)
VALUES ('8871d6a2-f6b8-4dbd-b4f5-8b45dadf7abc', '136780e7-9ead-46fc-8526-32fa58fe5846', 'title1', 'description1', 75000,
        NOW(), NOW()),
       ('fce78e5f-94a1-48da-a727-1346734bffbf', '136780e7-9ead-46fc-8526-32fa58fe5846', 'title2', 'description2',
        125000, NOW(), NOW()),
       ('9faf6fb5-7f8c-4ea9-919a-69eb7a4cbef1', '136780e7-9ead-46fc-8526-32fa58fe5846', 'title3', 'description3',
        200000, NOW(), NOW());


INSERT INTO QUOTATIONREQUEST (ID, MEMBERID, PORTFOLIOID, TITLE, DESCRIPTION, CREATEDAT, UPDATEDAT)
VALUES ('cb1d87df-3b5b-42b2-8031-3da210699463', 'f6b73d77-5fb8-462e-85f7-f6ed0425d2ba',
        '136780e7-9ead-46fc-8526-32fa58fe5846', 'title1', 'description1', NOW(), NOW()),
       ('5d80e9af-1dd6-4cea-97ae-6b1b84f8cd5e', 'cf6e8329-59a2-458b-b1ee-693d70d128fd',
        '136780e7-9ead-46fc-8526-32fa58fe5846', 'title2', 'description2', NOW(), NOW()),
       ('d8e01027-b7a3-43c1-a750-901e52688058', '5677b0b6-7b63-42c8-936c-a6d07c4793eb',
        '136780e7-9ead-46fc-8526-32fa58fe5846', 'title3', 'description3', NOW(), NOW()),
       ('8fa4b087-41a3-49aa-bb21-5a4018a73eaf', 'c1b6ca53-0437-4eeb-959e-ec3ec97139d3',
        '136780e7-9ead-46fc-8526-32fa58fe5846', 'title4', 'description4', NOW(), NOW()),
       ('2fbf0f42-1f34-49b7-a860-414fc1ac7468', '8ccf8a1a-3e8d-48a1-a508-d2b99ec4e631',
        '136780e7-9ead-46fc-8526-32fa58fe5846', 'title5', 'description5', NOW(), NOW()),
       ('971b9a7f-18f0-4ad6-b2f3-d03d1356a342', '8ccf8a1a-3e8d-48a1-a508-d2b99ec4e631',
        '136780e7-9ead-46fc-8526-32fa58fe5846', 'title6', NULL, NOW(), NOW());


INSERT INTO REQUESTSOLUTION (ID, QUOTATIONREQUESTID, SOLUTIONID, CREATEDAT, UPDATEDAT)
VALUES ('cd5dc428-b771-4e12-935b-f2afb0058c63', 'cb1d87df-3b5b-42b2-8031-3da210699463',
        '8871d6a2-f6b8-4dbd-b4f5-8b45dadf7abc', NOW(), NOW()),
       ('eb4d921d-1ed0-4032-a820-40bf227af74f', 'cb1d87df-3b5b-42b2-8031-3da210699463',
        'fce78e5f-94a1-48da-a727-1346734bffbf', NOW(), NOW()),
       ('ccddf298-e40f-40ac-96ae-02ea2aeebdd4', 'cb1d87df-3b5b-42b2-8031-3da210699463',
        '9faf6fb5-7f8c-4ea9-919a-69eb7a4cbef1', NOW(), NOW()),
       ('eb5bfe3c-ff0f-45a8-8f2b-3ab95ae90d80', '5d80e9af-1dd6-4cea-97ae-6b1b84f8cd5e',
        'fce78e5f-94a1-48da-a727-1346734bffbf', NOW(), NOW()),
       ('c5f4484a-346a-4fa7-93cb-153379a316a8', '5d80e9af-1dd6-4cea-97ae-6b1b84f8cd5e',
        '9faf6fb5-7f8c-4ea9-919a-69eb7a4cbef1', NOW(), NOW()),
       ('a941256a-e152-4072-9112-0d25dd3e3daf', 'd8e01027-b7a3-43c1-a750-901e52688058',
        '8871d6a2-f6b8-4dbd-b4f5-8b45dadf7abc', NOW(), NOW()),
       ('1c7629a9-b532-42d6-8fc2-dc0404aa8132', '8fa4b087-41a3-49aa-bb21-5a4018a73eaf',
        '8871d6a2-f6b8-4dbd-b4f5-8b45dadf7abc', NOW(), NOW()),
       ('50e0216a-254b-4136-be03-c5ad7edf6d01', '2fbf0f42-1f34-49b7-a860-414fc1ac7468',
        '8871d6a2-f6b8-4dbd-b4f5-8b45dadf7abc', NOW(), NOW()),
       ('0e2f8f61-c76d-41bc-97ae-bf45d8437f92', '971b9a7f-18f0-4ad6-b2f3-d03d1356a342',
        '8871d6a2-f6b8-4dbd-b4f5-8b45dadf7abc', NOW(), NOW());


INSERT INTO QUOTATION (ID, QUOTATIONREQUESTID, TOTALTRANSACTIONAMOUNT, CREATEDAT, UPDATEDAT)
VALUES ('f26e4985-d7e6-46c9-8447-b730457a426f', 'cb1d87df-3b5b-42b2-8031-3da210699463', 200000, NOW(), NOW()),
       ('e5e8d4ff-1944-4dd8-abfa-5af26aec3894', '5d80e9af-1dd6-4cea-97ae-6b1b84f8cd5e', 500000, NOW(), NOW()),
       ('42dbbfae-ad63-42bc-822e-5a5b3ba17324', 'd8e01027-b7a3-43c1-a750-901e52688058', 1000000, NOW(), NOW()),
       ('acd9b947-7db0-4c34-9f01-479a8fc76363', '8fa4b087-41a3-49aa-bb21-5a4018a73eaf', 400000, NOW(), NOW());


INSERT INTO REVIEW (ID, QUOTATIONID, MEMBERID, TITLE, DESCRIPTION, RATE, CREATEDAT, UPDATEDAT)
VALUES ('46985b47-85cc-450e-a4e4-ca6b16e950fb', 'f26e4985-d7e6-46c9-8447-b730457a426f',
        'f6b73d77-5fb8-462e-85f7-f6ed0425d2ba', 'title1', 'description1', 4.5, NOW(), NOW()),
       ('64999617-e718-4d2b-a712-cd0059149fc8', 'e5e8d4ff-1944-4dd8-abfa-5af26aec3894',
        'cf6e8329-59a2-458b-b1ee-693d70d128fd', 'title2', 'description2', 3.0, NOW(), NOW());


INSERT INTO IMAGE (ID, REFID, URL, FILENAME, ORIGINALFILENAME, CREATEDAT, UPDATEDAT)
VALUES ('48782f22-0d19-4548-a605-10327389ba6b', 'c0bbf8bb-a6f2-4445-aaa3-2892e551ff4c', 'url1', 'filename1',
        'adminProfile', NOW(), NOW()),
       ('20c59349-688b-490e-8639-75a1cc162975', 'f6b73d77-5fb8-462e-85f7-f6ed0425d2ba', 'url2', 'filename2',
        'userProfile', NOW(), NOW()),

       ('f073ce1d-3efa-4271-95ed-8aa6a2963fd8', 'd7e3b2f1-4a5e-4d0d-bc18-8c79c63c7c68',
        'https://intarea.s3.ap-northeast-2.amazonaws.com/upload/240828/0_0503f9b5-c6e3-4148-8531-3a1927306360.jpg',
        '0_0503f9b5-c6e3-4148-8531-3a1927306360.jpg',
        'companyProfile', NOW(), NOW()),

       ('d3f8e6c7-4b1e-4a8a-bb3c-7a4f1b8e5e4b', '136780e7-9ead-46fc-8526-32fa58fe5846',
        'https://intarea.s3.ap-northeast-2.amazonaws.com/upload/240906/0_846ccdc3-4e9b-44c3-abcb-591577340ad4.png',
        '0_846ccdc3-4e9b-44c3-abcb-591577340ad4.png',
        'portfolio1-1', NOW(), NOW()),
       ('b0a1c2d3-e4f5-6789-0abc-def123456789', '136780e7-9ead-46fc-8526-32fa58fe5846',
        'https://intarea.s3.ap-northeast-2.amazonaws.com/upload/240906/0_15209797-0751-4223-9e64-961c9eff17b1.png',
        '240906/0_15209797-0751-4223-9e64-961c9eff17b1.png',
        'portfolio1-2', NOW(), NOW()),
       ('51fdd0d5-c12d-40ff-8200-e7de572923e7', '136780e7-9ead-46fc-8526-32fa58fe5846', 'url6', 'filename6',
        'portfolio1-3', NOW(), NOW()),
       ('d4d39d0b-2101-4d01-abc9-c9c162caea31', '136780e7-9ead-46fc-8526-32fa58fe5846', 'url7', 'filename7',
        'portfolio1-4', NOW(), NOW()),
       ('3032d2c1-6ec3-4ff3-b78d-9eacb92e8cbe', '136780e7-9ead-46fc-8526-32fa58fe5846', 'url8', 'filename8',
        'portfolio1-5', NOW(), NOW()),
       ('ce391aeb-06c4-42e1-a77c-fd7ce78ea92b', '136780e7-9ead-46fc-8526-32fa58fe5846', 'url9', 'filename9',
        'portfolio1-6', NOW(), NOW()),
       ('4c9cf433-0d8c-4407-b524-465077386ec5', '136780e7-9ead-46fc-8526-32fa58fe5846', 'url10', 'filename10',
        'portfolio1-7', NOW(), NOW()),
       ('d1c1935f-8d50-472c-9ff1-b0739bee8aba', '136780e7-9ead-46fc-8526-32fa58fe5846', 'url11', 'filename11',
        'portfolio1-8', NOW(), NOW()),
       ('18077064-382a-4ed5-b7f7-afc92a408011', '136780e7-9ead-46fc-8526-32fa58fe5846', 'url12', 'filename12',
        'portfolio1-9', NOW(), NOW()),
       ('b2d01cf9-89e7-4d90-8bf6-68aa4d0d22d3', '136780e7-9ead-46fc-8526-32fa58fe5846', 'url13', 'filename13',
        'portfolio1-10', NOW(), NOW()),

       ('7559fcee-0c4e-4614-b7a4-331306fe2ba8', 'f26e4985-d7e6-46c9-8447-b730457a426f', 'url14', 'filename14',
        'quotation1', NOW(), NOW()),
       ('05c81c4f-4699-4a5c-bd22-e0d93dd1c0bc', 'e5e8d4ff-1944-4dd8-abfa-5af26aec3894', 'url15', 'filename15',
        'quotation2', NOW(), NOW()),
       ('a7884160-cfe8-4fc7-a82e-0804d02a8993', '42dbbfae-ad63-42bc-822e-5a5b3ba17324', 'url16', 'filename16',
        'quotation3', NOW(), NOW()),
       ('9392e3dd-fc4f-4399-a3f4-f70dc2240a70', 'acd9b947-7db0-4c34-9f01-479a8fc76363', 'url17', 'filename17',
        'quotation4', NOW(), NOW()),

       ('a9f346b6-f3ef-43cd-9157-dc85be7191f0', '46985b47-85cc-450e-a4e4-ca6b16e950fb', 'url18', 'filename18',
        'review1', NOW(), NOW()),
       ('f2376368-7fd2-4006-a72c-88c3632d888f', '64999617-e718-4d2b-a712-cd0059149fc8', 'url19', 'filename19',
        'review2', NOW(), NOW());


INSERT INTO REPORT (ID, REFID, MEMBERID, SORT, TITLE, DESCRIPTION, PROGRESS, CREATEDAT, UPDATEDAT)
VALUES ('6a1c0cde-e0a7-49ad-ac89-f60f66e6ecbf', 'f7ee9484-9c7e-4868-8d5c-0e344727e220',
        'f6b73d77-5fb8-462e-85f7-f6ed0425d2ba', 'PORTFOLIO', 'title1',
        'description1', 'PENDING', NOW(), NOW()),
       ('76074c8b-5e71-4707-819a-77330d9d6f6f', '46985b47-85cc-450e-a4e4-ca6b16e950fb',
        'a8a8cbf7-ae99-4b45-81ee-7d99b72aa317', 'REVIEW', 'title2',
        'description2', 'IN_PROGRESS', NOW(), NOW());