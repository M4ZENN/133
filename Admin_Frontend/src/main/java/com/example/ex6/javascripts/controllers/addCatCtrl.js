class AddCatCtrl {
    constructor() {
        this.http = new servicesHttp();
        this.callbackError = this.callbackError.bind(this);
        this.getBreedsSuccess = this.getBreedsSuccess.bind(this);
        this.addCatSuccess = this.addCatSuccess.bind(this);
        
        this.init();
        this.setupEventListeners();
    }

    init() {
        // Clear form fields
        this.clearForm();
        
        // Fetch breeds for dropdown
        this.fetchBreeds();
    }
    
    clearForm() {
        // Reset all form fields with new IDs
        $("#add-cat-name").val("");
        $("#add-cat-birthdate").val("");
        $("#add-cat-breed").val("");
        $("#add-cat-funfact").val("");
        $("#add-cat-description").val("");
        $("#preview").attr("src", "").css("display", "none");
        $("#file-input").val("");
    }
    
    fetchBreeds() {
        // Fetch breeds for the dropdown using the existing method
        this.http.getBreeds(this.getBreedsSuccess, this.callbackError);
    }
    
    getBreedsSuccess(data) {
        console.log("Breeds loaded successfully", data);
        
        // Select the breed dropdown with the new ID
        const breedSelect = $("#add-cat-breed");
        // Clear existing options
        breedSelect.empty();
        
        // Add a default option
        breedSelect.append($("<option>").attr("value", "").text("Sélectionnez une race"));
        
        // Add each breed as an option
        data.forEach(breed => {
            breedSelect.append($("<option>").attr("value", breed.id).text(breed.name));
        });
    }

    setupEventListeners() {
        // Back and cancel buttons event listener
        $("#cancel-btn").on("click", () => {
            window.location.href = "viewCat.html";
        });
        
        // Form submission
        $("#cat-form").on("submit", (e) => {
            e.preventDefault();
            
            // Get form data with new IDs
            const name = $("#add-cat-name").val();
            const birthdate = $("#add-cat-birthdate").val();
            const breedId = $("#add-cat-breed").val();
            const funFact = $("#add-cat-funfact").val();
            const description = $("#add-cat-description").val();
            
            // Validate form data
            if (!name || !birthdate || !breedId || !description) {
                alert("Veuillez remplir tous les champs obligatoires.");
                return;
            }
            
            // Use the existing addCat method from servicesHttp
            this.http.addCat(
                name, 
                birthdate, 
                breedId, 
                funFact, 
                description, 
                this.addCatSuccess, 
                this.callbackError
            );
        });
        
        // Image preview handler
        $("#file-input").on("change", (e) => {
            const preview = $("#preview")[0];
            const file = e.target.files[0];
            
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    $(preview).attr("src", e.target.result).css("display", "block");
                }
                reader.readAsDataURL(file);
            }
        });
    }
    
    addCatSuccess(data) {
        console.log("Chat ajouté avec succès", data);
        alert("Chat ajouté avec succès!");
        window.location.href = "index.html";
    }

    callbackError(request, status, error) {
        console.error("Error: " + error);
        alert("Erreur: " + error);
    }
}

$(document).ready(function () {
    window.ctrl = new AddCatCtrl();
});