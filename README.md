# Project Spec
Framework : Spring Boot (Java8) + JPA  
DBMS : MySQL

# API 명세서

### 1. 리뷰 작성, 수정, 삭제 이벤트가 일어날 때 호출하는 API

method : POST  
url : /events

|Request Parameter|필수?|설명|
|---|---|---|
| type | 필수 | "REVIEW" 고정|
| action | 필수 | 리뷰 작성은 "ADD", 수정은 "MOD", 삭제는 "DELETE"|
| reviewId | 필수 | 리뷰 아이디|
| content | 필수 | |
| attachedPhotoIds | 필수 | |
| userId | 필수 | 리뷰의 작성자 id |
| placeId | 필수 | |

| Response Parameter | 타입 | 필수? | 설명 |
|---|---|---|---|
| result | String | 필수 | 성공이면 SUCCESS, 실패면 FAIL 응답|

<br>

### 2. 포인트 조회 API

method : GET  
url : /point/check

|Request Parameter|필수?|설명|
|---|---|---|
| user | 필수 | user id |

|Response Parameter| 타입 | 필수?|설명|
|---|---|---|--|
| userId | String | 필수 | user Id |
| point | String | 필수 | user Id의 포인트 값|

<br>
<br>

# Getting Start
- MySQL 설치 및 아래 DDL문으로 테이블 생성
- Spring Boot Run


<br>
<br>

# Database

Table
1. REVIEW
   1. 테이블 생성 SQL  
   create table review (  
	review_id varchar(36) not null primary key comment 'UUID 포맷의 review_id',  
    content varchar(5000) comment 'review 내용',  
    attached_photo_ids varchar(36) comment 'review 사진',  
    user_id varchar(36) comment 'review 작성한 user_id',  
    place_id varchar(36) comment 'review 가 작성된 장소',  
    created_at datetime comment 'review 생성 시간',  
    updated_at datetime comment 'review 수정 시간',  
    deleted_at datetime comment 'review 삭제 시간',  
    is_delete int comment '0: 데이터 존재 / 1: 데이터 삭제'  
);
2. POINT
   1. 테이블 생성 SQL  
   create table point (  
	id int not null auto_increment primary key comment 'point id',  
    user_id varchar(36) comment 'point 를 받거나 회수당한 user_id',  
    type int comment '1: content point/ 2: photo point / 3: firstplace point',  
    point int comment '지급 또는 회수되는 point',  
    mark int comment '1: plus / 2 : minus',  
    retrieved_id int comment '회수 point id',  
    created_at datetime comment 'point 지급 또는 회수 시간',  
    review_id varchar(36) comment '포인트 지급이나 회수에 관련된 review_id'  
);  

3. USER
   1. 테이블 생성 SQL  
   create table user (  
	user_id varchar(36) not null primary key,  
    point int comment '유저가 가진 총 point'  
);



# TODO
- 테이블 인덱스 설정
- 리뷰 사진 배열로 수정
- API 요청 실패 시, Exception 으로 에러 처리
