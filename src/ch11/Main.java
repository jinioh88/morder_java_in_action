package ch11;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap();
        Optional<Object> value = Optional.ofNullable(map.get("key"));
    }

    public int readDuration(Properties props, String name) {

        return Optional.ofNullable(props.getProperty(name))
                .flatMap(OptionalUtility::stringToInt).filter(i -> i>0).orElse(0);
    }

}
