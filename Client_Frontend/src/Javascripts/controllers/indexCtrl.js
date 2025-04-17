class IndexCtrl {
    constructor() {
        // Initialise les dépendances, lie les méthodes et lance l'initialisation
        this.http = new servicesHttp();
        this.callbackError = this.callbackError.bind(this);
        this.getCatsSuccess = this.getCatsSuccess.bind(this);
        this.handleBuy = this.handleBuy.bind(this);
        this.init();
    }

    // Vérifie si l'utilisateur est connecté et charge les chats
    init() {
        if (!localStorage.getItem("clientEmail") || !localStorage.getItem("clientId")) {
            window.location.href = "login.html"; // Redirection si non connecté
            return;
        }

        this.http.chargerCats(this.getCatsSuccess, this.callbackError);
    }

    // Affiche les chats récupérés depuis le serveur
    getCatsSuccess(data) {
        const catsListContainer = $("#cats-list");
        const templateCatCard = $(".cat-card.template-card");

        data.forEach((cat) => {
            const catCard = templateCatCard.clone()[0];
            catCard.style.display = "block";

            // Calcule l’âge du chat
            const birthDate = new Date(cat.birthdate);
            const today = new Date();
            let age = today.getFullYear() - birthDate.getFullYear();
            const monthDiff = today.getMonth() - birthDate.getMonth();
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }

            // Remplit les infos du chat dans la carte
            $(catCard).find(".cat-header").text(cat.name);
            $(catCard).find(".cat-info .info-field p").eq(0).text(`${age} ans`);
            $(catCard).find(".cat-info .info-field p").eq(1).text(cat.breed.name.trim());
            $(catCard).find(".cat-info .info-field p").eq(2).text(cat.funFact);
            $(catCard).find(".cat-info .info-field p").eq(3).text(cat.description.trim());

            const image = cat.image || "https://coin-images.coingecko.com/coins/images/52603/large/OIALogo.jpg?1733756422"
            $(catCard).find("img").attr("src", image).attr("alt", cat.name);

            // Configure le bouton d’achat si le chat n’est pas déjà acheté
            const buyBtn = $(catCard).find(".btn-buy");
            if (cat.isPurchased === 1 || cat.isPurchased === true) {
                buyBtn.prop("disabled", true).text("Déjà acheté");
            } else {
                buyBtn.on("click", () => this.handleBuy(cat.id, catCard));
            }

            catsListContainer.append(catCard);
        });
    }

    // Gère l'achat d’un chat (envoie requête et met à jour l’UI)
    handleBuy(catId, catCard) {
        const buyerId = localStorage.getItem("clientId");
        if (!buyerId) {
            alert("Vous devez être connecté pour acheter un chat.");
            return;
        }

        this.http.buyCat(catId, buyerId,
            () => {
                $(catCard).find(".btn-buy").prop("disabled", true).text("Acheté !");
                console.log(`Chat ${catId} acheté par ${buyerId}`);
            },
            (xhr, status, error) => {
                console.error("Erreur lors de l'achat :", error);
                alert("Erreur lors de l'achat du chat.");
            }
        );
    }

    // Gère les erreurs lors du chargement des chats
    callbackError(request, status, error) {
        console.error("Erreur lors du chargement des chats : " + error);
        alert("Erreur lors du chargement des chats : " + error);
    }
}

$(document).ready(function () {
    window.ctrl = new IndexCtrl();
});
