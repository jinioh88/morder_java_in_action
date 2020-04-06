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
  
6. 메서드 참조
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
  
7. 람다, 메서드 참조 활용하기
- 1단계: 코드 전달
  - List의 sort메서드는 void sort(Comparator<? super E> c) 형식으로 되어있어 동작 파라미터화 됬다고 볼수 있다.
  - sort의 전략을 전달하면 sort의 동작이 달라진다.
  ```
  public class AppleComparator implements Comparator<Apple> {
      @Override
      public int compare(Apple a1, Apple a2) {
          return String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight()));
      }
  }
  
  inventory.sort(new AppleComparator());
  ```
- 2단계: 익명 클래스 사용
  - 위의 코드 보다는 익명 클래스를 이용하는게 좋다.
  ```
  inventory.sort(new Comparator<Apple>() {
      @Override
      public int compare(Apple a1, Apple a2) {
          return String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight()));
      }
  });
  ```
  
- 3단계: 람다 표현식 사용
  ```
  inventory.sort((Apple a1, Apple a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())));
  
  // 형식추론
  inventory.sort((a1, a2) -> String.valueOf(a1.getWeight()).compareTo(String.valueOf(a2.getWeight())));
  
  inventory.sort(Comparator.comparing(app -> app.getWeight()));
  ``` 
  
- 4단계: 메서드 참조 사용
  ```
  inventory.sort(Comparator.comparing(Apple::getWeight));
  ```
  - 마지막 과정은 앞전것 보다 코드도 짧아졌고 의미도 명확해졌다. 

- 람다 표현식을 조합할 수 있는 유용한 메서드
  - Comparator 조합
    - 역정렬
    ```
    // 역정렬하는 Comparator 인스턴스를 만들 필요가 없다.
    inventory.sort(Comparator.comparing(Apple::getWeight).reversed());
    ```
    - Comparator 연결
    ```
    // 같은 무게의 사과를 한번 더 비교하고 싶다. 
    inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
    ```
  - Predicate 조합
    - negate, and, or 세 가지 메서드를 제공한다. 
  - Function 조합
    - Function 인스턴스를 반환하는 andThen, compose 두 가지를 제공한다. 
    - f.andThen(g)는 g(f(x))가 되고 f.compose(g)는 f(g(x))가 된다. 
  
---
# Part2 함수형 데이터 처리
## 스트림 소개
1. 스트림이란 무엇인가?
- 스트림을 이용하면 선언형으로 컬렉션 데이터를 처리할 수 있다. 
- 스트림은 다음의 다양한 이득을 준다. 
  - 선언형으로 코드를 구현할 수 있다. 
  - filter, sorted, map, collect 같은 여러 빌딩 블록 연산을 연결해 복잡한 데이터 처리 파이프라인을 만들 수 있다. 
  
2. 스트림 시작하기
- 스트림이란 '데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소'로 정의할 수 있다.
  - 연속된 요소 : 스트림은 연속된 값 집합의 인터페이스를 제공한다. 컬렉션의 주제는 데이터고, 스트림의 주제는 계산이다. 
  - 소스 : 스트림은 데이터 제공 소스로부터 데이터를 소비한다.
  - 데이터 처리 연산 : 함수형 프로그래밍 언어에서 일반적으로 지원하는 연산과 데이터베이스와 비슷한 연산을 지원한다. 
- 스트림은 다음 2가지 중요 특징이 있다.
  - 파이프라이닝
  - 내부반복 
- collect를 제외한 모든 연산은 서로 파이프라인으 ㄹ형성하도록 스트림을 반환한다. 
  - filter : 스트림에서 특정요소를 제외시킨다. 
  - map : 한 요소를 다른 요소로 변환하거나 정보를 추출한다. 
  - limit : 스트림 크기를 축소한다. 
  - collect : 스트림을 다른 형식으로 변환한다.
  
3. 스트림과 컬렉션
- 딱 한 번만 탐색할 수 있다. 
  - 탐색된 스트림의 요소는 소비된다. 
  - 반복자와 마찬가지로 한 번 탐색한 요소를 다시 탐색하려면 초기 데이터 소스에서 새로운 스트림을 만들ㄹ어야 한다.
- 외부 반복과 내부 반복
  - 외부 반복은 사용자가 직접 요소를 반복해야 한다. 
  - 반면 스트림은 반복을 알아서 처리하고 결과를 어딘가 저장해주는 내부반복을 사용한다. 
  ```
    List<String> names = new ArrayList<>();
    for(Dish dish : menu) {
        names.add(dish.getName());
    }
    
    // 내부적으로 숨겨진 외부 반복
    Iterator<Dish> iterator = menu.iterator();
    while (iterator.hasNext()) {
        Dish dish = iterator.next();
        names.add(dish.getName());
    }
    
    // 내부반복
    List<String> nameStream =  menu.stream().map(Dish::getName).collect(Collectors.toList());  
  ```
  - 내부 반복을 사용하면 투명하게 병렬로 처리하거나 더 최적화된 다양한 순서로 처리할 수 있다. 
  ```
    // 퀴즈 문제
    List<Dish> highCaloriecDishes = menu.stream().filter(m -> m.getCalories() > 300).collect(Collectors.toList());
  ```

4. 스트림 연산
- 스트림 연산은 두 그룹으로 구분할 수 있다. 파이프라인을 형성하는 중간연산과 파이프라인을 다는 최종 연산이다.
- 중간연산
  - 중간 연산은 다른 스트림을 반환한다. 
  - 중간 연산은 게으르다. 중간연산을 합친 다음 합쳐진 중간 연산을 최종 연산으로 한 번에 처리하기 때문이다. 
- 최종연산
  - 스트림 파이프라인에서 결과를 도출한다.
- 스트림 이용하기
  - 스트림 이용 과정은 다음과 같이 세 가지로 요약할 수 있다. 
    - 질의를 수행할 데이터 소스
    - 스트림 파이프라인을 구성할 중간 연산 연결
    - 스트림 파이프라인을 실행하고 결과를 만들 최종 연산
    
