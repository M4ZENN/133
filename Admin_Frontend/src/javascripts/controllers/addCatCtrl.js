class AddCatCtrl {
    constructor() {
        this.http = new servicesHttp(); // Service HTTP pour gérer les requêtes
        this.callbackError = this.callbackError.bind(this);
        this.getBreedsSuccess = this.getBreedsSuccess.bind(this);
        this.addCatSuccess = this.addCatSuccess.bind(this);
        
        this.init(); // Initialisation
        this.setupEventListeners(); // Configuration des écouteurs d’événements
    }

    // Initialise le formulaire et récupère les races de chats
    init() {
        this.clearForm(); // Efface tous les champs du formulaire
        this.fetchBreeds(); // Récupère les races pour le dropdown
    }
    
    // Efface tous les champs du formulaire
    clearForm() {
        $("#add-cat-name").val("");
        $("#add-cat-birthdate").val("");
        $("#add-cat-breed").val("");
        $("#add-cat-funfact").val("");
        $("#add-cat-description").val("");
        $("#preview").attr("src", "").css("display", "none"); // Cache l’image
        $("#file-input").val(""); // Réinitialise l'input de fichier
    }
    
    // Récupère la liste des races pour le dropdown
    fetchBreeds() {
        this.http.getBreeds(this.getBreedsSuccess, this.callbackError);
    }
    
    // Callback en cas de succès pour récupérer les races
    getBreedsSuccess(data) {
        console.log("Races chargées avec succès", data);
        
        const breedSelect = $("#add-cat-breed");
        breedSelect.empty(); // Vide les options existantes
        
        // Ajoute une option par défaut
        breedSelect.append($("<option>").attr("value", "").text("Sélectionnez une race"));
        
        // Ajoute chaque race comme option dans le dropdown
        data.forEach(breed => {
            breedSelect.append($("<option>").attr("value", breed.id).text(breed.name));
        });
    }

    // Configure les événements pour les boutons et le formulaire
    setupEventListeners() {
        // Bouton "Annuler" qui redirige vers la vue des chats
        $("#cancel-btn").on("click", () => {
            window.location.href = "viewCat.html";
        });
        
        // Soumission du formulaire pour ajouter un chat
        $("#cat-form").on("submit", (e) => {
            e.preventDefault(); // Empêche la soumission du formulaire

            // Récupère les données du formulaire
            const name = $("#add-cat-name").val();
            const birthdate = $("#add-cat-birthdate").val();
            const breedId = $("#add-cat-breed").val();
            const funFact = $("#add-cat-funfact").val();
            const description = $("#add-cat-description").val();
            
            // Vérifie que tous les champs requis sont remplis
            if (!name || !birthdate || !breedId || !description) {
                alert("Veuillez remplir tous les champs obligatoires.");
                return;
            }
            
            // Appel de la méthode pour ajouter un chat
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
        
        // Gestion de l'aperçu de l'image
        $("#file-input").on("change", (e) => {
            const preview = $("#preview")[0];
            const file = e.target.files[0];
            
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    $(preview).attr("src", e.target.result).css("display", "block"); // Affiche l'image
                }
                reader.readAsDataURL(file);
            }
        });
    }
    
    // Callback en cas de succès après l'ajout d'un chat
    addCatSuccess(data) {
        console.log("Chat ajouté avec succès", data);
        alert("Chat ajouté avec succès!");
        window.location.href = "viewCat.html"; // Redirige vers la page des chats
    }

    // Callback d’erreur pour gérer les erreurs des requêtes HTTP
    callbackError(request, status, error) {
        console.error("Erreur : " + error);
        alert("Erreur : " + error);
    }
}

// Initialisation du contrôleur lorsque le document est prêt
$(document).ready(function () {
    window.ctrl = new AddCatCtrl();
});
