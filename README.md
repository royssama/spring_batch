# spring_batch

Spring Boot + Oracle + MyBatis 기반의 배치 샘플 프로젝트입니다.

## 기술 스택

- Java 21
- Spring Boot 3.3.5
- Spring Batch
- MyBatis (XML Mapper)
- Oracle (FREEPDB1)
- Swagger UI

## 실행 전 준비

`src/main/resources/application.yml`에서 DB 접속 정보를 확인하세요.

- `spring.datasource.url`: `jdbc:p6spy:oracle:thin:@localhost:1521/FREEPDB1`
- `spring.datasource.username`: `spring_batch`
- `spring.datasource.password`: 로컬 비밀번호

## 실행

IntelliJ에서 `SpringBatchApplication`을 실행합니다.

- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 배치 구성

### 1) Tasklet 기반 (`batchTasklet`)

- 경로: `src/main/java/com/example/springbatch/batchTasklet`
- Mapper(XML): `src/main/resources/mapper/TaskletBatchMapper.xml`
- Job 이름: `taskletDbJob`
- Step 이름: `taskletDbStep`
- 설명:
  - 시작 시 `TEST_TASKLET` 테이블이 없으면 생성
  - Tasklet 실행 시 로그 1건 insert

### 2) Chunk 기반 (`batchChunk`)

- 경로: `src/main/java/com/example/springbatch/batchChunk`
- Mapper(XML): `src/main/resources/mapper/ChunkBatchMapper.xml`
- Job 이름: `chunkDbJob`
- Step 이름: `chunkDbStep`
- Chunk Size: `5`
- 3요소:
  - Reader: `ChunkItemReader`
  - Processor: `ChunkItemProcessor`
  - Writer: `ChunkItemWriter`
- 설명:
  - 시작 시 `TEST_CHUNK_SOURCE`, `TEST_CHUNK_TARGET` 테이블 없으면 생성
  - SOURCE가 비어 있으면 seed 20건 생성
  - Reader가 미처리(`PROCESSED='N'`) 데이터 페이지 조회
  - Processor가 데이터 가공 후 Writer가 TARGET insert + SOURCE 처리완료 업데이트

### 3) 스케줄 배치 (`batch`)

- 경로: `src/main/java/com/example/springbatch/batch/TestBatchScheduler.java`
- 대상 테이블: `TEST`
- 동작 주기:
  - insert: `0 * * * * *`
  - update col2: `*/5 * * * * *`
  - update col3: `*/10 * * * * *`
  - select count: `*/15 * * * * *`
  - delete all: `30 * * * * *`

## Swagger 실행 API

컨트롤러: `src/main/java/com/example/springbatch/api/BatchController.java`

- `POST /api/batch/run` : `sampleJob` 실행
- `POST /api/batch/run/tasklet` : `taskletDbJob` 실행
- `POST /api/batch/run/chunk` : `chunkDbJob` 실행

## SQL 스크립트

- `src/main/resources/sql/create_test_table.sql`
  - `TEST` 테이블 생성 스크립트(Oracle)
