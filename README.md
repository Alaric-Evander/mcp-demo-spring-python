# Rapport  : Système Chatbot des entreprises marocaines avec Spring Boot, LLM et MCP (Java + Python)

## Objectif du Projet
Ce projet vise à développer un chatbot intelligent pour assister les utilisateurs dans leurs requêtes sur les entreprises marocaines, en s'appuyant sur :
des modèles de langage (LLM via Ollama + LLaMA3) et même OpenAi et Claude,
une architecture multi-agents et multi-outils (Java + Python),
le protocole MCP (Model Context Protocol) pour exposer et invoquer dynamiquement des outils.
L'interface utilisateur (HTML/JS) permet une expérience fluide via SSE (Server-Sent Events), et le back-end Spring communique avec des serveurs d'outils à la fois en Java (Spring) et Python (FastAPI).

## Architecture Globale

+----------------+        +------------------+       +-----------------+
| Interface Web  | <----> | Spring Boot       | <---> | MCP Tools (Java |
| HTML/CSS/JS    |        | (mcp-client)      |       |  + Python)      |
+----------------+        +------------------+       +-----------------+
        |                        |  └---> LLM (Ollama / llama3.1)
        |
        └---> chat-stream (SSE)

## Cycle de Fonctionnement Complet

### 1.Démarrage des serveurs :
mcp-server (Java) expose les outils annotés via @Util (StockTools).
python-mcp-server démarre via server.py et expose des fonctions Python comme outils MCP.

### 2.Lancement du mcp-client :
Se connecte au LLM Ollama.
Connecte dynamiquement les outils Java et Python via ToolCallbackProvider.
Lit mcp-servers.json pour démarrer server.py.

### 3.Connexion HTML (à /chat-stream) :
L’interface JS établit une connexion SSE avec /api/ai/chat-stream pour recevoir les réponses.
L’état de connexion est mis à jour (connecté, reconnecting...).

### 4.Envoi de message utilisateur :
L’interface appelle POST /api/ai/send.
AIRestController transmet le message à AIAgent.askLLM().
Le LLM traite la requête avec possibilité d’appeler un outil MCP.

### 5.Appel d’outil MCP :
Le LLM identifie qu’un outil peut répondre (ex: get_info_about en Python).
Le chatClient appelle dynamiquement l’outil via le protocole MCP.
La réponse est renvoyée via SSE à l’interface.

### 6.Affichage HTML :
La réponse est analysée (structurée, erreur, texte simple).
La réponse est affichée dans la bulle du chatbot.

## Technologies Utilisées

### Composant      Technologie

LLM                Ollama + LLaMA3.1

Back-end           Spring Boot (Spring AI, MCP)

Serveur outils     Java (annotations) + Python

UI                 HTML, CSS, Vanilla JS

Protocole          MCP (Model Context Protocol)

Connexion          Server-Sent Events (SSE)

## Exemple de Scénario

### Utilisateur : 
"Donne-moi les infos sur un employé appelé Ahmed"

### Cycle :
LLM reçoit la question via askLLM().
Il identifie que get_info_about(name) peut répondre.
Il appelle le serveur Python MCP.
Le résultat JSON est renvoyé et formaté dans l'interface.



## Les Captures d'écran

  POSTMAN requetes :
<img width="824" height="905" alt="Capture" src="https://github.com/user-attachments/assets/fb45fb48-ab53-4747-8e5a-63a6b41d550b" />
<img width="835" height="990" alt="Capture1" src="https://github.com/user-attachments/assets/4a8a1e89-627e-4644-ad5c-5927f57b969d" />
<img width="820" height="705" alt="Capture2" src="https://github.com/user-attachments/assets/44efdc14-cd92-4b94-8045-43a497913866" />
<img width="828" height="670" alt="Capture3" src="https://github.com/user-attachments/assets/71db0a16-1cd5-4a4a-8a53-ba9ed9dadab8" />
<img width="813" height="828" alt="Capture4" src="https://github.com/user-attachments/assets/7eb019ca-c479-4ffb-85c5-c3f37e8b56e7" />
<img width="833" height="836" alt="Capture5" src="https://github.com/user-attachments/assets/171b275b-39ae-4fe7-bbdb-a24b3b7e7a72" />
<img width="825" height="750" alt="Capture6" src="https://github.com/user-attachments/assets/44115986-fb99-4878-bdbc-517143768b77" />
<img width="807" height="333" alt="Capture7" src="https://github.com/user-attachments/assets/b96e5ef5-01ef-4897-9170-cac717ed0c7f" />
<img width="764" height="809" alt="Capture8" src="https://github.com/user-attachments/assets/fb45394f-8898-481e-9f4c-8a5d5730ba08" />
<img width="611" height="623" alt="Capture9" src="https://github.com/user-attachments/assets/0d0afba4-4ab3-4ab6-8595-f98b7ff88472" />
<img width="767" height="325" alt="Capture10" src="https://github.com/user-attachments/assets/c71793ba-316d-49c1-b862-f0da36aaba52" />
  
  Python :
  <img width="913" height="603" alt="Python" src="https://github.com/user-attachments/assets/6a348483-a84a-4ff0-9fbc-727d630140e6" />

  Test:
  <img width="525" height="88" alt="Capture11" src="https://github.com/user-attachments/assets/64a92960-dcfe-48a0-9df8-3c8826e146a1" />

  Swagger :
  <img width="608" height="164" alt="Swagger" src="https://github.com/user-attachments/assets/696b1376-d98c-4a9f-8064-2cce057431f1" />
  <img width="870" height="961" alt="Swagger2" src="https://github.com/user-attachments/assets/1b8286a0-ad72-41e0-a82f-daab3a932781" />

  Interface Frontend :
  <img width="1777" height="884" alt="image" src="https://github.com/user-attachments/assets/094b183d-ae04-4e8b-9e20-59f93bbced87" />
  <img width="1014" height="818" alt="image" src="https://github.com/user-attachments/assets/25128e4b-6ff0-45f5-8318-16df0d20975b" />
  <img width="975" height="706" alt="image" src="https://github.com/user-attachments/assets/5457a6cb-8b75-4c04-9511-ecbb18ceee2c" />