---
## 스트림 활용
1. 필터링
- 프레이케이트로 필터링
  - filter 메서드는 프레디케이트를 인수로 받아 프레디게이트와 일치하는 모든 요소를 포함하는 스트림을 반환한다. 
  ```
  List<Dish> vegetarianMenu = menu.stream().filter(Dish::isVegetarian).collect(Collectors.toList());
  ```
- 고유 요소 필터링
  - distinct로 고유 요소로 이뤄진 스트림을 반환할 수 있다. 
  ```
  List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
  numbers.stream().filter(i -> i%2 == 0).distinct().collect(Collectors.toList());
  ```
  
2. 스트림 슬라이싱
- 프레디케이트를 이용한 슬라이싱
  - TAKEWHILE 활용
    - 320칼로리 이하의 요리를 선택할 수 있는 방법은 뭐가 있을까?
    - filter 연산으로 전체 스트림을 반복하면서 각 요소에 프레디케이트를 적용하면 된다. 
    - 리스트가 이미 정렬 되 있다면 320 칼로리보다 크거나 같은 요리가 나왔을 때 내부 반복을 중단할 수 있다. 
    - takeWhile 연산을 이용하면 무한 스트림을 포함한 모든 스트림에 프레디켕트를 적용해 스트림을 슬라이할 수 있다.  
    ```
    List<Dish> sliceMenu1 = menu.stream().takeWhile(dish -> dish.getCalories() < 320).collect(Collectors.toList());
    ```
  - DROPWHILE 활용
    - 나머지 요소를 선택하려 할 때 사용한다. 
    - takeWhile과 정 반대의 작업을 한다. 
    - dropWhile은 처음으로 거짓이 되는 지점까지 발견된 요소를 버린다. 
    - 프레디케이트가 거짓이되면 그 지점까지 잡업을 중단하고 남은 모든 요소를 반환한다. 
- 스트림 축소
  - limit(n)으로 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환한다.
  ```
  List<Dish> vegetarianMenu = menu.stream().filter(Dish::isVegetarian).limit(3).collect(Collectors.toList());
  ``` 
- 요소 건너뛰기
  - skip(n)으로 처음 n개 요소를 제외한 스트림을 반환할 수 있다. 
  - limit(n)과 skip(n)은 상호 보완적인 연산을 수행한다. 
  
3. 매핑
- 스트림의 각 요소에 함수 적용하기
  - 스트림은 함수를 인수로 받는 map 메서드를 지원한다. 
  - 이 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다. 
  - 요리명으로 추출하는 코드
  ```
  List<String> dishNames = menu.stream().map(Dish::getName).collect(Collectors.toList());
  ```
  - 요리명의 길이를 알고 싶다면?
  ```
  List<Integer> dishNames = menu.stream().map(d -> d.getName().length()).collect(Collectors.toList());
  
  // 책은 다음과 같이 나옴(위처럼 하는거랑 다른건가?)
  List<Integer> dishNames = menu.stream().map(Dish::getName).map(String::length).collect(Collectors.toList());
  ```
- 스트림 평면화
  - flatMap 사용
    - flatMap은 map(Arrays::stream)과 달리 하나의 평면화된 스트림을 반환한다. 
    ```
    List<String> uniqueChars = words.stream().map(word -> word.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
    ```
    - 즉 flatMap은 스트림의 각 값을 다른 스트림으로 만든 다음 모든 스트림을 하나의 스트림으로 온결한다. 
  
4. 검색과 매핑
- 프레디케이트가 적어도 한 요소와 일치하는지 확인
  - anyMatch 메서드를 이용한다. 
  - 채식 요리가 있는지 확인하려면
  ```
  if(menu.stream().anyMatch(Dish::isVegetarian)) {
              System.out.println("채식 주의자");
  }
  ```
- 프레디케이트가 모든 요소와 일치하는지 검사
  - allMatch 메서드로 스트림 모든 요소가 일치하는지 검사할 수 있다. 
  ```
  boolean isHealthy = menu.stream().allMatch(dish -> dish.getCalories() < 10000);
  ```
  - nonMatch는 allMtach와 반대 연산을 수행한다. 
  - anyMatch, allMatch, noneMatch 세메서드는 스트림 쇼트서킷 기법, 즉 &&, || 와 같은 연산을 활용한다. 
- 요소 검색
  - findAny 메서드는 현재 스트림에서 임의의 요소를 반환한다. 
  - 쇼트서킷을 이용해 결과를 찾는 즉시 실행을 종료한다. 
  ```
  Optional<Dish> dish1 = menu.stream().filter(Dish::isVegetarian).findAny();
  ```
- 첫 번째 요소 찾기
  - 논리적인 아이템 순서가 정해져 있는 스트림에서 첫 번째 요소를 찾고 싶다.
  - findFirst 함수를 사용하면 된다. 
  - 병렬 실행에서는 첫 번째 요소를 찾기 어렵다. 그래서 반환 순서가 상관 없다면 병렬 스트림에선 제약이 적은 findAny를 사용한다. 
  
5. 리듀싱
- 스트림의 모든 요소를 반복적으로 처리하는 것을 리듀싱 연산이라 한다. 
- 요소의 합
  - reduce를 이용하면 애플리케이션의 반복된 패턴을 추상화 할 수 있다. 
  ```
  int sum = numbers.stream().reduce(0, (a, b) -> a + b);
  
  // 메서드 참조로 간단하게 할 수 있다.
  int sum = numbers.stream().reduce(0, Integer::sum);
  ```
  - 초깃값을 받지 않은 reduce도 있다. 이땐 Optional을 반환한다. 
- 최댓값과 최솟값
  - 두 요소에 최댓값을 반환하는 람다만 있으면 된다. 람다 대신 메서드 참조를 사용할 수 있다. 
  ```
  Optional<Integer> max = numbers.stream().reduce(Integer::max);
  Optional<Integer> min = numbers.stream().reduce(Integer::min);
  ```
  
