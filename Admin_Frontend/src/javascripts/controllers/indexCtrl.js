class IndexCtrl {
    constructor() {
        this.http = new servicesHttp(); // Instance du service HTTP
        this.callbackError = this.callbackError.bind(this);
        this.getCatsSuccess = this.getCatsSuccess.bind(this);
        this.deleteCatSuccess = this.deleteCatSuccess.bind(this);

        this.init(); // Chargement initial
        this.setupEventListeners(); // Définition des événements
    }

    // Charge la liste des chats au démarrage de la page
    init() {
        this.http.chargerCats(this.getCatsSuccess, this.callbackError);
    }

    // Configure les événements pour les boutons d’ajout et de déconnexion
    setupEventListeners() {
        // Bouton d’ajout d’un chat
        $("#add-cat-btn").on("click", () => {
            window.location.href = "addCat.html";
        });

        // Bouton de déconnexion
        $("#logout-btn").on("click", () => {
            // Redirige vers la page de login
            window.location.href = "login.html";
        });
    }

    // Callback en cas de succès du chargement des chats
    getCatsSuccess(data) {
        console.log("getCatsSuccess called", data);

        const catsListContainer = $("#cats-list");
        const templateCatCard = $(".cat-card.template-card");

        // Supprime les anciennes cartes (sauf le modèle)
        catsListContainer.find(".cat-card:not(.template-card)").remove();

        data.forEach((cat) => {
            const catCard = templateCatCard.clone();
            catCard.removeClass("template-card").css("display", "block");

            // Calcule l'âge du chat
            const birthDate = new Date(cat.birthdate);
            const today = new Date();
            let age = today.getFullYear() - birthDate.getFullYear();
            const monthDiff = today.getMonth() - birthDate.getMonth();
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }

            // Remplit les données du chat dans la carte
            $(catCard).find(".cat-header").text(cat.name);
            $(catCard).find(".cat-info .info-field p").eq(0).text(`${age} ans`);
            $(catCard).find(".cat-info .info-field p").eq(1).text(cat.breed.name.trim());
            $(catCard).find(".cat-info .info-field p").eq(2).text(cat.funFact);
            $(catCard).find(".cat-info .info-field p").eq(3).text(cat.description.trim());
<<<<<<< HEAD:Admin_Frontend/target/classes/com/example/ex6/javascripts/controllers/indexCtrl.js
    
            // Set image or fallback
x
    
            // Set the cat ID to the data-id attributes of the buttons
=======

            const image = cat.image || "https://coin-images.coingecko.com/coins/images/52603/large/OIALogo.jpg?1733756422"
            $(catCard).find("img").attr("src", image).attr("alt", cat.name);

            // Définition des attributs data-id pour les boutons
>>>>>>> f2ea6d69c206e6a8ffaaee58ff6c61fcafdc1f3e:Admin_Frontend/src/javascripts/controllers/indexCtrl.js
            $(catCard).find(".modify-cat").attr("data-id", cat.id);
            $(catCard).find(".delete-cat").attr("data-id", cat.id);

            // Bouton modifier : redirige vers la page de modification
            $(catCard).find(".modify-cat").on("click", (e) => {
                const catId = $(e.currentTarget).attr("data-id");
                window.location.href = `modifyCat.html?id=${catId}`;
            });

            // Bouton supprimer : demande confirmation puis supprime le chat
            $(catCard).find(".delete-cat").on("click", (e) => {
                const catId = $(e.currentTarget).attr("data-id");
                if (confirm("Êtes-vous sûr de vouloir supprimer ce chat ?")) {
                    this.http.deleteCat(catId, this.deleteCatSuccess, this.callbackError);
                }
            });

            // Ajoute la carte du chat dans le DOM
            catsListContainer.append(catCard);
        });

        console.log("Chats chargés et affichés avec succès");
    }

    // Callback après suppression d’un chat
    deleteCatSuccess(data) {
        console.log("Chat supprimé avec succès", data);
        if (data && data.message) {
            alert(data.message);
        }
        // Recharge la liste des chats
        this.http.chargerCats(this.getCatsSuccess, this.callbackError);
    }

    // Callback d’erreur pour toute requête échouée
    callbackError(request, status, error) {
        console.error("Erreur : " + error);
        alert("Erreur : " + error);
    }
}

// Démarrage du contrôleur quand la page est prête
$(document).ready(function () {
    window.ctrl = new IndexCtrl();
});
