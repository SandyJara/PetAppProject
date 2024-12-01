console.log("File loaded!");//i was testing because petsitter wasnt reading it, just owner

document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM fully loaded!");

    //  use MutationObserver : monitor changes in DOMM
    const observer = new MutationObserver(() => {
        const currentUserElement = document.getElementById("user-username");
        const currentUser = currentUserElement?.textContent.trim();

        if (currentUser) {
            console.log("Current user detected:", currentUser);

            // Initialize functions in the chat
            initChatFunctions(currentUser);

            // Initialize functions in the chat
            observer.disconnect();
        } else {
            console.warn("Waiting for 'user-username' to be dynamically filled.");
        }
    });

    // Configurating observer to detect changes in "document body"
    observer.observe(document.body, { childList: true, subtree: true });
});

// Start chat functions
function initChatFunctions(currentUser) {
    console.log("Initializing chat functions for:", currentUser);

    // load conversation
    function loadConversation(otherUser) {
        console.log("Loading conversation with:", otherUser);
        if (!otherUser) {
            console.warn("Recipient username is missing.");
            return;
        }

        fetch(`/messages/conversation?user1=${currentUser}&user2=${otherUser}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to load the conversation.");
                }
                return response.json();
            })
            .then((messages) => {
                const container = document.getElementById("messagesContainer");
                container.innerHTML = "";

                if (messages.length === 0) {
                    container.innerHTML = "<p>No messages found.</p>";
                    return;
                }

                messages.forEach((msg) => {
                    const formattedDate = new Date(msg.submissionDate).toLocaleString();
                    const messageHTML =
                        msg.senderUsername === currentUser
                            ? `<p><strong>To ${msg.receiverUsername}:</strong> ${msg.message} <br><small>${formattedDate}</small></p>`
                            : `<p><strong>${msg.senderUsername}:</strong> ${msg.message} <br><small>${formattedDate}</small></p>`;
                    container.innerHTML += messageHTML;
                });
            })
            .catch((error) => {
                console.error("Error loading conversation:", error);
            });
    }

    // sending messages
    function sendMessage() {
        const receiverUsername = document.getElementById("receiverUsername").value.trim();
        const messageContent = document.getElementById("messageContent").value.trim();

        if (!receiverUsername || !messageContent) {
            alert("Both 'To' and 'Message' fields are required.");
            return;
        }

        console.log(`Sending message from: ${currentUser} to ${receiverUsername}`);

        fetch("/messages/send", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: `senderUsername=${currentUser}&receiverUsername=${receiverUsername}&message=${encodeURIComponent(messageContent)}`,
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to send the message.");
                }
                return response.json();
            })
            .then(() => {
                loadConversation(receiverUsername); // load
                document.getElementById("sendMessageForm").reset(); // clean
            })
            .catch((error) => {
                console.error("Error sending message:", error);
            });
    }

    //Functions globally accessible
    window.sendMessage = sendMessage;
    window.loadConversation = loadConversation;

   // Click del botón "Send"
    const messageButton = document.getElementById("messageButton");
    if (messageButton) {
        messageButton.addEventListener("click", (e) => {
            e.preventDefault();
            console.log("Send button clicked!");
            sendMessage();
        });
    } else {
        console.error("Message button not found.");
    }

    //upload conversation choosing a receiver
    const receiverInput = document.getElementById("receiverUsername");
    if (receiverInput) {
        receiverInput.addEventListener("input", function () {
            const otherUser = this.value.trim();
            loadConversation(otherUser);
        });
    } else {
        console.error("Receiver input field not found.");
    }
}
