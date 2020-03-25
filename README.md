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