7. 숫자형 스트림
- 기본형 특화 스트림
  - 스트림 API는 박싱 비용을 피할 수 있도록 IntStream, DoubleStream, LongStream을 제공한다. 
  - 자주 사용하는 숫자 관련 리듀싱 연산 수행 메서드를 제공한다. 
  - 박싱 과정에서 일어나는 효율성과 관련만 있고, 추가 기능은 없다. 
  - 숫사 스트림으로 매핑
    - mapToInt, mapToDouble, mapToLong을 가장 많이 사용한다. 
    ```
    int cal = menu.stream().mapToInt(Dish::getCalories).sum();
    ```
  - 객체 스트림으로 복원하기
    - boxed를 이용해 특화 스트림을 일반 스트림으로 변환할 수 있다. 
    ```
    IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
    Stream<Integer> stream = intStream.boxed();
    ```
  - 기본값: OptionalInt
    - IntStream의 기본값은 0이다. 스트림 요소가 없을 때 실제 최댓값이 0이 되 버릴 수 있다.
    - OptionalInt, OptionalDouble, OptionalLong 으로 기본 값을 설정할 수 있다. 
    ```
    OptionalInt maxCalories = menu.stream().mapToInt(Dish::getCalories).max();
    int max1 = maxCalories.orElse(1);  // 값이 없을때 기본값 명시
    ```
- 숫자 범위
  - IntStream과 LongStream에서 range와 rangeClosed라는 두 가지 정적 메서드를 제공한다. 
  - range 메서드는 시작값과 종료값이 결과에 포함되지 않는다. 

8. 스트림 만들기
- 값으로 스트림 만들기
  - 임의의 수를 인수로 받는 정적 메서드 Stream.of를 이용해 스트림을 만들 수 있다. 
  - empty로 스트림을 비울 수 있다. 
  ```
  Stream<String> strStream = Stream.of("Mordern", "Java", "In", "Action");
  ```
- null이 될 수 있는 객체로 스트림 만들기
  - Stream.ofNullable로 null이 될 수 있는 객체를 스트림으로 만들 수 있다. 
  - 위를 사용안하면 null인지 아닌지 판별하는 코드가 들어가야 한다. 
- 배열로 스트림 만들기
  - Arrays.stream으로 만들 수 있다. 
- 파일로 스트림 만들기
  - Files.lines는 주어진 파일의 행 스트림을 문자열로 반환한다. 
  - Stream 인터페이스는 AutoCloseable 인터페이스를 구현한다. 
- 함수로 무한 스트림 만들기
  - 보통 무한한 값을 출력하지 않도록 limit(n) 함수를 함께 연결해 사용한다. 
  - iterate 메서드
    - 초깃값과 람다를 인수로 받아 새루운 값을 끊임 없이 생산할 수 있다. 
    - 일반적으로 연속된 일련의 값을 만들 때는 iteratre를 사용한다. 
    ```
    Stream.iterate(0, i -> i + 2).limit(10).forEach(System.out::println);
    ```
    - 자바 9의 iterate 메서드는 프레디케이트를 지원한다. 두번째 인수로 지정하면 된다. 
  - generate 메서드
    - 생상된 각 값을 연속적으로 계산하지 않는다. 
    ```
    Stream.generate(Math::random).limit(5).forEach(System.out::println);        
    ```

---
## 스트림으로 데이터 수집
collect도 다양한 요소 누적 방식을 인수로 받아 스트림을 최종 결과로 도출하는 리듀싱 연산을 수행할 수 있다.

1. 컬렉터란 무엇인가?
- Collector 인터페이스 구현은 스트림의 요소를 어떤 식으로 도출할지 지정한다. 
- 고급 리듀싱 기능을 수행하는 컬렉터
  - 스트림에 collect를 호출하면 스트림의 요소에 리듀싱 연산이 수행된다. 
  - collect에서는 리듀싱 연산을 이용해 스트림의 각 요소를 방문하면서 컬렉터가 작업을 처리한다. 
- 미리 정의된 컬렉터
  - 크게 3가지로 구분된다. 
    - 스트림 요소를 하나의 값으로 리듀스하고 요약
    - 요소 그룹화
    - 요소 분할

2. 리듀싱과 요약
- 스트림 값에서 최댓값과 최솟값 검색
  - maxBy, minBy 두 메서드를 이용하는데, 인수로 Comparator를 받는다. 
  ```
  Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
  Optional<Dish> mostCaloreisDish = menu.stream().collect(maxBy(dishCaloriesComparator));
  ``` 
  - 스트림에는 객체의 숫자 필드의 합계나 평균등 반환 연산도 자주 사용된다. 이 연산을 요약이라 한다. 
- 요약 연산
  - Collectors.summingInt라는 특별한 요약 메서드가 있다. 
  - summingInt의 인수로 전달된 함수는 객체를 int로 매핑한 컬렉터를 반환한다. 
  - collect 메서드로 전달되면 요약 작업을 수행한다. 
  ```
  int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
  ```
  - summarizingInt는 요수 수, 합계, 평균, 최댓값, 최솟값 등을 추려낸다. 
- 문자열 연결
  - joining 메서드를 이용하면 스트림의 각 객체에 toString 메서드를 호출해 추출한 모든 문자열을 하나의 문자열로 연결한다. 
  - 내부적으로 StringBuilder를 이용해 문자열을 하나로 만든다. 
  - 연결된 두 요소 사이에 구분문자열을 넣을 수 있는 오버로디된 joining도 있다. 
  ```
  String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(","));
  ```
 - 범용 리듀싱 요약 연산 
   - 범용 Collectors.reducing으로 위에서 했던 작업들을 할 수 있다. 
   - 그냥 reducing을 사용안했던 이유는 프로그래밍적 편의성 때문이다.(가독성도 중요)
   ```
   int totalCalories1 = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> j+j));
   ```
- 컬렉션 프레임워크의 유연성: 같은 연산도 다양한 방식으로 수행할 수 있다.
  - reducing을 이용한 이전방식 대신 Integer 클래스의 sum을 이용하면 좀 더 단순화 할 수 있다. 
  ```
  int totalCalories1 = menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));
  ```
  - reduce 연산으로도 같은 결과를 도출할 수 있다. 
  ```
  int totalCalories1 = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
  ```
  - 마지막으로 IntStream으로 매핑한 다음 sum 메서드로 도출할 수 도 있다. 
