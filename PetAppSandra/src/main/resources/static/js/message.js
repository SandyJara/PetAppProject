console.log("File loaded for Pet Sitters!"); // //i was testing because petsitter wasnt reading it, just owner

document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM fully loaded for Pet Sitters!");

     //  use MutationObserver : monitor changes in DOMM to wait the user name
    const checkUsernameInterval = setInterval(() => {
        const currentUserElement = document.getElementById("user-username");
        const currentUser = currentUserElement?.textContent.trim();

        if (currentUser) {
            console.log("Pet Sitter detected:", currentUser);

           // Initialize functions in the chat
            try {
                initChatFunctions(currentUser);
                console.log("Chat functions initialized for Pet Sitter:", currentUser);
            } catch (error) {
                console.error("Error initializing chat functions:", error);
            }

            // Stop the interval once functions initialized
            clearInterval(checkUsernameInterval);
        } else {
            console.warn("Waiting for 'user-username' to be dynamically filled (Pet Sitter).");
        }
    }, 500); // Check every 500ms, this was the only way to make it work for the petsitters because the username wansnt arriving to start the functions
});

// Start chat functions
function initChatFunctions(currentUser) {
    console.log("Initializing chat functions for:", currentUser);

    // load previous conversations
    function loadPreviousConversations() {
        console.log("Loading previous conversations for Pet Sitter...");
        fetch(`/messages/conversations/users?username=${currentUser}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to load previous conversations.");
                }
                return response.json();
            })
            .then((users) => {
                console.log("Previous conversations retrieved (Pet Sitter):", users);
                const datalist = document.getElementById("conversationList");
                datalist.innerHTML = ""; // clean

                if (users.length === 0) {
                    console.log("No previous conversations found for Pet Sitter.");
                    return;
                }

                users.forEach((user) => {
                    const option = document.createElement("option");
                    option.value = user; // add user name
                    datalist.appendChild(option);
                });
            })
            .catch((error) => {
                console.error("Error loading previous conversations (Pet Sitter):", error);
            });
    }

    // load conversations
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
                container.innerHTML = ""; // clean messages

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
                loadConversation(receiverUsername); //load
                loadPreviousConversations(); //load
                document.getElementById("sendMessageForm").reset(); // clean, resert form
            })
            .catch((error) => {
                console.error("Error sending message:", error);
            });
    }

   //Functions globally accessible
    window.sendMessage = sendMessage;
    window.loadConversation = loadConversation;

    // Click  "Send"
    const messageButton = document.getElementById("messageButton");
    if (messageButton) {
        messageButton.addEventListener("click", (e) => {
            e.preventDefault();
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
        console.error("Receiver input field not found for Pet Sitter.");
    }

    // Load conversations from before
    loadPreviousConversations();
}
