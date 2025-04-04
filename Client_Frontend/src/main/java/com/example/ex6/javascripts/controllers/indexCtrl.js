class IndexCtrl {
    constructor() {
        this.http = new servicesHttp();
        this.connectSuccess = this.connectSuccess.bind(this);
        this.disconnectSuccess = this.disconnectSuccess.bind(this);
        this.callbackError = this.callbackError.bind(this);
        this.getCatsSuccess = this.getCatsSuccess.bind(this);
        
        this.init();
    }

    init() {
        const user = JSON.parse(localStorage.getItem("user"));
        if (user) {
            this.displayUser(user);
        }
    }

    connectSuccess(data) {
        console.log("connectSuccess called", data);
        
        if (data.success) {
            localStorage.setItem("user", JSON.stringify(data));
            this.displayUser(data);

            Toastify({
                text: "Login successful",
                duration: 3000,
                gravity: "top",
                position: "right",
                backgroundColor: "#33cc33"
            }).showToast();
        }
    }

    disconnectSuccess() {
        localStorage.removeItem("user");
        Toastify({
            text: "User disconnected",
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#33cc33"
        }).showToast();
        window.location.href = "./login.html";
    }

    displayUser(user) {
        $("#user-greeting").text(`Bienvenue, ${user.name}`);
        $("#login-section").hide();
        $("#dashboard-section").show();
    }

    getCatsSuccess(data) {
        console.log("getCatsSuccess called", data);
        
        if (data.cats && data.cats.length > 0) {
            const cat = data.cats[0]; // On prend le premier chat pour remplir les champs
            
            $("#catName").val(cat.name);
            $("#catBirthdate").val(cat.birthdate);
            $("#catFunFact").val(cat.funFact);
            $("#catDescription").val(cat.description);
            
            if (cat.image) {
                $("#catImage").attr("src", `data:image/jpeg;base64,${cat.image}`);
            }
            
            $("#catIsPurchased").prop("checked", cat.isPurchased);
            
            // Charger les races dans la liste déroulante
            const breedSelect = $("#catBreed");
            breedSelect.empty();
            data.breeds.forEach(breed => {
                breedSelect.append(new Option(breed.name, breed.pk_breed));
            });
            breedSelect.val(cat.fk_breed);
            
            Toastify({
                text: "Cat details loaded successfully",
                duration: 3000,
                gravity: "top",
                position: "right",
                backgroundColor: "#33cc33"
            }).showToast();
        }
    }

    callbackError(request, status, error) {
        Toastify({
            text: "Error: " + error,
            duration: 3000,
            gravity: "top",
            position: "right",
            backgroundColor: "#ff3333"
        }).showToast();
    }
}

$(document).ready(function () {
    window.ctrl = new IndexCtrl();
    
    $("#loginForm").on("submit", function (event) {
        event.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        console.log("Form submitted", { email, password });
        window.ctrl.http.connect(email, password, window.ctrl.connectSuccess, window.ctrl.callbackError);
    });
    
    $("#logout-btn").on("click", function () {
        window.ctrl.disconnectSuccess();
    });
    
    if ($("#catName").length) {
        console.log("Loading cat details");
        window.ctrl.http.getCats(window.ctrl.getCatsSuccess, window.ctrl.callbackError);
    }
    
    $("#addButton").on("click", function () {
        window.location.href = "./views/addCat.html";
    });
    
    $("#modifyButton").on("click", function () {
        window.location.href = "./views/modifyCat.html";
    });
});