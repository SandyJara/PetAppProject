document.addEventListener("DOMContentLoaded", () => {
    // use MutationObserver : monitor changes in DOMM
    const observer = new MutationObserver(() => { //this was needed to wait untiL user-name is readY to get it to my chat
        const currentUserElement = document.getElementById("user-username");
        const currentUser = currentUserElement ? currentUserElement.textContent.trim() : null;

        if (currentUser) {
            console.log("Current user:", currentUser);

            // Initialize functions in the chat
            initChatFunctions(currentUser);

            // stop observing is the user was found
            observer.disconnect();
        } else {
            console.warn("Waiting for 'user-username' to be dynamically filled.");//for testing
        }
    });

    // Configurating observer to detect changes in "document body"
    observer.observe(document.body, {
        childList: true,
        subtree: true,
    });
});

// Start chat functions
function initChatFunctions(currentUser) {
    // load conversation between users
    function loadConversation(otherUser) {
        if (!otherUser) {
            console.warn("No recipient specified for the conversation.");
            return;
        }

        fetch(`/messages/conversation?user1=${currentUser}&user2=${otherUser}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to load the conversation.");
                }
                return response.json();
            })
            .then(messages => {
                const container = document.getElementById("messagesContainer");
                container.innerHTML = ""; // Limpiar mensajes previos

                if (messages.length === 0) {
                    container.innerHTML = "<p><strong>No messages found for this conversation.</strong></p>";
                    return;
                }

                messages.forEach(msg => {
                    const formattedDate = new Date(msg.submissionDate).toLocaleString(); // Formatear fecha
                    const messageHTML = msg.senderUsername === currentUser
                        ? `<p><strong>To ${msg.receiverUsername}:</strong> ${msg.message} <br><small>${formattedDate}</small></p>`
                        : `<p><strong>${msg.senderUsername}:</strong> ${msg.message} <br><small>${formattedDate}</small></p>`;
                    container.innerHTML += messageHTML;
                });
            })
            .catch(error => {
                console.error("Error loading conversation:", error);
                const container = document.getElementById("messagesContainer");
                container.innerHTML = "<p><strong>Error loading messages.</strong></p>";
            });
    }

    // sending messages
    function sendMessage() {
        const receiverUsername = document.getElementById("receiverUsername").value.trim();
        const messageContent = document.getElementById("messageContent").value.trim();

        if (!receiverUsername || !messageContent) {
            alert("Both 'To' and 'Message' fields are required!");
            return;
        }

        console.log(`Sending message from: ${currentUser}, to: ${receiverUsername}, content: ${messageContent}`);

        fetch("/messages/send", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: `senderUsername=${currentUser}&receiverUsername=${receiverUsername}&message=${encodeURIComponent(messageContent)}`,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to send the message.");
                }
                return response.json();
            })
            .then(() => {
                loadConversation(receiverUsername); // upload conversacionn
                document.getElementById("sendMessageForm").reset(); // clean form
            })
            .catch(error => {
                console.error("Error sending message:", error);
                alert("An error occurred while sending the message. Please try again.");
            });
    }

    // Click del botón "Send"
    const messageButton = document.getElementById("messageButton");
    if (messageButton) {
        messageButton.addEventListener("click", function (e) {
            e.preventDefault(); 
            sendMessage();
        });
    } else {
        console.error("Message button not found. Ensure it has the correct ID: 'messageButton'.");
    }

    const receiverInput = document.getElementById("receiverUsername");
    if (receiverInput) {
        receiverInput.addEventListener("input", function () {
            const otherUser = this.value.trim(); //get the receptor name
            loadConversation(otherUser); // upload mensagges currentUser and receptor
        });
    } else {
        console.error("Receiver input field not found. Ensure it has the correct ID: 'receiverUsername'.");
    }
}
