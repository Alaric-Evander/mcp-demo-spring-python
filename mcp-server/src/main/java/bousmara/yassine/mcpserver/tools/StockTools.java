package bousmara.yassine.mcpserver.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class StockTools {
    private final List<Company> companies = List.of(
            new Company("Maroc Telecom", "Telecommunications", 36.7, 10600, "Maroc", "www.iam.ma"),
            new Company("OCP", "Extraction minière", 95.2, 20000, "Maroc", "www.ocpgroup.ma")
    );

    // New tool to get all companies' data
    @Tool(name = "get_all_companies_info",
            description = "Get complete information for all available companies including stock data")
    public String getAllCompaniesInfo() {
        return companies.stream()
                .map(company -> {
                    Stock stock = getStockByCompanyName(company.name());
                    return formatCompanyResponse(company, stock);
                })
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    @Tool(name = "get_company_full_info", description = "Get complete information about a company including stock data")
    public String getCompanyFullInfo(String companyName) {
        Company company = companies.stream()
                .filter(c -> c.name().equalsIgnoreCase(companyName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Stock stock = getStockByCompanyName(companyName);

        return formatCompanyResponse(company, stock);
    }

    @Tool(name = "get_stock_info", description = "Get current stock information for a company")
    public String getStockInfo(String companyName) {
        Company company = companies.stream()
                .filter(c -> c.name().equalsIgnoreCase(companyName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Stock stock = getStockByCompanyName(companyName);

        return formatStockResponse(company, stock);
    }

    @Tool
    private Stock getStockByCompanyName(String name) {
        // Simulate realistic stock variations
        double basePrice = name.equals("Maroc Telecom") ? 500 : 300;
        double variation = (Math.random() * 10) - 5; // -5% to +5%
        double currentPrice = basePrice * (1 + variation/100);

        return new Stock(name, LocalDate.now(), currentPrice, variation);
    }

    private String formatCompanyResponse(Company company, Stock stock) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

        return String.format("""
            *%s* - Informations complètes:
            
            * Secteur d'activité: %s
            * Chiffre d'affaires: %.1f milliards MAD
            * Effectif: %,d employés
            * Pays: %s
            * Site web: %s
            
            *Informations boursières (%s):*
            * Cours actuel: %.2f MAD
            * Variation: %.2f%%
            """,
                company.name(),
                company.activity(),
                company.turnover(),
                company.employeesCount(),
                company.country(),
                company.website(),
                stock.date().format(formatter),
                stock.stock(),
                stock.variation()
        );
    }

    private String formatStockResponse(Company company, Stock stock) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

        return String.format("""
            *%s* - Données boursières:
            
            * Date: %s
            * Cours: %.2f MAD
            * Variation: %.2f%%
            * Secteur: %s
            """,
                company.name(),
                stock.date().format(formatter),
                stock.stock(),
                stock.variation(),
                company.activity()
        );
    }

    record Company(
            String name,
            String activity,
            double turnover,
            int employeesCount,
            String country,
            String website
    ) {}

    record Stock(
            String companyName,
            LocalDate date,
            double stock,
            double variation
    ) {}
}