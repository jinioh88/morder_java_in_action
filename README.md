모던 자바 인 액션 스터디
# Part1 기초
## CH2 동작 파라미터화 코드 전달하기
동작 파라미터화를 이용하면 자주 바뀌는 요구사항에 효과적으로 대응할 수 있다. 
 
동작 파라미터화란 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미한다. 

결과적으로 코드블럭에 따라서 메서드의 동작이 파라미터화된다.

동작 파라미터화를 추가하려면 쓸데없는 코드가 늘어나지만 자바8은 람다 표현식으로 이 문제를 해결한다. 

1. 변화하는 요구사항에 대응하기
    - 첫 번째 시도: 녹색 사과 필터링
    ```
    public List<Apple> filterGreenApples(List<Apple> inventory) {
            List<Apple> result = new ArrayList<>();
            for(Apple apple : inventory) {
                if(Color.GREEN.equals(apple.getColor())) {
                    result.add(apple);
                }
            }
            return result;
    }
    ``` 
      - 농부가 빨간색도 필터링 하고싶어 한단다... 다른 색도 늘어날 텐데... if문을 계속 추가해야 할까?
      
    - 두 번째 시도: 색을 파라미터화
      - 색을 파라미터화할 수 있도록 메서드에 파라미터를 추가하면 변화하는 요구사항에 좀 더 유연하게 대응하는 코드를 만들 수 있다. 
    ```
    public List<Apple> filterAppleByColor(List<Apple> inventory, Color color) {
            List<Apple> result = new ArrayList<>();
            for(Apple apple : inventory) {
                if(apple.getColor().equals(color)) {
                    result.add(apple);
                }
            }
            return result;
    }
    ```  
      - 조금 편해진 함수가 나왔는데, 농부가 갑자기 '색 이외에도 무게를 구분할 수 있었으면한다...'
    ```
    public List<Apple> filterAppleByWeight(List<Apple> inventory, int weight) {
            List<Apple> result = new ArrayList<>();
            for(Apple apple : inventory) {
                if(apple.getWeight() > weight) {
                    result.add(apple);
                }
            }
            return result;
    }
    ```
      - 좋은 해결책이지만 색 필터링하는 코드와 대부분 중복된다. 
      - 이는 소프트웨어 공학의 DRY 원칙을 어기는 것이다. 
      - 색이나 무게 중 어떤 것을 기준으로 필터링할지 가리키는 플래그를 추가할 수 있지만, 이러진 말자...
      
    - 세 번째 시도: 가능한 모든 속성으로 필터링
      - 다음은 하지말아야 할 짓을 할 코드로 모든 속송을 파라미터로 다 때려 박은 것이다.
    ```
    public List<Apple> filterApples(List<Apple> inventory, Color color, int weight, boolean flag) {
            List<Apple> result = new ArrayList<>();
            for(Apple apple : inventory) {
                if((flag && apple.getColor().equals(color)) || (!flag && apple.getWeight() > weight)) {
                    result.add(apple);
                }
            }
            return result;
    }  
    ```
      - true, false는 뭘의미하는지도 모르겠고 유구사항이 바뀌었을 때 유연하게 대응할 수도 없다. 

2. 동작 파라미터화
  - 참 또는 거짓을 반환하는 함수를 프레디케이트라고 한다. 
      ```java
        public interface ApplePredicate {
            boolean test(Apple apple);
        }
    
        public class AppleHeavyWeightPredicate implements ApplePredicate {
            @Override
            public boolean test(Apple apple) {
                return apple.getWeight() > 150;
            }
        }
    
        public class AppleGreenColorPredicate implements ApplePredicate {
            @Override
            public boolean test(Apple apple) {
                return Color.GREEN.equals(apple.getColor());
            }
        }
      ```
  - 위 조건에 따라 filter 메서드가 다르게 동작할 수 있는데 이를 전략 디자인 패턴이라 한다. 
  - filterApples함수에서 ApplePredicate 객체를 받아 애플의 조건을 검사하도록 메서드를 고쳐야 한다.
  - 이렇게 동작 파라미터화, 즉 메서드가 다양한 동작을 받아 내부적으로 다양한 동작을 수행할 수 있다 
  
  - 네 번째 시도: 추상적 조건으로 필터링
      ```
        public List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
                List<Apple> result = new ArrayList<>();
                for(Apple apple : inventory) {
                    if(p.test(apple)) {
                        result.add(apple);
                    }
                }
                return result;
        }
      ```
    - 코드/동작 전달하기
      - 우리가 전달한 ApplePredicate 객체에 의해 filterApples 메서드 동작이 결정된다. 
      - filterApples 메서드의 동작을 파라미터화한 것이다. 
    - 한 개의 파라미터, 다양한 동작
      - 동작 파라미터화로 한 메서드가 다른 동작을 수행하도록 재활용 할수 있다.
      
