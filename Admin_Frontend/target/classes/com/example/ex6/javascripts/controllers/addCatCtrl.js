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
        window.location.href = "viewCat.html";

        this.http.chargerCats(this.getCatsSuccess, this.callbackError);
    }

    /**
     * Success callback for loading the list of cats from the server.
     * @param {Object} data - The raw data returned from the server.
     */
    getCatsSuccess(data) {
        console.log("getCatsSuccess called", data);
    
        const catsListContainer = $("#cats-list");
        const templateCatCard = $(".cat-card.template-card");
        
        // Clear existing cats (except the template)
        catsListContainer.find(".cat-card:not(.template-card)").remove();
    
        data.forEach((cat) => {
            const catCard = templateCatCard.clone()[0]; // Clone template card
            $(catCard).removeClass("template-card");
    
            // Calculate age from birthdate
            const birthDate = new Date(cat.birthdate);
            const today = new Date();
            let age = today.getFullYear() - birthDate.getFullYear();
            const monthDiff = today.getMonth() - birthDate.getMonth();
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
    
            // Fill in cat data
            $(catCard).find(".cat-header").text(cat.name);
            $(catCard).find(".cat-info .info-field p").eq(0).text(`${age} ans`);
            $(catCard).find(".cat-info .info-field p").eq(1).text(cat.breed.name.trim());
            $(catCard).find(".cat-info .info-field p").eq(2).text(cat.funFact);
            $(catCard).find(".cat-info .info-field p").eq(3).text(cat.description.trim());
    
            // Set image or fallback
            const image = cat.image || "https://via.placeholder.com/200x150?text=No+Image";
            $(catCard).find("img").attr("src", image).attr("alt", cat.name);
    
            // Set the cat ID to the data-id attributes of the buttons
            $(catCard).find(".modify-cat").attr("data-id", cat.id);
            $(catCard).find(".delete-cat").attr("data-id", cat.id);
            
            // Add click handlers for the modify and delete buttons
            $(catCard).find(".modify-cat").on("click", (e) => {
                const catId = $(e.currentTarget).attr("data-id");
                window.location.href = `modify.html?id=${catId}`;
            });
            
            $(catCard).find(".delete-cat").on("click", (e) => {
                const catId = $(e.currentTarget).attr("data-id");
                if (confirm("Êtes-vous sûr de vouloir supprimer ce chat ?")) {
                    this.http.deleteCat(catId, null, this.deleteCatSuccess, this.callbackError);
                }
            });
            
            // Append to list
            catsListContainer.append(catCard);
        });
    
        console.log("Cats loaded and displayed successfully");
    }

    callbackError(request, status, error) {
        // Handle errors when loading cats
        console.error("Error: " + error);
        alert("Error: " + error);
    }

    callbackError(request, status, error) {
        console.error("Error: " + error);
        alert("Erreur: " + error);
    }
}

$(document).ready(function () {
    window.ctrl = new AddCatCtrl();
});