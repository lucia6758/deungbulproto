
SELECT * FROM `order`;
SELECT * FROM `funeral`;
SELECT * FROM `client`;
SELECT * FROM `expert`;
SELECT * FROM `assistant`;
SELECT * FROM `genFile`;
SELECT * FROM `review`;
SELECT * FROM `rating`;
SELECT * FROM `event`;

TRUNCATE `genFile`;
TRUNCATE `review`;
TRUNCATE `rating`;
TRUNCATE `event`;

# 전문가 랜덤생성 쿼리
INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = CONCAT('expert', FLOOR(RAND() * 50) + 1),
    loginPw = 'expert',
    authKey = CONCAT('authKey2__', FLOOR(RAND() * 50) + 1),
    acknowledgment_step = 2,
    `name` = CONCAT('expert', FLOOR(RAND() * 50) + 1),
    `email` = 'expert1@expert1.com',
    `cellphoneNo` = '010-4130-8397',
    `region` = '대전광역시',
    `license` = '장례지도사',
    `career` = CONCAT(FLOOR(RAND() * 30) + 1,'년'),
    `work` = 1,
    deviceIdToken = "";
    
    
# 의뢰 랜덤생성 쿼리
INSERT INTO `order`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '기독교',
    region = '대전광역시',
    `startDate` = '2021-04-05',
    `endDate` = '2021-04-07',
    deceasedName = CONCAT('김고인_', FLOOR(RAND() * 50) + 1),
    bereavedName = CONCAT('김상주_', FLOOR(RAND() * 50) + 1),
    funeralHome = '대전장례식장',
    `body` = '기타 요청 사항',
    `expertId` = 0,
    `clientId` = 1;
   