- 자신의 상황에 맞는 최적의 해법 선택
  - 스트림 인터페이스에서 직접 제공하는 메서드를 이용하는것에 비해 컬렉터를 이용하는 코드가 더 복잡하다.
  - 하지만 재사용성과 커스터마이즈 가능성을 제공하는 높은 수준의 추상화와 일반화를 얻을 수 있다. 
  
3. 그룹화
- 명령형으로 그룹화를 구현하려면 까다롭다. 
- Collectors.groupingBy를 이용하면 쉽게 그룹화할 수 있다. 
  ```
  Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
  ```
  - 결과는 Map이 반환된다.
  - 단순한 속성 접근자 대신 더 복잡한 분류 기준이 필요한 상황에선 메서드 참조를 분류 함수로 사용할 수 없다. 
- 그룹화된 요소 조작
  - 다음과 같은 실수를 할 수 있다.
  ```
  Map<Dish.Type, List<Dish>> dishesByType = menu.stream().filter(d -> d.getCalories() > 500).collect(groupingBy(Dish::getType));
  ``` 
  - 위의 문제는 키로 오는 Type이 빠질 수 있다는 것이다. 필터링에서 아무것도 안나와서.
  - groupingBy 오버로드해 두 번째 인수에 프레디케이트를 이용한다. 
  ```
  Map<Dish.Type, List<Dish>> dishesByType = 
      menu.stream().collect(groupingBy(Dish::getType, filtering(d -> d.getCalories() > 500, toList())));
  ```
  - 프레디케이트로 그룹의 요소와 필터링 된 요소를 재그룹화 한다. 비어있던 FISH 키가 등록된다. 
  - 그룹화된 항목을 조작하는 다른 유용한 기능으로 매핑 함수를 이용해 요소를 변환하는 작업이다.
    - mapping 메서드를 이용하면 된다. 
    ```
    Map<Dish.Type, List<String>> dishNamesByType = menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
    ```
    - 두 수준의 리스트를 한 수준으로 평면화하려면 flatMap을 수행하면 된다. 
- 다수준의 그룹화(다시보기)
  - Collectors.goupingBy를 이용해 항목을 다수준으로 그룹화 할 수 있다. 
  - 바깥쪽 groupingBy 메서드에 스트림 항목을 분류할 두 번째 기준을 정의하는 내부 groupingBy를 전달해 두 수준으로 스트림의 항목을 그룹화할 수 있다. 
- 서브그룹으로 데이터 수집
  
4. 분할
- 분할 함수는 Boolean을 을 키로하는 맵을 반환한다. 
- 그룹화 맵은 최대 두 개의 그룹으로 분류된다. 
  ```
  Map<Boolean, List<Dish>> patitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian));
  patitionedMenu.get(true);
  ```
- 분할의 장점
  - 참, 거짓 두 가지 요소의 스트림 리스트를 모두 유지 한다는 것이 장점이다. 
  - 오버라이딩 된 메서드로 두번째 인수로 Collector를 전달 할 수 있다. 
  ```
  Map<Boolean, Map<Dish.Type, List<Dish>>> patitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
  ```
  
 5. Collector 인터페이스(다시보기)
 - Collector 인터페이스는 리듀싱 연산을 어떻게 구현할지 제공하는 메서드 집합으로 구성된다. 
 
---
## 병렬 데이터 처리와 성능(다시보기)
1. 병렬 스트림
- parallelStream을 호출하면 병렬 스트림이 생성된다. 
- 병렬 스트림을 이용하면 모든 멀티코어 프로ㅔㅅ서가 각각의 청크를 처리하도록 할당 할 수 있다. 
- 순차 스트림을 병렬 스트림으로 변환하기 
  - 순차 스트림에 parallel을 호출해도 스트림 자체에는 아무 변화가 없다. 
  - sequential로 병렬 스트림을 순차 스트림으로 바꿀 수 있다. 
- 병렬 스트림은 내부적으로 ForkJoinPool을 사용한다.
  - 프로세스 수가 스레드 수와 같다. 
- 스트림 성능 측정
  - 멀티코어 간 데이터 이동은 생각보다 비싸다.
  - 코어 간에 데이터 전송 시간보다 오래 걸리는 작업만 병렬로 다른 코어에서 수행하는 것이 바람직하다. 
- 병렬 스트림의 올바른 사용법
  - 병렬 스트림을 사용했을 때 이상한 결과에 당황하지 않으려면 상태 공유에 따른 부작용을 피해야 한다.
  - 공유된 가변 상태를 피하자. 
- 병렬 스트림 효과적으로 사용하기
  - 박싱을 주의하라
  - 요소의 순서에 의존하는 연산을 병렬로 처리하면 비싼 비용이 든다. 
    - findAny가 findFirst보다 성능이 더 좋다. 
    - 소량의 데이터에서는 병렬 스트림이 도움이 안된다. 
    - 최종 연산의 병합 과정 비용을 살펴보라. 
  - 스트림 소스와 분해성
    - ArrayList : 훌륭
    - LinkedList : 나쁨
    - IntStream.range : 훌륭
    - Srream.iterate : 나쁨
    - HashSet : 좋음
    - TreeSet 좋음

2. 포크/조인 프레임워크
- 포크/조인 프레임워크는 병렬화할 수 있는 작업을 재귀적으로 작은 작업으로 분할한 다음 서브태스크 각각의 결과를 합쳐 전체 결과를 만든다. 
- RecursiveTask 활용
  - 스레드 풀을 이용하려면 RecursiveTask<R>의 서브클래스를 만들어야 한다. 
  - compute를 구현해야 한다. 
    - 태스크를 서브태스크로 분할하는 로직과 더 이상 분할 할 수 없을 때 개별 서브 태스크의 결과를 생산할 알고리즘을 정의한다.
    ```java
    public class ForkJoinSumCalculator extends RecursiveTask<Long> {
        private final long[] numbers;
        private final int start;
        private final int end;
        public static final long THRESHOLD = 10_000;
    
        public ForkJoinSumCalculator(long[] numbers) {
            this(numbers, 0, numbers.length);
        }
    
        public ForkJoinSumCalculator(long[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }
    
        @Override
        protected Long compute() {
            int length = end - start;
            if(length < THRESHOLD) {
                return computeSequentially();
            }
            
            ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);
            leftTask.fork();  // 비동기로 실행
    
            ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length/2, length/2);
            
            Long rightResult = rightTask.compute();  // 두 번째 task를 동기 실행. 추가 분할 발생 가능성 있음
            Long leftResult = leftTask.join();  // 첫 번째 테스크의 결과를 읽거나 아직 결과가 없으면 기다림
            
            return leftResult + rightResult;
        }
    
        private Long computeSequentially() {
            long sum = 0;
            for(int i = start; i < end; i++) {
                sum +=numbers[i];
            }
            return sum;
        }
    }
    ```
  - 일반적으로 애플리케이션에서는 둘 이상의 ForkJoinPool을 사용하지 않는다. 싱글턴으로 저장해야 한다. 
