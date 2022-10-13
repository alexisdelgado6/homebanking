const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            client: {},
            accounts: [],
            queryString: "",
            params: "",
            id: "",
            cards: [],
            cardType:"",
            cardColor:"",
            creditCard: [],
            debitCard: [],
            activeCards: [],
            cardNumber:"",
            dateNow:"",
            accountSelected:"",
            cardHolderSelected:"",
            cardNumberSelected:"",
            amountSelected:"",
            cvvSelected:"",
            thruDateSelected:"",
            descriptionSelected:"",
        }
    },
    created(){
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');
        this.loadClient();
        this.loadData();
        this.dateNow = (new Date(Date.now())).toLocaleDateString();
        this.newDate1();
    },
    mounted () {
        
    },
    methods: {
        loadClient(){
            axios.get('/api/clients/current')
            .then(response =>{
                this.clients = response.data
                this.accounts = this.clients.accounts.filter(e=> e.acountStatus == true)
                this.initialFirstName = this.clients.firstName.slice(0,1).toUpperCase()
                this.initialLastname = this.clients.lastName.slice(0,1).toUpperCase()
            })
        },
        loadData (){
            axios.get('/api/clients/current')
            .then(response =>{
                this.client = response.data
                this.cards = this.client.cards.sort((a,b) => a.id-b.id)
                this.activeCards = this.cards.filter(e => e.cardStatus)
                this.creditCard = this.activeCards.filter(e=> e.cardType == "CREDIT")
                this.debitCard = this.activeCards.filter(e=> e.cardType == "DEBIT")
                console.log(this.activeCards[0].thruDate)
            })
        },
        logout(){
            axios.post('/api/logout')
            .then(()=>{
                window.location.href='./index.html'
            })
        },
        createCards(){
            axios.post('/api/clients/current/cards',`cardColor=${this.cardColor}&cardType=${this.cardType}`)
            .then (() =>Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Your card has been created.',
                showConfirmButton: false,
                timer: 1500
            }))
            .then(() =>
                window.location.href='./card.html')
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${error.response.data}`,
                    }))
        },
        deletedCard(){
            axios.patch('/api/clients/current/cards', `cardNumber=${this.cardNumber}`)
            .then (() =>Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Your card has been deleted.',
                showConfirmButton: false,
                timer: 1500
            }))
            .then(()=>
            window.location.reload())
            .catch(error =>
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `${error.response.data}`,
                }))
        },
        newDate1(value){
            return value = new Date(value).toLocaleDateString();
        },
        makePayment() {
            Swal.fire({
            title: 'Are you sure?',
            showCancelButton: true,
            confirmButtonText: 'Payment',
            }).then((result) => {
            if (result.isConfirmed) {
                axios.post('/api/clients/current/transactions/payments', {accountNumber:this.accountSelected,cardHolder:this.cardHolderSelected,number:this.cardNumberSelected,amount:this.amountSelected,cvv:this.cvvSelected,thruDate:this.thruDateSelected,description:this.descriptionSelected})
                .then(() => Swal.fire("Successful payment","","success"))
                .then(()=>window.location.href="./accounts.html")
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${error.response.data}`,
                    }))
            }
            })
        },
},
    computed: {
    },
}).mount('#app')