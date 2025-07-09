const API_BASE = 'http://127.0.0.1:8066'; // Make sure this matches your Spring Boot port

let eventSource = null;

function connectSSE() {
    eventSource = new EventSource(`${API_BASE}/api/ai/chat-stream`);

    eventSource.onopen = () => {
        console.log("✅ Connected to AI stream");
    };

    eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        appendMessage(data.sender, data.content);
    };

    eventSource.onerror = (err) => {
        console.error("SSE Error:", err);
        eventSource.close();

        // Reconnect after 3 seconds
        setTimeout(connectSSE, 3000);
    };
}

function sendMessage(message) {
    fetch(`${API_BASE}/api/ai/send`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            sender: "User",
            content: message
        })
    }).catch(err => console.error('❌ Send failed:', err));
}

function appendMessage(sender, content) {
    const messageBox = document.getElementById('messages');
    const messageDiv = document.createElement('div');
    messageDiv.innerHTML = `<strong>${sender}:</strong> ${content}`;
    messageBox.appendChild(messageDiv);
    messageBox.scrollTop = messageBox.scrollHeight;
}

document.getElementById('input-form').addEventListener('submit', function (e) {
    e.preventDefault();

    const input = document.getElementById('message-input');
    const message = input.value.trim();

    if (message) {
        console.info("message : "+message);
        sendMessage(message);
        appendMessage("You", message);
        input.value = '';
    }
});

// Connect SSE when the page loads
window.onload = connectSSE;