- 포크/조인 프레임워크를 제대로 사용하는 방법
  - join 메서드를 태스크에 호출하면 태스크가 생산하는 결과가 준비될 때까지 호출자를 블록시킨다. 
  - RecusiveTask 내에서는 ForkJoinPool의 invoke 메서드를 사용하지 말아햐 한다. 
    - 대신 compute나 fork 메서드를 직접 호출할 수 있다. 
    - 순차 코드에서 병렬 계산을 시작할 때만 invoke를 사용한다. 
  - 서브테스크에 fork 메서드를 호출해 ForkJoinPool의 일정을 조절할 수 있다. 
  - 디버깅 하기가 어렵다. 
  - 순차처리보다 무조건 빠르다는 것은 버려야 한다. 
- 작업 훔치기
  - 작업 훔치기 기법에는 ForkJoinPool의 모든 스레드를 거의 공정하게 분할한다. 
  
3. Spliterator 인터페이스
- 분할 할 수 있는 반복자란 뜻이다.
- Iterator처럼 소스의 요소 탐색 기능을 제공하지만 병렬 작업에 특화되 있다.    
- 분할 과정
  - 스트림을 여러 스트림으로 분할하는 과정은 재귀적으로 일어난다. 
  - 첫 Spliterator에 trySplit을 호출하면 두 번째 Spliterator가 생성된다. 
  - trySplit이 null이 될 때까지 반복한다. 
  
---
# PART 3 스트림과 람다를 이용한 효과적 프로그래밍
## 컬렉션 API 개선
1. 컬렉션 팩토리
- 자바 9에서는 작은 컬렉션 객체를 쉽게 만들 수 있는 방법을 제공한다. 
  - Arrays.asList() 팩토리 메서드를 이용하면 된다. 
  - 고정크기로 만들므로 요소를 갱신할 순 없지만 새 요소를 추가하거나 요소를 삭제할 순 있다. 
  ```
  List<String> friends = Arrays.asList("Sejin", "NamTack", "YongNam");
  ```
  - UnsupportedOperatationException 예외 발생
    - 내부적으로 고정된 크기의 변환할 수 있는 배열로 구현되어 이 에러가 발생한다. 
- 리스트 팩토리
  - List.of 메서드로 간단하게 리스트를 만들 수 있다. 
  - 만든 리스트에 뭔가를 추가하면 UnsupportedOperatioException이 발생한다. 
  ```
  List<String> friends = List.of("Sejin", "NamTack", "YongNam");
  ```
  - 의도치 않게 변경하는 것을 막을 수 있다. 요소 자체가 변하는 건 막을수 없지만.
  - 데이터 처리 형식을 설정하거나 데이터를 변환할 필요가 없다면 사용하기 간편한 팩토리 메서드를 이용할 것을 권장한다. 
- 집합 팩터리
  - Set.of로 만들면 중복된 요소가 있을때 예외를 발생시킨다. 
- 맵 팩토리
  - 두 가지 방법이 있다.
  - Map.of에 키와 값을 번갈아 제공하는 방법이 있다. 
  ```
  Map<String, Integer> ageofFriends = Map.of("Sejin", 31, "Peter", 35, "James", 29);
  ```
  - 위 방법은 열 개 이하의 값을 만들때 유용하다. 
  - 열개 이상이면 Map.ofEntries 메서드를 이용하자. 
  
2. 리스트와 집합 처리
- 다음 메서드들은 메서드를 호출한 컬렉션 자체를 바꾼다. 
- removeIf 메서드
  - for문 안에서 List를 삭제할 때 반복자 상태가 컬렉션의 상태와 동기화 되지 않는 문제가 발생할 수 있다. 
  - removeIf 메서드로 위문제를 해결할 수 있다. 
  ```
  friends.removeIf(f -> f.length() > 10
  ```
- replaceAll 메서드
  - 리스트의 각 요소를 새로운 요소로 바꿀 수 있다. 
- sort 메서드

3. 맵 처리
- forEach 메서드
  - 기본엔 Map.Enty 반복자를 이용했지만 forEach를 이용하면 쉽게 구현된다.
- 정렬 메서드
  - Enty.comparingByValue
  - Enty.comparingByKey 
  - 위 두가지로 정렬할 수 있다. 
- getOrDefault 메서드
  - 기존엔 키가 존재 하지않으면 Exception이 발생했다.
  - 위 문제를 기본값을 반환하도록 해결한 것이다. 
- 계산 패턴
  - computeIfAbsent: 제공된 키에 해당하는 값이 없으면 키를 이용해 새 값을 계산하고 맵에 추가한다.
  - computeIfPresent: 제공된 키가 존재하면 새 값을 계산하고 맵에 추가
  - compute: 제공된 키로 새 값을 계산하고 맵에 저장
- 삭제 패턴
  - remove(key, value);로 쉽게 삭제 할 수 있다. 
- 교체 패턴
  - replaceAll: 
  - replace: 키가 존재하면 맵의 값을 바꿈. 
  
4. 개선된 ConcurrentHashMap
- 동시성 친화적이며 최신 기술을 반영한 HashMap(비동기) 버전이다. 
- 내부 자료구조의 특정 부분만 잠궈 동시 추가, 갱신 작업을 허용한다. 
- 동기화된 Hashtable 버전에 비해 읽기 쓰기 연산 성능이 월등하다. 

