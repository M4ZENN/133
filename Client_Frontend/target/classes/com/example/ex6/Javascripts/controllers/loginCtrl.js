/*
 * Controller for the "login.html" view
 */

/**
 * This function is called when the login is successful. It processes the response and
 * stores the admin credentials in the local storage if the login is successful.
 * It then redirects to the "admin.html" view.
 * 
 * @param {Object} data - The data returned from the server, expected to be in XML format.
 * @param {string} text - The status text returned from the server (not used).
 * @param {Object} jqXHR - The jQuery XMLHttpRequest object.
 */
function connectSuccess(data, text, jqXHR) {
  console.log("Login response:", data);

  const user = data.user;

  if (user && user.isAdmin === false) {
    // Store client credentials
    localStorage.setItem("clientEmail", user.email);
    localStorage.setItem("clientId", user.id);

    alert("Connexion réussie !");
    window.location.href = "viewCat.html";
  } else if (user && user.isAdmin === true) {
    alert("Seuls les clients peuvent accéder à cette page.");
  } else {
    alert("Échec de la connexion : informations invalides.");
  }
}

  
  /**
   * This function is called when the user logs out. It clears the session storage and
   * redirects the user to the "index.html" view.
   * 
   * @param {Object} data - The data returned from the server (not used).
   * @param {string} text - The status text returned from the server (not used).
   * @param {Object} jqXHR - The jQuery XMLHttpRequest object (not used).
   */
  function disconnectSuccess(data, text, jqXHR) {
    alert("User disconnected");
  
    // Clear session storage upon logout
    localStorage.clear();
    // Redirect to index.html after logout
    window.location.href = "login.html";
  }
  
  /**
   * This function is called in case of an error while attempting to log in.
   * It logs the error and shows an alert with the error message.
   * 
   * @param {Object} request - The jqXHR object containing the response.
   * @param {string} status - The status of the request.
   * @param {string} error - The error message returned from the server.
   */
  function CallbackError(request, status, error) {
    console.log("Error login:", error); 
    alert("Error login: " + error);
  }
  
  /**
   * This function is called when the page is fully loaded. It sets up event handlers
   * for the login and logout buttons and handles the actions when clicked.
   */
  $(document).ready(function () {
    const service = new servicesHttp(); // 👈 create instance here
    var butConnect = $("#login-btn");
    var butDisconnect = $("#logout-btn");
  
    butConnect.click(function (event) {
      event.preventDefault();  // Prevent the form from submitting normally
  
      var email = $("#email").val();
      var password = $("#password").val();
      console.log("Sending email:", email, "and password:", password);
  
      // ✅ Use the instance to call connect
      service.connect(email, password, connectSuccess, CallbackError);
    });
  
    butDisconnect.click(function (event) {
      service.disconnect(disconnectSuccess, CallbackError);
    });
  });
  