from mcp.server.fastmcp import FastMCP

mcp = FastMCP("Python-MCP-Server")

@mcp.tool()
def get_info_about(name: str) -> dict:
    """
    Get Information about a given employee name:
    - First Name
    - Last Name
    - Salary
    - Email
    """
    return {
        "first_name": name,
        "last_name": "Mohamed",
        "salary": 5400,
        "email": "med@gmail.com"
    }

@mcp.tool()
def greet_user() -> str:
    """
    Greet a user by name with a friendly welcome message.
    """
    return f"Bonjour, comment puis je vous aider aujourd'hui?"


if __name__ == "__main__":
    mcp.run()