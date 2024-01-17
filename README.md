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
  - 의존성 주입을 lombok 패키지의 @RequiredArgsConstructor로 대체

## 스프링 빈을 등록하는 두가지 방법
  - 컴포넌트 스캔과 자동 의존관계 설정
  - 자바 코드로 직접 스프링 빈 등록하기

### 컴포넌트 스캔과 자동 의존관계 설정
  - @Component 어노테이션이 있으면 스프링 빈으로 자동 등록이 됨<br>
    아래 어노테이션들은 @Component를 포함
    - @Controller : 외부 요청을 받음
    - @Service : 비즈니스 로직 구현
    - @Repository : 데이터를 저장
  - 하위 패키지가 아닌 패키지는 컴포넌트 스캔 안함

### DI(Dependency Injection)
- 강의에서는 @Autowired 사용했지만 @RequiredArgsConstructor로 대체
- DI 종류
  - 필드 주입
  - 생성자 주입(권장)
  - Setter 주입(누군가 호출했을 시 메서드가 public으로 열려있어야 함)
  
### 자바 코드로 직접 스프링 빈 등록하기
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
