DROP DATABASE IF EXISTS deungbulTest;
CREATE DATABASE deungbulTest;
USE deungbulTest;

# 의뢰인 앱, 전문가 앱  공통
# 요청사항 테이블
CREATE TABLE `order` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    deceasedName CHAR(30) NOT NULL, #고인 이름
    bereavedName CHAR(30) NOT NULL, #유족(상주) 이름
    `startDate` DATE NOT NULL, #장례시작일
    `endDate` DATE NOT NULL, #장례종료일(발인날짜)
    funeralHome CHAR(200) NOT NULL, #장례식장    
    religion CHAR(200) NOT NULL, #종교 ## 무교,불교,천주교,기독교,무슬림,기타
    `region` CHAR(100) NOT NULL, #지역(시도)
    `body` TEXT NOT NULL, #상세요구사항
    `expertId` INT(10) UNSIGNED DEFAULT 0 NOT NULL, # 전문가 매칭 전엔 0
    `clientId` INT(10) UNSIGNED NOT NULL,
    stepLevel SMALLINT(2) UNSIGNED DEFAULT 1 NOT NULL COMMENT '(1=의뢰요청(의뢰검토),2=의뢰승인(장례준비중),3=장례진행중,4=장례종료(종료확인요청),5=종료확인(최종종료),6=취소(중도취소))'
);

# 의뢰 테스트 데이터 생성
INSERT INTO `order`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '기독교',
    region = '대전광역시',
    `startDate` = '2021-04-01',
    `endDate` = '2021-04-03',
    deceasedName = '홍길동',
    bereavedName = '홍길순',
    funeralHome = '대전장례식장',
    `body` = '기타 요청 사항',
    `expertId` = 1,
    `clientId` = 1;

INSERT INTO `order`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '불교',
    region = '서울특별시',
    `startDate` = '2021-04-01',
    `endDate` = '2021-04-03',
    deceasedName = '임꺽정',
    bereavedName = '임꺽순',
    funeralHome = '서울장례식장',
    `body` = '기타 요청 사항',
    `expertId` = 0,
    `clientId` = 2;
    
    INSERT INTO `order`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '기독교',
    region = '서울특별시',
    `startDate` = '2021-04-05',
    `endDate` = '2021-04-07',
    deceasedName = '김삿갓',
    bereavedName = '김아무개',
    funeralHome = '서울장례식장',
    `body` = '기타 요청 사항',
    `expertId` = 2,
    `clientId` = 1;

# 회원 테이블 생성
CREATE TABLE `client` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(30) NOT NULL,
    loginPw VARCHAR(100) NOT NULL,
    authKey CHAR(80) NOT NULL,
    `name` CHAR(30) NOT NULL,
    `email` CHAR(100) NOT NULL,
    `cellphoneNo` CHAR(20) NOT NULL,
    `region` CHAR(100) NOT NULL #지역
);

# 로그인 ID로 검색했을 때
ALTER TABLE `client` ADD UNIQUE INDEX (`loginId`);

# authKey 칼럼에 유니크 인덱스 추가
ALTER TABLE `client` ADD UNIQUE INDEX (`authKey`);

# 의뢰인회원 테스트 데이터 생성
INSERT INTO `client`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'user1',
    loginPw = 'user1',
    authKey = 'authKey1__1',
    `name` = 'user1',
    `email` = 'user1@user1.com',
    `cellphoneNo` = '010-1111-1111',
    `region` = '대전광역시';

INSERT INTO `client`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'user2',
    loginPw = 'user2',
    authKey = 'authKey1__2',
    `name` = 'user2',
    `email` = 'user2@user2.com',
    `cellphoneNo` = 01022222222,
    `region` = '인천광역시';

INSERT INTO `client`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'user3',
    loginPw = 'user3',
    authKey = 'authKey1__3',
    `name` = 'user3',
    `email` = 'user3@user3.com',
    `cellphoneNo` = '010-3333-3333',
    `region` = '광주광역시';
    
# 전문가(장례지도사) 회원 테이블 생성
CREATE TABLE `expert` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(30) NOT NULL,
    loginPw VARCHAR(100) NOT NULL,
    authKey CHAR(80) NOT NULL,
    acknowledgment_step SMALLINT(2) UNSIGNED DEFAULT 1 NOT NULL COMMENT '(1=가입대기 2=가입승인 3=가입실패)',
    `name` CHAR(30) NOT NULL,
    `email` CHAR(100) NOT NULL,
    `cellphoneNo` CHAR(20) NOT NULL,
    `region` CHAR(100) NOT NULL, #지역(시도)
    `license` CHAR(100) NOT NULL,  #자격증
    `career` CHAR(50) NOT NULL,
    `work` SMALLINT(2) UNSIGNED DEFAULT 1 NOT NULL COMMENT '(1=대기중 2=작업중)'
);

# 로그인 ID로 검색했을 때
ALTER TABLE `expert` ADD UNIQUE INDEX (`loginId`);

# authKey 칼럼에 유니크 인덱스 추가
ALTER TABLE `expert` ADD UNIQUE INDEX (`authKey`);