---
## 리펙터링, 테스팅, 디버깅
1. 가독성과 유연성을 개선하는 리팩터링
- 익명 클래스를 람다 표현식으로 리팩터링하기
  - 모든 익명 클래스를 람다 표현식으로 변환할 수 있는 것은 아니다
    - 익명클래스에서 this는 익명클래스 자신을 나타내지만 람다에선 람다를 감싸는 클래스를 가리킨다
    - 람다에선 변수를 가릴 수 없다. 
    ```
    int a = 10;
    Runnable r1 = () -> {
        int a = 2;  // 컴파일 에러
        System.out.println(a);
    };
    ```
  - 익명 클래스를 람다 표현식으로 바꾸면 콘텍스트 오버로딩에 따른 모호함이 초래 될 수 있다. 
    - 명시적 형변환을 이용해 모호함을 제거할 수 있다. 
- 람다 표현식을 메서드 참조로 리팩터링하기
  - 메서드 참조의 메서드명으로 코드의 의도를 명확히 알릴 수 있다. 
- 명령형 데이터 처리를 스트림으로 리팩터링하기
  - 반복자를 이용한 기존의 모든 컬렉션 처리 코드를 스트림 API로 바꿔야 한다. 
    - 데이터 처리 파이프라인의 의도를 더 명확히 보여주기 때문이다. 
- 코드 유연성 개선
  - 함수형 인터페이스 적용
    - 람다 표현식을 이용하려면 함수형 인터페이스가 필요하다. 
  - 조건부 연기 실행(다시보기)
  - 실행 어라운드
    - 매번 같은 준비, 종료 과정을 반복적으로 수행하는 코드가 있다면 이를 람다로 반환할 수 있다. 
    - 준비, 종료 과정을 처리하는 로직을 재사요함으로 코드 중복을 줄일 수 있다. 
    
2. 람다로 객체지향 디자인 패턴 리팩터링하기
- 디자인 패턴은 공통적인 소프트웨어 문제를 설계할 때 재사용 할 수 있는 검증된 청사진을 제공한다. 
- 전략
  - 전략 패턴은 한 유형의 알고리즘을 보유한 상태에서 런타임에 적절한 알고리즘을 선택하는 기법이다.
  - 세부분으로 구성 된다.
    - 알고리즘을 나타내는 인터페이스(Strategy 인터페이스)
    - 인터페이스를 구현한 클래스
    - 클라이언트
  ```
  Validator numbericValidator = new Validator((String s) -> s.matches("[a-z]+"));
  boolean b1 = numbericValidator.validate("aaaa");
  ```
    - 람다를 사용하면 디자인 패턴에 발생하는 자잘한 코드를 제거할 수 있다 
    - 람다 표현식은 코드 조각을 캡슐화한다. 
- 템플릿 메서드(여기 부터 다시)
  - 알고리즘의 개요를 제시한 다음 알고리즘의 일부를 고칠 수 있는 유연함을 제공해야 할 때 템플릿 메서드 디자인 패턴을 사용한다. 
  - 템플릿 메서드는 '이 알고리즘을 사용하고 싶은데 그대로는 안되고 조금 고쳐야 하는' 상황에 적합하다. 

3. 람다 테스팅(다시보기)
  
4. 디버깅
- 람다 표현식과 스트림은 기존 디버깅 기법을 무력화한다.
- 스택 트레이스 확인
  - 스택 프레임에서 예외 발생으로 프로그램 실행이 갑자기 중단되었다면 어디에서 멈췄고 어떻게 멈추게 되었는지 살펴볼 수 있다. 
  - 프로그램이 멈췄다면 프로그램이 어떻게 멈추게 되었는지 프레임별로 보이는 스택트레이스를 얻을 수 있다. 
  - 람다와 스택 트레이스
    - 람다엔 이름이 없어서 조금 복잡한 스택 트레이스가 생성된다. 
    - 컴파일러가 람다를 참조하는 이름을 만들어낸다. 
- 정보 로깅
  - peek 스트림 연산은 스트림의 각 요소를 소비한 것처럼 동작을 실행한다. 
  - forEach처럼 실제 스트림의 요소를 소비하진 않는다. 
  - peek는 자신이 확인한 요소를 파이프라인의 다음 연산으로 그대로 전달한다. 

---
## 람다를 이용한 도메인 전용 언어
- DSL은 특정 도메인을 대상으로 만들어진 특수 프로그래밍 언어다. 

1. 도메인 전용 언어(다시보기)
- 자바에서는 DSL이란 특정 비즈니스 도메인을 인터페이스로 만든 API라고 생각할 수 있다.
- JVM에서 이용할 수 있는 다른 DSL 해결책
  - DSL의 카테고리를 구분하는 가장 흔한 방법은 내부 DSL과 외부 DSL을 나누는 것이다. 
  - 내부 DSL
    - 자바로 구현한 DSL을 위미한다. 

---
# PART 4 매일 자바와 함께
## null 대신 Optional 클래스
1. 값이 없는 상황을 어떻게 처리할까?
- 보수적인 자세로 NullPointerException 줄이기
  - 대부분 null 확인 코드를 추ㄹ가해 예외 문제를 해결할 것이다. 
  ```
  public class Person {
      private Car car;
      
      public Car getCar() {
          return car;
      }
      
      public String getCarInsuranceName(Person person) {
          if(person != null) {
              Car car = person.getCar();
              if(car != null) {
                  Insurance insurance = car.getInsurance();
                  if(insurance != null) {
                      return insurance.getName();
                  }
              }
          }
          return "Unkown";
      }
  }
  ``` 
  - 위 코드는 중첩된 if가 추가된 반복 패턴 코드인 '깊은 의심'이다. 
  - 들여쓰기 수가 증가한다. 
  ```
  public String getCarInsuranceName(Person person) {
          if(person != null) {
              return "Unkown";
          }
          
          Car car = person.getCar();
          if(car != null) {
              return "Unkown";
          }
          Insurance insurance = car.getInsurance();
          if(insurance != null) {
              return "Unkown";
          }
          
          return insurance.getName();
  }
  ```
  - 위 코드도 네 개의 출구가 생겼기 때문에 좋지 않다. 
  
