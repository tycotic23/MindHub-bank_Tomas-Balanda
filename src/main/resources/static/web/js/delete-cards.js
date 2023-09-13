Vue.createApp({
    data() {
        return {
            errorToats: null,
            errorMsg: null,
            cardNumber:""
        }
    },
    methods: {
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
    }
}).mount('#app')