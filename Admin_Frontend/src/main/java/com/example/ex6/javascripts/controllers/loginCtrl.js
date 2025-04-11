
class loginCtrl {
    constructor() {
        this.http = new servicesHttp();  // Assuming servicesHttp is already implemented
        this.callbackError = this.callbackError.bind(this);
        this.connectSuccess = this.connectSuccess.bind(this);
        this.disconnectSuccess = this.disconnectSuccess.bind(this);

        this.setupEventListeners();
    }

    /**
     * Sets up event listeners for login and logout buttons.
     */
    setupEventListeners() {
        const butConnect = $("#login-btn");
        const butDisconnect = $("#logout-btn");

        // Handle login button click
        butConnect.click((event) => {
            event.preventDefault(); // Prevent form submission

            const email = $("#email").val();
            const password = $("#password").val();
            console.log("Sending email:", email, "and password:", password);

            // Call the connect method via the service
            this.http.connect(email, password, this.connectSuccess, this.callbackError);
        });

        // Handle logout button click
        butDisconnect.click((event) => {
            // Call the disconnect method via the service
            this.http.disconnect(this.disconnectSuccess, this.callbackError);
        });
    }

    /**
     * Success callback for handling successful login.
     * It processes the data returned from the server.
     *
     * @param {Object} data - Data returned from the server, expected to contain user info.
     * @param {string} text - Status text from the server (not used here).
     * @param {Object} jqXHR - jQuery XMLHttpRequest object (not used here).
     */
    connectSuccess(data, text, jqXHR) {
        console.log("Login response:", data);

        const user = data.user;

        if (user && user.isAdmin === false) {
            // Store client credentials in localStorage
            localStorage.setItem("clientEmail", user.email);
            localStorage.setItem("clientId", user.id);

            alert("Connexion réussie !");
            window.location.href = "viewCat.html";
        } else if (user && user.isAdmin === true) {
            alert("Seuls les administrateur peuvent accéder à cette page.");
        } else {
            alert("Échec de la connexion : informations invalides.");
        }
    }

    /**
     * Success callback for handling successful logout.
     * It clears session storage and redirects the user to the login page.
     *
     * @param {Object} data - Data returned from the server (not used here).
     * @param {string} text - Status text from the server (not used here).
     * @param {Object} jqXHR - jQuery XMLHttpRequest object (not used here).
     */
    disconnectSuccess(data, text, jqXHR) {
        alert("User disconnected"); 
        // Clear localStorage upon logout
        localStorage.clear();
        // Redirect to login page
        window.location.href = "login.html";
    }

    /**
     * Error callback for handling errors during login or logout.
     * It logs the error and shows an alert with the error message.
     *
     * @param {Object} request - The jqXHR object containing the response.
     * @param {string} status - The status of the request.
     * @param {string} error - The error message returned from the server.
     */
    callbackError(request, status, error) {
        console.log("Error login:", error);
        alert("Error login: " + error);
    }
}

$(document).ready(function () {
    window.ctrl = new loginCtrl();
});