class servicesHttp {
  constructor() {
      this.BASE_URL = "http://localhost:8080/";
  }

  chargerCats(successCallback, errorCallback) {
      $.ajax({
          type: "GET",
          dataType: "json",
          url: this.BASE_URL + "gateway/getCats",
          success: successCallback,
          error: errorCallback
      });
  }

  buyCat(catId, buyerId, successCallback, errorCallback) {
    $.ajax({
        type: "PUT",
        dataType: "json",
        url: this.BASE_URL + "gateway/updatePurchase",
        data: {
            id: catId,
            buyerId: buyerId
        },
        success: successCallback,
        error: errorCallback
    });
}

/**
 * Authenticate a user with email and password.
 * @param {string} email - User's email address.
 * @param {string} password - User's password.
 * @param {function} successCallback - Called on success.
 * @param {function} errorCallback - Called on error.
 */
 connect(email, password, successCallback, errorCallback) {
    $.ajax({
      type: "POST",
      dataType: "json",
      url: this.BASE_URL + "gateway/login",
      data: {
        action: 'connect',
        email: email,
        password: password
      },
   /*   xhrFields: {
        withCredentials: true // Include credentials in the request
      },*/
      success: successCallback,
      error: errorCallback
    });
  }
  
  /**
   * Disconnect the user.
   * @param {function} successCallback - Called on success.
   * @param {function} errorCallback - Called on error.
   */
   disconnect(successCallback, errorCallback) {
    $.ajax({
      type: "POST",
      dataType: "json",
      url:this.BASE_URL + "gateway/logout",
      data: 'action=disconnect',
      success: successCallback,
      error: errorCallback
    });
  }
}
