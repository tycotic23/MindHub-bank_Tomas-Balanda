Vue.createApp({
    data() {
        return {
            clientInfo: {},
            creditCards: [],
            debitCards: [],
            errorToats: null,
            errorMsg: null,
            selectedCard:null,
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDIT" && card.state=="ENABLED");
                    this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBIT" && card.state=="ENABLED");
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
        enabledCards: function(){
            return this.clientInfo.cards.filter(card=>card.state=='ENABLED').length;
        },
        classCard: function(card){
            date=new Date();
            if(card.thruDate.substring(0,4)<date.getFullYear()){
                return 'outdate';
            }
            if(card.thruDate.substring(5,7)<date.getMonth() && card.thruDate.substring(0,4)==date.getFullYear()){
                return 'outdate';
            }
            if(card.thruDate.substring(8,10)<date.getDate() && card.thruDate.substring(5,7)==date.getMonth() && card.thruDate.substring(0,4)==date.getFullYear()){
                return 'outdate';
            }
            return 'outdate2';
        },
        renewCard: function(card){
            selectedCard=card;
            this.modal.show();
            
        },
        applyRenew: function () {
            axios.post(`/api/cards/renew?number=${selectedCard.number}`)
            .then(response => {
                this.modal.hide();
                this.okmodal.show();
            })
            .catch((error) => {
                this.errorMsg = error.response.data;
                this.errorToats.show();
            });
        },
        finish: function () {
            window.location.reload();
        },
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
    }
}).mount('#app')