3. 복잡한 과정 간소화
- 앞선 방법은 여러 filter 조건을 구현하는 클래스를 정의한 다음 인스턴스화 해야 하는 번거로움이 있다. 
- 자바의 익명 클래스를 이용해 코드의 양을 줄일 수 있지만 익명 클래스가 모든걸 해결하는 것은 아니다. 
- 다섯 번째 시도: 익명 클래스 사용
  ```
    List<Apple> redApples = apple.filterApples(inventory, new ApplePredicate() {
                @Override
                public boolean test(Apple apple) {
                    return Color.RED.equals(apple.getColor());
                }
    });
  ```
  - 익명 클래스는 좋았지만 여전히 많은 공간을 차지한다.
- 여섯 번째 시도: 람다 표현식 사용
  ```
    List<Apple> redApples = apple.filterApples(inventory, (Apple a) -> Color.RED.equals(a.getColor()));
  ```
- 일곱 번째 시도: 리스트 형식으로 추상화
  ```
    public <T> List<T> filter(List<T> list, Predicate<T> p) {
            List<T> result = new ArrayList<>();
            for(T e : list) {
                if(p.test(e)) {
                    result.add(e);
                }
            }
            return result;
  }
  ```
  - 이렇게 하면 사과든 오렌지든 다 받아들일 수 있다.
  
4. 실전 예제
- Comparator로 정렬하기
  - 개발자에게 변화하는 요구사항에 쉽게 대응 할 수 있는 정렬 동작을 수행할 수 있는 코드가 절실하다. 
  - List에 sort 메서드가 있는데, Comparator 객체를 이용해 sort 동작을 파라미터화 할 수 있다. 
  ```
    inventory.sort((Apple a1, Apple a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())));
  ```
---
## CH3 람다 표현식
1. 람다란 무엇인가?
- 람다 표현식은 메서드로 전달할 수 있는 익명 함수를 단순화한 것이다. 
- 람다 표현식에는 이름은 없지만, 파라미터 리스트, 바디, 반환형식, 예외리스트를 가질 수 있다. 
  ```
    Comparator<Apple> byWeight = new Comparator<Apple>() {
                public int compare(Apple a1, Apple a2) {
                    return String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight()));
                }
  };
  ```
  - 위의 기존 코드를 다음과 같이 람다로 표현할 수 있다.
  ```
    Comparator<Apple> byWeight = (Apple a1, Apple a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight()));
  ```

2. 어디에 어떻게 람다를 사용할까?
- 함수형 인터페이스
  - 함수형 인터페이스는 하나의 추상 메서드를 지정하는 인터페이스다. 
  - 인터페이스가 디폴트 메서드를 가진다고 해도 추상 메서드가 하나면 함수형 인터페이스다. 
  - 전체 표현식을 함수형 인터페이스의 인스턴스로 취급할 수 있다. 익명 클래스로도 할 수 있다.
    ```
        Runnable r1 = () -> System.out.println("hello world!");
    
        // 익명 클래스 사용
        Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("hello world");
                    }
        };
    ``` 
- 함수 디스크립터
  - 람다 표현식의 시그니처를 서술하는 메서드를 함수 디스크립터 라고 부른다. 
    - 예를 들어 Runnable 인터페이스는 인수와 반환값이 없으므로 인수와 반환값이 없는 시그니처로 본다.
    - () -> void
  - @FunctionalInterface란?
    - 함수형 인터페이스음 가리키는 어노테이션이다. 

3. 람다 활용: 실행 어라운드 패턴
- 자원을 처리하는 코드와 같이 중복되는 준비 코드와 정리 코드가 있다면 이를 실행 어라운드 패턴이라 부른다. 
    ```
    public String processFile() throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return br.readLine(); // 실제 필요한 작업을 수행.  나머지는 작업마다 중복되는 코드
        }
    }
    ```
- 1단계: 동작 파라미터화를 기억하라
  - 기존 위 코드는 한 줄만 읽을 수 있다. 요구 사항이 바뀐다면 어떻게 해야 할까?
  - 기존의 설정, 정리 과정은 재사용하고 processFile 메서드만 다른 동작을 수행하도록 명령하면 좋을 것이다. 
  - processFile의 동작을 파라미터화 하면 된다. 
  - 람다를 이용해 전달하면 된다.
- 2단계: 함수형 인터페이스를 이용해 동작 전달
  - 함수형 인터페이스 자리에 람다를 사용할 수 있다.
  ```
  @FunctionalInterface
  public interface BufferedReaderProcessor {
      String process(BufferedReader b) throws IOException;
  }
  ```
- 3단계: 동작실행
  - process 메서드의 시그니처(BufferedReader -> String)와 일치하는 람다를 전달 할 수 있다.
  ```
  public static String processFile(BufferedReaderProcessor p) throws IOException {
          try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
              return p.process(br);
          }
  }
  ```
- 4단계: 람다 전달
  ```
  String result = processFile((BufferedReader br) -> br.readLine() + br.readLine());
  String result2 = processFile((BufferedReader br) -> br.readLine());
  ```

