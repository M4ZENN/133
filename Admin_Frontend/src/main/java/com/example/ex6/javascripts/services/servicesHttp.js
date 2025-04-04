class ServicesHttp {
    constructor() {
        // Base URL for the API Gateway
        this.apiBaseUrl = "http://localhost:8080/gateway";
    }

    // Login method
    connect(email, password, successCallback, errorCallback) {
        $.ajax({
            url: `${this.apiBaseUrl}/login`,
            type: "POST",
            data: {
                email: email,
                password: password
            },
            success: function(response) {
                successCallback(response);
            },
            error: function(request, status, error) {
                errorCallback(request, status, error);
            }
        });
    }

    // Logout method
    disconnect(successCallback, errorCallback) {
        $.ajax({
            url: `${this.apiBaseUrl}/logout`,
            type: "POST",
            success: function(response) {
                successCallback(response);
            },
            error: function(request, status, error) {
                errorCallback(request, status, error);
            }
        });
    }

    // Get cats list
    getCats(successCallback, errorCallback) {
        $.ajax({
            url: `${this.apiBaseUrl}/cats`,
            type: "GET",
            success: function(response) {
                successCallback(response);
            },
            error: function(request, status, error) {
                errorCallback(request, status, error);
            }
        });
    }

    // Add a new cat
    addCat(catData, successCallback, errorCallback) {
        $.ajax({
            url: `${this.apiBaseUrl}/cat/add`,
            type: "POST",
            data: catData,
            success: function(response) {
                successCallback(response);
            },
            error: function(request, status, error) {
                errorCallback(request, status, error);
            }
        });
    }

    // Update a cat
    updateCat(catId, catData, successCallback, errorCallback) {
        $.ajax({
            url: `${this.apiBaseUrl}/cat/update/${catId}`,
            type: "PUT",
            data: catData,
            success: function(response) {
                successCallback(response);
            },
            error: function(request, status, error) {
                errorCallback(request, status, error);
            }
        });
    }

    // buy a cat
    buyCat(catId, catData, successCallback, errorCallback) {
        $.ajax({
            url: `${this.apiBaseUrl}/cat/update/${catId}`,
            type: "PUT",
            data: catData,
            success: function(response) {
                successCallback(response);
            },
            error: function(request, status, error) {
                errorCallback(request, status, error);
            }
        });
    }

    // Delete a cat
    deleteCat(catId, successCallback, errorCallback) {
        $.ajax({
            url: `${this.apiBaseUrl}/cat/delete/${catId}`,
            type: "DELETE",
            success: function(response) {
                successCallback(response);
            },
            error: function(request, status, error) {
                errorCallback(request, status, error);
            }
        });
    }
}