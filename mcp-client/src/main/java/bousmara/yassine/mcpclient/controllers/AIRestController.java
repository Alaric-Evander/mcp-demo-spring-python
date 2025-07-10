package bousmara.yassine.mcpclient.controllers;

import bousmara.yassine.mcpclient.agents.AIAgent;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/ai")
public class AIRestController {

    private final AIAgent agent;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public AIRestController(AIAgent agent) {
        this.agent = agent;
    }

    // Existing endpoint (kept for compatibility)
    @GetMapping("/chat")
    public String chat(@RequestParam String query) {
        return agent.askLLM(query);
    }

    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream() {
        System.out.println("HELOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO MAN");
        SseEmitter emitter = new SseEmitter(0L); // no timeout for long-lived connection
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        // 💡 Send a "ping" event immediately to establish connection
        try {
            emitter.send(SseEmitter.event()
                    .name("init")
                    .data("Connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(emitter);
        }

        return emitter;
    }


    // New endpoint for sending messages
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody ChatMessage message) throws IOException {
        String response = agent.askLLM(message.getContent());

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .data(new ChatMessage("AI Agent", response))
                        .id(String.valueOf(System.currentTimeMillis())));
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }

        return ResponseEntity.accepted().build(); // ✅ 202 with empty body
    }


    // DTO for message exchange
    public static class ChatMessage {
        private String sender;
        private String content;

        // Constructors, getters, and setters
        public ChatMessage() {}

        public ChatMessage(String sender, String content) {
            this.sender = sender;
            this.content = content;
        }

        // Standard getters and setters
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}