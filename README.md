모던 자바 인 액션 스터디
# Part1 기초
## CH2 동작 파라미터화 코드 전달하기
동작 파라미터화를 이용하면 자주 바뀌는 요구사항에 효과적으로 대응할 수 있다. 
 
동작 파라미터화란 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미한다. 

결과적으로 코드블럭에 따라서 메서드의 동작이 파라미터화된다.

동작 파라미터화를 추가하려면 쓸데없는 코드가 늘어나지만 자바8은 람다 표현식으로 이 문제를 해결한다. 

1. 변화하는 요구사항에 대응하기
    - 첫 번째 시도: 녹색 사과 필터링
    ```java
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
    ```java
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
    ```java
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
    ```java
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
      ```java
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
  - 
  


