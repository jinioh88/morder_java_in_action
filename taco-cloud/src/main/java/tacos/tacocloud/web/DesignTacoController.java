package tacos.tacocloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {
    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", ),
                new Ingredient("COTO", "Corn Tortilla", ),
                new Ingredient("GRBF", "", ),
                new Ingredient("CARN", "", ),
                new Ingredient("TMTO", "", ),
                new Ingredient("LETC", "", ),
                new Ingredient("CHED", "", ),
                new Ingredient("JACK", "", ),
                new Ingredient("SLSA", "", ),
                new Ingredient("SRCR", "", )
        );
    }
}
