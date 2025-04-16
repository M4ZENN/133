class ModifyCatCtrl {
    constructor() {
        this.http = new servicesHttp();
        this.catId = this.getCatIdFromUrl();

        // Bind des callbacks pour garder le contexte `this`
        this.getCatSuccess = this.getCatSuccess.bind(this);
        this.modifyCatSuccess = this.modifyCatSuccess.bind(this);
        this.getBreedsSuccess = this.getBreedsSuccess.bind(this);
        this.callbackError = this.callbackError.bind(this);

        this.init();
        this.setupEventListeners();
    }

    // Initialise le contrôleur en récupérant les données du chat et les races
    init() {
        if (this.catId) {
            this.http.getCat(this.catId, this.getCatSuccess, this.callbackError);
            this.http.getBreeds(this.getBreedsSuccess, this.callbackError);
        } else {
            alert("Identifiant du chat manquant dans l'URL");
            window.location.href = "login.html";
        }
    }

    // Ajoute les écouteurs d’événements pour le formulaire, l’annulation et l’image
    setupEventListeners() {
        $("#cat-form").on("submit", (e) => {
            e.preventDefault();
            this.submitForm();
        });

        $("#cancel-btn").on("click", () => {
            window.location.href = "index.html";
        });

        // Gère l’aperçu de l’image sélectionnée
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

    // Récupère l'ID du chat depuis l'URL
    getCatIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get("id");
    }

    // Callback de succès : remplit les champs du formulaire avec les données du chat
    getCatSuccess(data) {
        console.log("Cat data loaded successfully:", data);
        
        $("#add-cat-name").val(data.name);

        // Formatage de la date pour l’input
        const birthdate = new Date(data.birthdate);
        const formattedDate = birthdate.toISOString().split('T')[0];
        $("#add-cat-birthdate").val(formattedDate);

        this.catBreedId = data.breed.id;
        $("#add-cat-funfact").val(data.funFact);
        $("#add-cat-description").val(data.description);

        if (data.image) {
            $("#preview").attr("src", data.image).show();
        }

        $(".cat-header").text("Modifier le chat: " + data.name);

        // Recharge la liste des races
        this.http.getBreeds(this.getBreedsSuccess, this.callbackError);
    }

    // Remplit le menu déroulant avec les races, et sélectionne la bonne
    getBreedsSuccess(data) {
        console.log("Breeds loaded successfully:", data);
        const dropdown = $("#add-cat-breed");
        dropdown.find("option:not(:first)").remove();

        data.forEach(breed => {
            const option = $("<option></option>")
                .attr("value", breed.id)
                .text(breed.name);
            dropdown.append(option);
        });

        dropdown.val(this.catBreedId);
    }

    // Soumet les données du formulaire pour modifier le chat
    submitForm() {
        if (!this.validateForm()) return;

        const name = $("#add-cat-name").val();
        const birthdate = $("#add-cat-birthdate").val();
        const breedId = $("#add-cat-breed").val();
        const funFact = $("#add-cat-funfact").val();
        const description = $("#add-cat-description").val();

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

    // Vérifie que tous les champs obligatoires sont remplis
    validateForm() {
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

    // Callback de succès après la modification du chat
    modifyCatSuccess(data) {
        console.log("Cat modified successfully:", data);
        alert("Le chat a été modifié avec succès!");
        window.location.href = "viewCat.html";
    }

    // Callback en cas d’erreur API
    callbackError(request, status, error) {
        console.error("Error:", error);
        alert("Erreur: " + error);
    }
}

// Instancie le contrôleur une fois le DOM prêt
$(document).ready(function () {
    window.ctrl = new ModifyCatCtrl();
});
