## 스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술

## MVC = Model, View, Controller
    관심사 분리
## 기본 구조 
    웹 브라우저 -> 톰캣 내장서버 -> 스프링 컨테이너 -> 변환후 웹 브라우저로 리턴
    톰캣 내장서버는 스프링부트 실행시 같이 띄움 
### HTML 방식(정적 컨텐츠)
    스프링 컨테이너 내부의 @Controller가 html파일을 찾아서 리턴

### 템플릿 방식(Html 변환)
    스프링 컨테이너 내부의 ViewResolver가 맞는 템플릿을 찾아서 리턴
    @RequestParam : 요청 파라미터 값을 url에 넣어줄 수 있음
        - required : Default = true

### API 방식
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