2. Optional 클래스 소개
- Optional<Car> 형식은 이 값이 없을 수 있음을 명시적으로 보여준다. 
- 모든 참조를 Optional로 대치하는 것은 바람직하지 않다. 
  - 보험회사는 반드시 이름을 가져야 하며 이름이 없는 보험회사를 발견했다면 예외를 처리하는 코드를 추가하는 것이 아니라 보험회사 이름이 없는 이유가 무엇인지 밝혀 문제를 해결해야 한다. 
    
3. Optional 적용 패턴
- Optional 객체 만들기
  - 빈 Optional
    - Optional.empty로 빈 객체를 얻을 수 있다. 
  - null이 아닌 값으로 Optional 만들기
    - Optional.of로 null이 아닌 값을 포함하는 객체를 만들 수 있다. 
  - null 값으로 Optional 만들기
    - Optional.ofNullable로 null값을 저정할 수 있는 Optional을 만들 수 있다.
- 맵으로 Optional의 값을 추출하고 변환하기
  - Optional은 map 메서드를 지원한다. 
  - Optional이 값을 포함하면 map의 인수로 제공된 함수가 값을 바꾼다. 
- flatMap으로 Optional 객체 연결
  - 보통 인수로 받은 함수를 스트림의 각 요소에 적용하면 스트림의 스트림이 만들어진다.
  - 그러나 flatMapt은 인수로 받은 함수를 적용해 생성된 각각의 스트림에서 콘텐츠만 남긴다.
  ```
  public String getCarInsuranceName(Optional<Person> person) {
          return person.flatMap(Person::getCar)
                  .flatMap(Car::getInsurance)
                  .map(Insurance::getName).orElse("Unkown");
  }
  ```   
  - null의 조건 분기 코드가 사라졌다!
  - 호출 체친 중 어떤 메서드가 빈 Optional을 반환한다면 전체 결과로 빈 Optional을 반환하고 아니면 관련 값을 포함하는 Optiona을 반환한다. 
-  도에인 모델에 Optional을 사용했을 때 데이터를 직렬화 할 수 없는 이유
  - Optinoal의 용도는 선택형 반환값을 지원하는 것이라고 못 박았다.
  - Optional 클래스는 필드 형식으로 사용할 것을 가정하지 않았으므로, Serializable 인터페이스를 구현하지 않는다. 
  - 도메인 모델에 Optional을 사용하면 직렬화 모델을 사용하는 도구나 프레임워크에서 문제가 생길 수 있다. 
  - 하지만 Optional을 사용해 도메인 모델을 구성하는 것이 바람직하다. 
  - 직렬화 모델이 필요하다면 다음과 같이 Optional로 값을 반환받을 수 있는 메서드를 추가하자.
    ```
    public Optional<Car> getCarAsOptional() {
            return Optional.ofNullable(car);
    }
    ```
- Optional 스트림 조작
  - 자바 9에서 Optional을 포함하는 스트림을 쉽게 처리할 수 있도록 메서드를 추가했다. 
- 디폴트 액션과 Optional 언랩
  - get()은 값을 읽는 가장 간단한 메서드면서 동시에 가장 안전하지 않다. 
    - 값이 없으면 NoSuchElementException을 발생시킨다. 
  - orElse를 이용하면 Optional이 값을 포함하지 않을 때 기본값을 제공할 수 있다. 
  - orElseGet(Supplier<?extends T> other)은 orElse 메서드에 대응하는 게으른 버전의 메서드다. 
    - Optional에 값이 없을때만 Supplier가 실행된다. 
  - orElseThrow(Supplier<? extends X> exceptionSupplier)는 Optional이 비어있을 때 예외를 발생시킨다. 
    - get과 비슷하지만 예외의 종류를 선택할 수 있다.
  - ifPresent(Consumer<? super T> consumer)를 이용하면 값이 존재할 때 인수로 넘겨준 동작을 실행한다. 
  - ifPresentorElse(Consumer<? super T> action, Runnable emptyAction)이 자바 9에 추가 됐다.
    - Optional이 비었을 때 실행할 수 있는 Runnable을 인수로 받는다. 
- 필터로 특정값 거르기
  - filter 메서드로 거를 수 있다. 
  - Optional 객체가 프레디케이트와 일치하면 그 값을 반환하고, 그렇지 않으면 빈 Optional 객체를 반환한다. 
  
4. Optional을 사용한 실용 예제
- 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기
  - 기존 맵의 방식을 다음처럼 바꿀 수 있다 .
  ```
  Optional<Object> value = Optional.ofNullable(map.get("key"));
  ```
  - null일 수 있는 값을 Optional로 안전하게 변환할 수 있다.
- 예외와 Optional 클래스
  ```
  public static Optional<Integer> stringToInt(String s) {
          try {
              return Optional.of(Integer.parseInt(s));
          } catch (NumberFormatException e) {
              return Optional.empty();
          }
  }
  ```
- 기본형 Optional을 사용하지 말아햐 하는 이유
  - Optional<Integer> 대산 OptionalInt를 반환할 수 있다. 
  - Optional의 최대 요소 수는 한 개 이므로 Optional에서는 기본형 특화 클래스로 성능을 개선할 수 없다. 
  - 기본형 특화 Optional 사용을 권장하지 않는다. 
  
---
## 새로운 날짜와 시간 API
1. LocalDate, LocalTime, Instant, Duration, Period 클래스
- LocalDate와 LocalTime 사용
  - LocalDate 인스턴스는 시간을 제외한 날짜를 표현하는 불변 객체다. 
  - 정적 팩토리 메서드 of로 만들 수 있다. 
  - now는 시스템 시계의 정보를 이용해 현재 날짜 정보를 얻는다. 
  - parse로 문자열을 LocalDate나 LocalTime을 만들 수 있다.
    - parse에 DateTimeFormatter를 전달할 수도 있다. 
  ```
    LocalDate date = LocalDate.now();

	int year  = date.getYear();

	// ChronoField 사용방법
	int year2 = date.get(ChronoField.YEAR);

	LocalTime time = LocalTime.of(13, 45, 20);
	int hour = time.getHour();

	// 문자열로 만드는 방법 parse이용
	LocalDate date2 = LocalDate.parse("2020-04-06");
  ```
