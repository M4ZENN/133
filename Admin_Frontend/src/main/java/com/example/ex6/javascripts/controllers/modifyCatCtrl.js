class ModifyCatCtrl {
    constructor() {
        this.http = new servicesHttp();
        this.catId = this.getCatIdFromUrl();
        
        // Bind callbacks to preserve 'this' context
        this.getCatSuccess = this.getCatSuccess.bind(this);
        this.modifyCatSuccess = this.modifyCatSuccess.bind(this);
        this.getBreedsSuccess = this.getBreedsSuccess.bind(this);
        this.callbackError = this.callbackError.bind(this);
        
        this.init();
        this.setupEventListeners();
    }

    init() {
        // Load the cat data using the ID from URL
        if (this.catId) {
            this.http.getCat(this.catId, this.getCatSuccess, this.callbackError);
            // Also load the breeds for the dropdown
            this.http.getBreeds(this.getBreedsSuccess, this.callbackError);
        } else {
            alert("Identifiant du chat manquant dans l'URL");
            window.location.href = "login.html";
        }
    }

    setupEventListeners() {
        // Handle form submission
        $("#cat-form").on("submit", (e) => {
            e.preventDefault();
            this.submitForm();
        });

        // Handle cancel button
        $("#cancel-btn").on("click", () => {
            window.location.href = "index.html";
        });

        // Handle image preview
        $("#file-input").on("change", (e) => {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    $("#preview").attr("src", e.target.result).show();
                };
                reader.readAsDataURL(file);
            }
        });
    }

    getCatIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get("id");
    }

    getCatSuccess(data) {
        console.log("Cat data loaded successfully:", data);
        
        // Populate form fields with cat data
        $("#add-cat-name").val(data.name);
        
        // Format the date to YYYY-MM-DD for the date input
        const birthdate = new Date(data.birthdate);
        const formattedDate = birthdate.toISOString().split('T')[0];
        $("#add-cat-birthdate").val(formattedDate);
        
        // The breed dropdown will be populated in getBreedsSuccess, 
        // but we need to remember which one to select
        this.catBreedId = data.breed.id;
        
        $("#add-cat-funfact").val(data.funFact);
        $("#add-cat-description").val(data.description);
        
        // If there's an image, show it in the preview
        if (data.image) {
            $("#preview").attr("src", data.image).show();
        }
        
        // Update page title
        $(".cat-header").text("Modifier le chat: " + data.name);
    }

    getBreedsSuccess(data) {
        console.log("Breeds loaded successfully:", data);
        const dropdown = $("#add-cat-breed");
        
        // Clear any existing options except the default one
        dropdown.find("option:not(:first)").remove();
        
        // Add breed options
        data.forEach(breed => {
            const option = $("<option></option>")
                .attr("value", breed.id)
                .text(breed.name);
                
            // If this is the cat's current breed, select it
            if (breed.id === this.catBreedId) {
                option.attr("selected", "selected");
            }
            
            dropdown.append(option);
        });
    }

    submitForm() {
        // Validate the form
        if (!this.validateForm()) {
            return;
        }
        
        // Collect form data
        const name = $("#add-cat-name").val();
        const birthdate = $("#add-cat-birthdate").val();
        const breedId = $("#add-cat-breed").val();
        const funFact = $("#add-cat-funfact").val();
        const description = $("#add-cat-description").val();
        
        // Call the API to modify the cat
        this.http.modifyCat(
            this.catId,
            name, 
            birthdate, 
            breedId, 
            funFact, 
            description, 
            this.modifyCatSuccess, 
            this.callbackError
        );
    }

    validateForm() {
        // Check required fields
        const name = $("#add-cat-name").val();
        const birthdate = $("#add-cat-birthdate").val();
        const breedId = $("#add-cat-breed").val();
        const description = $("#add-cat-description").val();
        
        if (!name || !birthdate || !breedId || !description) {
            alert("Veuillez remplir tous les champs obligatoires");
            return false;
        }
        
        return true;
    }

    modifyCatSuccess(data) {
        console.log("Cat modified successfully:", data);
        alert("Le chat a été modifié avec succès!");
        window.location.href = "index.html";
    }

    callbackError(request, status, error) {
        console.error("Error:", error);
        alert("Erreur: " + error);
    }
}

$(document).ready(function () {
    window.ctrl = new ModifyCatCtrl();
});