package bousmara.yassine.mcpclient.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class AIAgent {
    private ChatClient chatClient;

    public AIAgent(ChatClient.Builder chatClient, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClient
                .defaultSystem(""" 
                                        Answer the user queries based on the existing tools. 
                                        You are a financial assistant specializing in Moroccan companies.
                                        Important rules:
                                        1. Always use available tools to obtain accurate data.
                                        2. For company inquiries, use get_company_full_info.
                                        3. For stock-specific inquiries, use get_stock_info.
                                        4. Always format responses in clear Markdown.
                                        5. Financial values must have exactly 2 decimal places.
                                        6. Always include the reference date.
                                        7. Respond in French unless otherwise requested.
                                        8. If asked for all companies use 
                                        For a specific employee informations use the get_info_about tool. If no name was specified take "Youssefi" as name by default.
                                        
                                        """)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(MessageWindowChatMemory.builder().build()).build())
                .build();
    }

    public String askLLM(String question) {
        return chatClient.prompt()
                .user(question).call().content();
    }
}