package bousmara.yassine.mcpclient;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class McpClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(List<McpSyncClient> clients) {
        return args -> {
            clients.forEach(client -> {
            client.listTools().tools().forEach(tool -> {
                System.out.println("------------------");
                System.out.println(tool.name());
                System.out.println(tool.description());
                System.out.println(tool.inputSchema());
                System.out.println("------------------");
            });
            });
        };
    }
    }