- 날짜와 시간 조합
  - LocalDateTime은 LocaDate와 LocaTime을 쌍으로 갖는 복합 클래스다.
  - LocalDateTime에서 toLocalDate(), toLocalTime()으로 인스턴스를 추출할 수 있다. 
  ```
    LocalDateTime ldt = LocalDateTime.of(2020, Month.MARCH, 4, 12, 20, 0);
    LocalDate ld = ldt.toLocalDate();
    LocalTime lt = ldt.toLocalTime();  
  ```
- Instant 클래스: 기계의 날짜와 시간
  - Java의 Instant 클래스에서 기계적인 관점에서 시간을 표현한다. (유닉스 에포크 시간 기준)
  팩토리 메서드 ofEpochSecond에 초를 넘겨줘서 Instant 클래스 인스턴스를 만들 수 있다. 
- Duration과 Period 정의
  - LocalDate는 사람이 사용하도록, Instant는 기계가 사용하도록 만들어진 클래스로 두 인스턴스는 서로 혼합할 수 없다. 
  - Duration 클래스는 초와 나노초로 시간 단위를 표현하므로 메서드에 LocalDate를 전달할 수 없다.
  - 년, 월, 일로 시간을 표현할 때는 Period 클래스를 사용한다. 
  ```
    Duration threeMinutes = Duration.ofMinutes(3);
    Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);

    Period tenDays = Period.ofDays(10);
  ```

2. 날짜 조정, 파싱, 포매팅
- TemporalAdjusters 사용하기
  - 다음 주 일요일, 돌아오는 평일 등 다양한 상황에서 사용할 수 있도록 TemporalAdjusters를 제공한다. 
  ```
    LocalDate date3 = date.with(nextOrSame(DayOfWeek.SUNDAY));
    LocalDate date4 = date.with(lastDayOfMonth());
  ```
  - 필요한 기능이 없을때 커스텀도 할 수 있다. 
  ```
    public class NextWorkingDay implements TemporalAdjuster {

        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if(dow == DayOfWeek.FRIDAY)
                dayToAdd = 3;
            else if(dow == DayOfWeek.SATURDAY)
                dayToAdd = 2;
            
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        }
    }
  ```
- 날짜와 시간 객체 출력과 파싱
  - DateTimeFormatter로 날짜나 시간을 특정 형식의 문자열로 만들 수 있다. 
  ```
  LocalDate date5 = LocalDate.parse("20200406", DateTimeFormatter.BASIC_ISO_DATE);
  ```
  - 기존 DateFormat과는 달리 DateTimeFormatter는 스레드에서 안전하게 사용할 수 있다. 
  ```
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate date6 = LocalDate.of(2020,4,6);
    String formattedDate = date6.format(formatter);
    LocalDate date7 = LocalDate.parse(formattedDate, formatter);
  ```

3. 다양한 시간대와 캘린더 활용 방법
- 기존 TimeZone을 대체할 수 있는 ZoneId 클래스가 새롭게 등장했다. 이는 불변 클래스다.
- 시간대 사용하기
  - ZoneId의 getRules()를 이용해 해당 시간대의 규정을 획득할 수 있다. 

---
## 디폴트 메서드
- 디폴터 메서드는 주로 라이브러리 설계자들이 사용한다. 

1. 변화하는 API
- 사용자가 겪는 문제
  - 인터페이스에 새로운 메서드를 추가하면 바이너리 호환성은 유지된다. 
    - 새로 추가된 메서드를 호출하지만 않으면 새로운 메서드 구현이 없어도 기존 클래스 파일 구현이 잘 동작한다는 의미다. 
  - 공개된  API를 고치면 기존 버전과의 호환성 문제가 발생한다. 
  - 디폴트 메서드로 이 모든 문제를 해결할 수 있다. 

3. 디폴트 메서드 활용 패턴
- 선택형 메서드
  - Iterator를 예로 보면 remove 같은 메서드는 기분 구현을 제공할 수 있으므로 인터페이스를 구현하는 클래스에서 빈 구현을 제공할 필요가 없다. 
- 동작 다중 상속
  - 자바에선 클래스는 한 개의 다른 클래스만 상속할 수 있지만 인터페이스는 여러 개 구현할 수 있다. 
- 옳지 못한 상속
  - 상속으로 코드 재사용 문제를 모두 해결할 수 있는 것은 아니다.
  - 한 개의 메서드를 재사용하려고 1-개의 메서드와 필드가 정의되어 있는 클래스를 상속받는 것은 좋은 생각이 아니다. 
  - 이럴 땐, 델리게이션, 즉 멤버 변수를 이용해 클래스에서 필요한 메서드를 직접 호출하는 메서드를 작성하는게 좋다. 

4. 해석 규칙
- 디폴트 메서드 때문에 같은 시그니처를 갖는 디폴트 메서드를 상속받는 상황이 생길 수 있다. 
- 알아야 할 세 가지 해결 규칙
  - 다른 클래스나 인터페이스로부터 같은 시그니처를 갖는 메서드를 상속받을 때는 세 가지 규칙을 따라야 한다. 
    - 클래스가 항상 이긴다. 디폴트 메서드보다 우선권을 가진다.
    - 1번 규칙 이외의 상황에선 서브인터페이스가 이긴다. 
    - 여전히 디폴터 메서드의 우선순위가 결정되지 않았다면 여러 인터페이스를 상속받는 클래스가 명시적으로 디폴트 메서드를 오버라이드하고 호출해야 한다. 
- 충돌 그리고 명시적인 문제 해결
  - 충돌 해결
    - 자바 8에서는 X.super.m(...) 형태의 문법을 제공한다.
    - X는 호출하려는 메스드 m의 슈퍼인터페이스다.
    ```
    public class C implements B, A {

        @Override
        public void hello() {
            B.super.hello();
        }
    }
    ```
    
---


