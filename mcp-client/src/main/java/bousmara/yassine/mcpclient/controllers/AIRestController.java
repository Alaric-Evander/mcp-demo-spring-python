package bousmara.yassine.mcpclient.controllers;

import bousmara.yassine.mcpclient.agents.AIAgent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")  // Base path for your controller
public class AIRestController {

    private final AIAgent agent;

    public AIRestController(AIAgent agent) {
        this.agent = agent;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String query) {
        return agent.askLLM(query);
    }
}