class CricketChatbot {
    constructor() {
        this.apiBaseUrl = 'http://localhost:8080/api/chat';
        this.sessionId = null;
        this.isTyping = false;
        
        this.initializeElements();
        this.attachEventListeners();
        this.initializeSession();
        this.setupAutoResize();
    }
    
    initializeElements() {
        this.messageInput = document.getElementById('messageInput');
        this.sendBtn = document.getElementById('sendBtn');
        this.chatMessages = document.getElementById('chatMessages');
        this.welcomeSection = document.getElementById('welcomeSection');
        this.typingIndicator = document.getElementById('typingIndicator');
        this.charCount = document.getElementById('charCount');
        this.newChatBtn = document.getElementById('newChatBtn');
        this.historyBtn = document.getElementById('historyBtn');
        this.historyModal = document.getElementById('historyModal');
        this.closeModal = document.getElementById('closeModal');
        this.historyContent = document.getElementById('historyContent');
        this.loadingOverlay = document.getElementById('loadingOverlay');
    }
    
    attachEventListeners() {
        // Send message events
        this.sendBtn.addEventListener('click', () => this.sendMessage());
        this.messageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                this.sendMessage();
            }
        });
        
        // Input events
        this.messageInput.addEventListener('input', () => this.handleInputChange());
        
        // Suggestion cards
        document.querySelectorAll('.suggestion-card').forEach(card => {
            card.addEventListener('click', () => {
                const message = card.getAttribute('data-message');
                this.messageInput.value = message;
                this.handleInputChange();
                this.sendMessage();
            });
        });
        
        // Header buttons
        this.newChatBtn.addEventListener('click', () => this.startNewChat());
        this.historyBtn.addEventListener('click', () => this.showChatHistory());
        
        // Modal events
        this.closeModal.addEventListener('click', () => this.hideModal());
        this.historyModal.addEventListener('click', (e) => {
            if (e.target === this.historyModal) {
                this.hideModal();
            }
        });
        
        // Escape key to close modal
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && this.historyModal.style.display === 'block') {
                this.hideModal();
            }
        });
    }
    
    async initializeSession() {
        try {
            this.showLoading();
            const response = await fetch(`${this.apiBaseUrl}/session/new`);
            const data = await response.json();
            this.sessionId = data.sessionId;
            
            // Check for existing chat history
            await this.loadChatHistory();
            
        } catch (error) {
            console.error('Failed to initialize session:', error);
            this.showError('Failed to connect to the server. Please refresh the page.');
        } finally {
            this.hideLoading();
        }
    }
    
    async loadChatHistory() {
        if (!this.sessionId) return;
        
        try {
            const response = await fetch(`${this.apiBaseUrl}/history/${this.sessionId}`);
            const messages = await response.json();
            
            if (messages.length > 0) {
                this.welcomeSection.style.display = 'none';
                messages.forEach(msg => {
                    this.addMessage(msg.userMessage, 'user', msg.timestamp);
                    this.addMessage(msg.botResponse, 'bot', msg.timestamp);
                });
                this.scrollToBottom();
            }
        } catch (error) {
            console.error('Failed to load chat history:', error);
        }
    }
    
    handleInputChange() {
        const message = this.messageInput.value.trim();
        const charCount = this.messageInput.value.length;
        
        this.charCount.textContent = `${charCount}/1000`;
        this.sendBtn.disabled = message.length === 0 || this.isTyping;
        
        if (charCount > 900) {
            this.charCount.style.color = '#e74c3c';
        } else if (charCount > 700) {
            this.charCount.style.color = '#f39c12';
        } else {
            this.charCount.style.color = '#6c757d';
        }
    }
    
    setupAutoResize() {
        this.messageInput.addEventListener('input', () => {
            this.messageInput.style.height = 'auto';
            this.messageInput.style.height = Math.min(this.messageInput.scrollHeight, 120) + 'px';
        });
    }
    
    async sendMessage() {
        const message = this.messageInput.value.trim();
        if (!message || this.isTyping) return;
        
        // Hide welcome section
        this.welcomeSection.style.display = 'none';
        
        // Add user message
        this.addMessage(message, 'user');
        
        // Clear input
        this.messageInput.value = '';
        this.messageInput.style.height = 'auto';
        this.handleInputChange();
        
        // Show typing indicator
        this.showTyping();
        
        try {
            const response = await fetch(`${this.apiBaseUrl}/message`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    message: message,
                    sessionId: this.sessionId
                })
            });
            
            const data = await response.json();
            
            // Update session ID if new
            if (data.sessionId && data.sessionId !== this.sessionId) {
                this.sessionId = data.sessionId;
            }
            
            // Hide typing and add bot response
            this.hideTyping();
            this.addMessage(data.response, 'bot');
            
        } catch (error) {
            console.error('Error sending message:', error);
            this.hideTyping();
            this.addMessage('Sorry, I encountered an error. Please try again.', 'bot', null, true);
        }
    }
    
    addMessage(content, sender, timestamp = null, isError = false) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${sender}`;
        
        const bubbleDiv = document.createElement('div');
        bubbleDiv.className = 'message-bubble';
        bubbleDiv.textContent = content;
        
        if (isError) {
            bubbleDiv.style.background = '#fee';
            bubbleDiv.style.borderColor = '#fcc';
            bubbleDiv.style.color = '#c33';
        }
        
        messageDiv.appendChild(bubbleDiv);
        
        // Add timestamp and avatar for bot messages
        if (sender === 'bot') {
            const infoDiv = document.createElement('div');
            infoDiv.className = 'message-info';
            
            const avatarDiv = document.createElement('div');
            avatarDiv.className = 'bot-avatar';
            avatarDiv.innerHTML = '<i class="fas fa-robot"></i>';
            
            const timeSpan = document.createElement('span');
            timeSpan.textContent = timestamp ? 
                new Date(timestamp).toLocaleTimeString() : 
                new Date().toLocaleTimeString();
            
            infoDiv.appendChild(avatarDiv);
            infoDiv.appendChild(timeSpan);
            messageDiv.appendChild(infoDiv);
        } else {
            const infoDiv = document.createElement('div');
            infoDiv.className = 'message-info';
            infoDiv.style.textAlign = 'right';
            
            const timeSpan = document.createElement('span');
            timeSpan.textContent = timestamp ? 
                new Date(timestamp).toLocaleTimeString() : 
                new Date().toLocaleTimeString();
            
            infoDiv.appendChild(timeSpan);
            messageDiv.appendChild(infoDiv);
        }
        
        this.chatMessages.appendChild(messageDiv);
        this.scrollToBottom();
    }
    
    showTyping() {
        this.isTyping = true;
        this.typingIndicator.style.display = 'flex';
        this.sendBtn.disabled = true;
        this.scrollToBottom();
    }
    
    hideTyping() {
        this.isTyping = false;
        this.typingIndicator.style.display = 'none';
        this.handleInputChange();
    }
    
    scrollToBottom() {
        setTimeout(() => {
            this.chatMessages.scrollTop = this.chatMessages.scrollHeight;
        }, 100);
    }
    
    async startNewChat() {
        try {
            this.showLoading();
            
            // Clear current chat
            this.chatMessages.innerHTML = '';
            this.welcomeSection.style.display = 'block';
            this.messageInput.value = '';
            this.handleInputChange();
            
            // Get new session
            const response = await fetch(`${this.apiBaseUrl}/session/new`);
            const data = await response.json();
            this.sessionId = data.sessionId;
            
        } catch (error) {
            console.error('Failed to start new chat:', error);
            this.showError('Failed to start new chat. Please try again.');
        } finally {
            this.hideLoading();
        }
    }
    
    async showChatHistory() {
        if (!this.sessionId) {
            this.historyContent.innerHTML = '<p>No chat history available.</p>';
            this.showModal();
            return;
        }
        
        try {
            const response = await fetch(`${this.apiBaseUrl}/history/${this.sessionId}`);
            const messages = await response.json();
            
            if (messages.length === 0) {
                this.historyContent.innerHTML = '<p>No chat history available.</p>';
            } else {
                let historyHtml = '<div class="history-messages">';
                messages.forEach(msg => {
                    const time = new Date(msg.timestamp).toLocaleString();
                    historyHtml += `
                        <div class="history-message">
                            <div class="history-user-msg">
                                <strong>You:</strong> ${this.escapeHtml(msg.userMessage)}
                                <small>(${time})</small>
                            </div>
                            <div class="history-bot-msg">
                                <strong>AI:</strong> ${this.escapeHtml(msg.botResponse)}
                            </div>
                        </div>
                    `;
                });
                historyHtml += '</div>';
                this.historyContent.innerHTML = historyHtml;
            }
            
            this.showModal();
            
        } catch (error) {
            console.error('Failed to load chat history:', error);
            this.historyContent.innerHTML = '<p>Failed to load chat history.</p>';
            this.showModal();
        }
    }
    
    showModal() {
        this.historyModal.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }
    
    hideModal() {
        this.historyModal.style.display = 'none';
        document.body.style.overflow = 'auto';
    }
    
    showLoading() {
        this.loadingOverlay.style.display = 'flex';
    }
    
    hideLoading() {
        this.loadingOverlay.style.display = 'none';
    }
    
    showError(message) {
        // You could implement a toast notification here
        console.error(message);
        alert(message);
    }
    
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Initialize the chatbot when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new CricketChatbot();
});

// Add some additional CSS for history modal content
const additionalStyles = `
    .history-messages {
        max-height: 300px;
        overflow-y: auto;
    }
    
    .history-message {
        margin-bottom: 1.5rem;
        padding-bottom: 1rem;
        border-bottom: 1px solid #e9ecef;
    }
    
    .history-message:last-child {
        border-bottom: none;
        margin-bottom: 0;
    }
    
    .history-user-msg, .history-bot-msg {
        margin-bottom: 0.5rem;
        line-height: 1.5;
    }
    
    .history-user-msg {
        color: #2c3e50;
    }
    
    .history-bot-msg {
        color: #495057;
        padding-left: 1rem;
    }
    
    .history-user-msg small {
        color: #6c757d;
        font-weight: normal;
        margin-left: 0.5rem;
    }
`;

// Inject additional styles
const styleSheet = document.createElement('style');
styleSheet.textContent = additionalStyles;
document.head.appendChild(styleSheet);