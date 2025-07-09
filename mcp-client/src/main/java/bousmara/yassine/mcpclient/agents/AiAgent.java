package bousmara.yassine.mcpclient.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;

public class AiAgent {
    private ChatClient chatClient;

    public AiAgent(ChatClient.Builder chatClient, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClient.defaultToolCallbacks(toolCallbackProvider).defaultSystem("Answer the user question using provided tools").build();
    }

    public String askLLM(String query){
        return chatClient.prompt().user(query).call().content();
    }
}