# 전문가회원 테스트 데이터 생성
INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'expert1',
    loginPw = 'expert1',
    authKey = 'authKey2__1',
    acknowledgment_step = 2,
    `name` = 'expert1',
    `email` = 'expert1@expert1.com',
    `cellphoneNo` = '010-1111-1111',
    `region` = '대전광역시',
    `license` = '장례지도사2급',
    `career` = '1년',
    `work` = 2;

INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'expert2',
    loginPw = 'expert2',
    authKey = 'authKey2__2',
    acknowledgment_step = 1,
    `name` = 'expert2',
    `email` = 'expert2@expert2.com',
    `cellphoneNo` = '010-2222-2222',
    `region` = '서울특별시',
    `license` = '장례지도사자격증',
    `career` = '2년',
    `work` = 2;

INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'expert3',
    loginPw = 'expert3',
    authKey = 'authKey2__3',
    acknowledgment_step = 2,
    `name` = 'expert3',
    `email` = 'expert3@expert3.com',
    `cellphoneNo` = '010-3333-3333',
    `region` = '부산광역시',
    `license` = '장례지도사자격증',
    `career` = '5년',
    `work` = 2;
    
INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'expert4',
    loginPw = 'expert4',
    authKey = 'authKey2__4',
    acknowledgment_step = 2,
    `name` = 'expert4',
    `email` = 'expert4@expert4.com',
    `cellphoneNo` = '010-4444-4444',
    `region` = '대전광역시',
    `license` = '장례지도사자격증',
    `career` = '8년',
    `work` = 2;
   
# 파일 테이블 생성 
CREATE TABLE genFile (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT, # 번호
  regDate DATETIME DEFAULT NULL, # 작성날짜
  updateDate DATETIME DEFAULT NULL, # 갱신날짜
  delDate DATETIME DEFAULT NULL, # 삭제날짜
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 삭제상태(0:미삭제,1:삭제)
  relTypeCode CHAR(50) NOT NULL, # 관련 데이터 타입(article, member)
  relId INT(10) UNSIGNED NOT NULL, # 관련 데이터 번호
  originFileName VARCHAR(100) NOT NULL, # 업로드 당시의 파일이름
  fileExt CHAR(10) NOT NULL, # 확장자
  typeCode CHAR(20) NOT NULL, # 종류코드 (common)
  type2Code CHAR(20) NOT NULL, # 종류2코드 (attatchment)
  fileSize INT(10) UNSIGNED NOT NULL, # 파일의 사이즈(byte)
  fileExtTypeCode CHAR(10) NOT NULL, # 파일규격코드(img, video)
  fileExtType2Code CHAR(10) NOT NULL, # 파일규격2코드(jpg, mp4)
  fileNo SMALLINT(2) UNSIGNED NOT NULL, # 파일번호 (1)
  fileDir CHAR(20) NOT NULL, # 파일이 저장되는 폴더명
  PRIMARY KEY (id),
  KEY relId (relId,relTypeCode,typeCode,type2Code,fileNo)
);

# 리뷰 테이블 생성
CREATE TABLE review (
  id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  `relTypeCode` CHAR(20) NOT NULL,
  relId INT(10) UNSIGNED NOT NULL,
  clientId INT(10) UNSIGNED NOT NULL,
  `body` TEXT NOT NULL
);

# 고속 검색을 위해서 인덱스 걸기
ALTER TABLE review ADD KEY (relTypeCode, relId); 
# SELECT * FROM reply WHERE relTypeCode = 'article' AND relId = 5; # O
# SELECT * FROM reply WHERE relTypeCode = 'article'; # O
# SELECT * FROM reply WHERE relId = 5 AND relTypeCode = 'article'; # X


# 평점 테이블 생성
CREATE TABLE rating (
  id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  `relTypeCode` CHAR(20) NOT NULL,
  relId INT(10) UNSIGNED NOT NULL,
  clientId INT(10) UNSIGNED NOT NULL,
  `point` FLOAT(10) ## 왜 not null
);

# 고속 검색을 위해서 인덱스 걸기
ALTER TABLE rating ADD KEY (relTypeCode, relId); 
# SELECT * FROM reply WHERE relTypeCode = 'article' AND relId = 5; # O
# SELECT * FROM reply WHERE relTypeCode = 'article'; # O
# SELECT * FROM reply WHERE relId = 5 AND relTypeCode = 'article'; # X

# 관리자
CREATE TABLE `adm` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(30) NOT NULL,
    loginPw VARCHAR(100) NOT NULL,
    authKey CHAR(80) NOT NULL,
    `name` CHAR(30) NOT NULL,
    `email` CHAR(100) NOT NULL,
    `cellphoneNo` CHAR(20) NOT NULL
);

# 관리자회원 테스트 데이터
INSERT INTO `adm`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'adm1',
    loginPw = 'adm1',
    authKey = 'authKey5__1',
    `name` = 'adm1',
    `email` = 'adm1@adm1.com',
    `cellphoneNo` = 01011111111;
