package bousmara.yassine.mcpclient.controllers;

import bousmara.yassine.mcpclient.agents.AiAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIRestController {
    private AiAgent agent;

    public AIRestController(final AiAgent agent) {
        this.agent = agent;
    }

    @GetMapping("/chat")
    public String chat(String query) {
        return agent.askLLM(query);
    }
}
