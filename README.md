## 스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술

### MVC = Model, View, Controller
    관심사 분리

### 기본 구조
    웹 브라우저 -> 톰캣 내장서버 -> 스프링 컨테이너 -> 변환후 웹 브라우저로 리턴
    톰캣 내장서버는 스프링부트 실행시 같이 띄움 
#### HTML 방식(정적 컨텐츠)
    스프링 컨테이너 내부의 @Controller가 html파일을 찾아서 리턴

#### 템플릿 방식(Html 변환)
    스프링 컨테이너 내부의 ViewResolver가 맞는 템플릿을 찾아서 리턴
    @RequestParam : 요청 파라미터 값을 url에 넣어줄 수 있음
        - required : Default = true

#### API 방식
    getter, setter = JavaBean규약(프로퍼티 접근 방식)
    alt + insert는 generater 창을 여는 단축키

    - @ResponseBody
      객체가 반환 타입일 경우 문자열과는 다르게 JSON타입으로 응답에 반환하겠다.(기본 정책)
      이 과정은 ResponseBody가 있을경우 스프링 컨테이너 내부의 ViewResolver 대신
      HttpMessageConverter가 응답 
        - 기본 문자 처리 : StringHttpMessageConverter(StringConverter)
        - 기본 객체 처리 : MappingJackson2HttpMessageConveter(JsonConverter)

    * 참고 : 클라이언트의 HTTP Accept 헤더와 서버의 컨트롤러 반환 타입 정보 둘을 조합해서
            HttpMessageConverter가 선택된다.

## 회원 관리 예제
### 비즈니스 요구사항 정리
    - 데이터 : 회원ID, 이름
    - 기능 : 회원 등록, 조회
    - 아직 데이터 저장소가 선정되지 않음(가상의 시나리오)
  - 일반적인 웹 어플리케이션 계층 구조
  - 컨트롤러 : 웹 MVC 의 컨트롤러 역할
  - 서비스 : 핵심 비즈니스 로직 구현
  - 레포지토리 : 핵심 비즈니스 로직 구현(저장소)
  - 도메인 : 비즈니스 도메인 객체
    예)회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨
  - 아직 데이터 저장소가 선정되지 않아서, 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계
  - 데이터 저장소는 RDB, NoSQL 등등 다양한 저장소를 고민중인 상황으로 가정
  - 개발을 진행하기 위해서 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소 사용

### 회원 도메인과 레포지토리 만들기

### 회원 레포지토리 테스트케이스 작성
#### 테스트 단축키 ctrl+shift+t
  - 테스트는 테스트케이스로 함(@Test)
  - 실무에서는 build 툴과 엮어서 오류 테스트케이스를 통과하지 않으면 정지
  - 모든 테스트는 메서드 순서 보장이 안됨(따로 동작하게 설계)
  - 테스트마다 데이터를 클리어 해줘야 함(afterEach())
  - Assert 로 확인 가능
    -  Assertions
    - org.junit.jupiter.api.Assertions
      - assertEquals : Assertions.assertEquals(member, result);
    - org.assertj.core.api.Assertions
      - assertThat : Assertions.assertThat(member).isEqualTo(result);
  - static import 기능을 사용하면 클래스명 없이 사용 가능
  - 테스트 툴을 먼저 만들고 구현 클래스를 만들는 걸 TDD(Test Driven Development)라고함

### 회원 서비스 개발
  - 서비스는 비즈니스에 의존적으로 설계 ex) join
  - 레포지터리는 기계적인 네이밍으로 설계 ex) save
  - Optional<>
    - ifPresent : null 아닌 값이 오면 동작
    - orElseGet : 값이 있으면 꺼내고 없으면 메서드 실행
#### ctrl+alt+v는 반환 메서드 구현 단축키
#### ctrl+alt+m은 메서드 추출 단축키

### 회원 서비스 테스트
  - 테스트는 과감하게 한글로 메서드명 작성 가능
  - given - when - then 문법
  - 다른 DB를 만들어서 사용할 일 없게 BeforeEach 사용 
  - 의존성 주입을 lombok 패키지의 @RequiredArgsConstructor 로 대체

## 스프링 빈을 등록하는 두가지 방법
  - 컴포넌트 스캔과 자동 의존관계 설정
  - 자바 코드로 직접 스프링 빈 등록하기

### 컴포넌트 스캔과 자동 의존관계 설정
  - @Component 어노테이션이 있으면 스프링 빈으로 자동 등록이 됨<br>
    아래 어노테이션들은 @Component 포함
    - @Controller : 외부 요청을 받음
    - @Service : 비즈니스 로직 구현
    - @Repository : 데이터를 저장
  - 하위 패키지가 아닌 패키지는 컴포넌트 스캔 안함

