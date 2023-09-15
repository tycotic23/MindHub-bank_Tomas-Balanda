Vue.createApp({
    data() {
        return {
            clientCards: [],
            errorToats: null,
            errorMsg: null,
            cardNumber:""
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current/cards")
                .then((response) => {
                    //get client ifo
                    this.clientCards = response.data.filter(card=>card.state=='ENABLED');
                })
                .catch((error) => {
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        deleting: function () {
            console.log(this.cardNumber.length);
            if (this.cardNumber.length!=19) {
                this.errorMsg = "You must select a card number";
                this.errorToats.show();
            } else {
                console.log("yay");
                let config = {
                    headers: {
                        'content-type': 'application/x-www-form-urlencoded'
                    }
                }
                axios.delete(`/api/cards?number=${this.cardNumber}`, config)
                    .then(response => window.location.href = "/web/cards.html")
                    .catch((error) => {
                        this.errorMsg = error.response.data;
                        this.errorToats.show();
                    })
            }
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')