4. 함수형 인터페이스 사용
- 함수형 인터페이스의 추상 메서드 시그니처를 함수 디스크립터라고 한다. 
- 자바 8 라이브러리 설계자들은 java.util.function 패키지로 여러 가지 새로운 함수형 인터페이스를 제공한다. 
- Predicate
  - test라는 추상 메서드를 정의하며 제네릭 T의 객체를 인수로 받아 불리언을 반환한다. 
  ```
  public <T> List<T> filter(List<T> list, Predicate<T> p) {
          List<T> results = new ArrayList<>();
          for(T t : list) {
              if(p.test(t)) {
                  results.add(t);
              }
          }
          return results;
  }
  
  Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
  List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);
  ```
- Consumer
  - 제네릭 T 객체를 받아 void를 반환하는 accept 추상 메서드를 정의한다. 
  ```
  public <T> void forEach(List<T> list, Consumer<T> c) {
      for(T t : list) {
          c.accept(t);
      }
  }
  
  forEach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.println(i));
  ```
- Function
  - 제네릭 T를 인수로 받아 제네릭 형식 R 객체를 반환하는 apply를 정의한다. 
  - 입력을 출력으로 매핑하는 람다를 정의할 때 Function 인터페이스를 활용할 수 있다. 
  ```
  public <T, R> List<R> map(List<T> list, Function<T, R> f) {
      List<R> result = new ArrayList<>();
      for(T t : list) {
          result.add(f.apply(t));
      }
      return result;
  }
  
  List<Integer> l= map(Arrays.asList("lambdas", "in", "action"), (String s) -> s.length());
  ```
- 기본형 특화
  - 제네릭 파라미터에는 참조형만 사용할 수 있다. 
  - 자바에서 오토박싱이 존재하는데, 이런 변환 과정은 비용이 소모된다. 박싱한 값은 기본형을 감싸는 래퍼며 힙에 저장된다.
  - 박싱한 값은 메모리를 더 소비하여 기본형을 가져올 때도 메모리를 탐색하는 과정이 필요하다. 
  - 자바8 에서는 IntPredicate와 같이 오토박싱을 피하기 위한 특별한 함수형 인터페이스를 제공한다. 
  ```
  IntPredicate evenNumbers = (int i) -> i % 2 == 0; // Integer가 아닌 int다.
  evenNumbers.test(1000); 
  ``` 

5. 형식검사, 형식 추론, 제약

람다 표현식 자체에는 람다가 어떤 함수형 인터페이스를 구현하는지의 정보가 포함되어 있지 않다. 따라서 람다 표현식을 더 제대로 이해하려면 람다의 실제 형식을 파악해야 한다. 
- 형식 검사
  - 람다가 사용되는 컨텍스트를 이용해 람다의 형식을 추론할 수 있다. 
  - 어떤 컨텍스트에서 기대되는 람다 표현식의 형식을 대상형식 이라고 부른다. 
- 같은 람다, 다른 함수형 인터페이스
  - 대상 형식이라는 특징 때문에 같은 람다 표현식이더라도 호환되는 추상 메서드를 가진 다른 함수형 인터페이스로 사용 될 수 있다. 
  ```
  Callable<Integer> c = () -> 42;
  PrivilegedAction<Integer> p = () -> 42;
  ```
- 형식 추론
  - 자바 컴파일러는 람다 표현식이 사용된 컨텍스트를 이용해 람다 표현식과 관련된 함수형 인터페이스를 추론한다. 
  ```
  Comparator<Apple> c1 = (Apple a1, Apple a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())); // 형식 추론 안함
  Comparator<Apple> c2 = (a1, a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())); // 형식 추론
  ```
  
- 지역 변수 사용
  - 람다에서는 자유변수(파라미터로 넘겨진 변수가 아닌 외부에서 정의된 변수)를 활용할 수 있다.
  - 이 동작을 람다 캡처링 이라 부른다. 
  - 이 경우 약간의 제약이 있는데, 지역 변수는 명시적으로 final로 선언되어 있어야 하거나 실질적으로 final로 선언된 변수와 똑같이 사용되야 한다. 
  
6. 메서드 참조(다시보기)
- 메서드 참조를 이용하면 기존의 메서드 정의를 재활용해 람다처럼 전달 할 수 있다. 
- 때론 람다 표현식보다 메서드 참조를 사용하는 것이 더 가독성이 좋고 자연스러울 수 있다. 
```
inventory.sort(Comparator.comparing(Apple::getWeight));
```
- 요약
  - 메서드 참조는 특정 메서드만을 호출하는 람다의 축약형이라고 생각할 수 있다. 
  - 메서드 참조를 이용하면 기존 메서드 구현으로 람다 표현식을 만들 수 있다. 
  - 메서드 참조를 만드는 방법
    - 메서드 참조는 세 가지 유형으로 구분할 수 있다. 
      - 정적 메서드 참조
      - 다양한 형식의 인스턴스 메서드 참조
      - 기존 객체의 인스턴스 메서드 참조
- 생성자 참조
  ```
  Supplier<Apple> cc = () -> new Apple();
  Supplier<Apple> cc2 = Apple::new;
  ```
  

 