### DI(Dependency Injection)
- 강의에서는 @Autowired 사용했지만 @RequiredArgsConstructor 로 대체
- DI 종류
  - 필드 주입
  - 생성자 주입(권장)
  - Setter 주입(누군가 호출했을 시 메서드가 public 으로 열려있어야 함)
  
### 자바 코드로 직접 스프링 빈 등록하기
```java
    @Configuration
    public class SpringConfig {
        @Bean
        public MemberService memberService() {
            return new MemberService(memberRepository());
        }
    
        @Bean
        public MemberRepository memberRepository() {
            return new MemoryMemberRepository();
        }
    }
```


## 회원 관리 예제 - 웹 MVC 개발
### 회원 웹 기능 - 홈 화면 추가
스프링이 실행되면 스프링 컨테이너가 관련 컨트롤러가 있는지 먼저 찾고 없으면<br>
static 파일을 찾도록 되어있음

### 회원 웹 기능 - 등록
```java
@Controller
    @RequiredArgsConstructor
    public class MemberController {
        private final MemberService memberService;
    
        @GetMapping("members/new")
        public String createForm() {
            return "members/createMemberForm";
        }
    
        @PostMapping("/members/new")
        public String create(MemberForm form) {
            Member member = new Member();
            member.setName(form.getName());
    
            memberService.join(member);
            return "redirect:/";
        }
    }
```

### 회원 웹 기능 - 조회
```java
    @Controller
    @RequiredArgsConstructor
    public class MemberController {
        @GetMapping("/members")
        public String list(Model model) {
            List<Member> members = memberService.findMembers();
            model.addAttribute("members", members);
            return "members/memberList";
        }
    }
```

## 스프링 DB 접근 기술
### H2 데이터베이스 설치
    개발이나 테스트 용도로 가볍고 편리한 DB, 웹 화면 제공
- 다운로드 및 설치 https://www.h2database.com
- h2 데이터베이스 버전은 스프링부트 버전에 맞춘다.
- 권한 주기: chmod 755 h2.sh (윈도우 사용자는 x)
- 실행: ./h2.sh (윈도우 사용자는 h2.bat)
- 데이터베이스 파일 생성 방법
  - jdbc:h2:~/test (최초 한번)
  - ~/test.mv.db 파일 생성 확인
  - 이후부터는 jdbc:h2:tcp://localhost/~/test 이렇게 접속
  - 
### 순수 JDBC
고대의 방식
build.gradle 파일에 jdbc, h2 데이터베이스 관련 라이브러리 추가 
```
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
runtimeOnly 'com.h2database:h2'
```
application.properties
```
spring.datasource.url=jdbc:h2:tcp://localhost/~/test
spring.datasource.driver-class-name=org.h2.Driver
```
임의로 yml 로 변경 application.properties -> application.yml

MemoryMemberRepository 에서  JdbcMemberRepository 로 변경
- 개방-폐쇄 원칙(OCP, Open-Closed Principle)
    - 확장에는 열려있고, 수정, 변경에는 닫혀있다.
- 스프링의 DI를 이용하면 기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경 할 수 있다.
### 스프링 통합 테스트
- 단위 테스트가 아닌 통합 테스트 시에는 @SpringBootTest
- @Transactional 어노테이션을 달아주면 테스트 코드와 같은 상황에서 롤백을 시켜줌
* byte-buddy 경고는 jdk21로 올리면서 생긴 경고
### 스프링 JdbcTemplate
    - 순수 Jdbc와 동일한 환경설정을 하면 된다.
    - 스프링 JdbcTemplate과 MyBatis 같은 라이브러리는 JDBC API에서 본 반복 코드를 
      대부분 제거해준다. 하지만 SQL은 직접 작성해야 한다.
### JPA
- JPA는 기존의 반복 코드는 물론이고, 기본적인 SQL도 JPA가 직접 만들어서 실행해준다.
- JPA를 사용하면, SQL 데이터 중심의 설계에서 객체 중심의 설계로 패러다임을 전환을 할 수 있다.
- JPA를 사용하면 개발 생산성을 크게 높일 수 있다.
- findByName, findAll 같은 pk 기반이 아닌 것들은 jpql 이라는 쿼리를 작성
* 항상 트렌젝션이(@Transactional) 있어야 함(서비스 계층)
### 스프링 데이터 JPA
